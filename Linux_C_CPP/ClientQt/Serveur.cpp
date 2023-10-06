#include <stdio.h> 
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>
#include <pthread.h>
#include <signal.h>
#include <mysql.h>

#include "SocketLib.h"
#include "protocole.h"

void HandlerSIGINT(int s);

void* FctCaddie(void * );

//pthread_t threadCaddie[10];

#define NB_THREADS_POOL 4
#define TAILLE_FILE_ATTENTE 20
int socketsAcceptees[TAILLE_FILE_ATTENTE];
int indiceEcriture=0, indiceLecture=0;
pthread_mutex_t mutexSocketsAcceptees;
pthread_cond_t condSocketsAcceptees;

pthread_mutex_t mutexBd;


int sServeur ;
//int clients[NB_MAX_CLIENTS];
//int nbClients = 0;


//ACCES BASE DE DONNEES 
//MYSQL_RES  *resultat;
//MYSQL_ROW  Tuple;
MYSQL * connexion;


 int main() 
 {  
     //preparation de la base de données
    connexion = mysql_init(NULL);
    if (mysql_real_connect(connexion,"localhost","Student","PassStudent1_","PourStudent",0,0,0) == NULL)
    {
        fprintf(stderr,"(SERVEUR) Erreur de connexion à la base de données...\n");
        exit(1);  
    }
    printf("Serveur - connexion réalisé\n");

     // preparation du stockage des descripteur de socket 
     for (int i=0 ; i<TAILLE_FILE_ATTENTE ; i++)
     {
         socketsAcceptees[i] = -1;
     }
            
    // armement de sigaction
    struct sigaction A;
    A.sa_flags = 0;
    sigemptyset(&A.sa_mask);
    A.sa_handler = HandlerSIGINT;
    if (sigaction(SIGINT,&A,NULL) == -1)
    {
        perror("Serveur - Erreur de sigaction");
        exit(1);
    }

    // creation du pool de thread 
    pthread_t th;
    for (int i=0 ; i<NB_THREADS_POOL ; i++)
    {
      pthread_create(&th,NULL,FctCaddie,NULL);
    }


    int sService;
    
    sServeur = Socket::ServerSocket(1500);

     while(1)
    {
        printf("Serveur -  Attente d'une connexion...\n");
        if ((sService = Socket::Accept(sServeur,NULL)) == -1)
        {
            perror("Serveur - Erreur de Accept");
            close(sServeur);
            exit(1);
        }
        printf("Serveur -  Connexion acceptée : socket=%d\n",sService);
        // Insertion en liste d'attente et réveil d'un thread du pool
        // (Production d'une tâche)
        pthread_mutex_lock(&mutexSocketsAcceptees);
        socketsAcceptees[indiceEcriture] = sService; // !!!
        indiceEcriture++;
        if (indiceEcriture == TAILLE_FILE_ATTENTE)
        {
            indiceEcriture = 0;
        } 
        pthread_mutex_unlock(&mutexSocketsAcceptees);
        pthread_cond_signal(&condSocketsAcceptees);
    }
}

void*FctCaddie(void * )
{

    int sService;
    int result;
    bool retour , fini;
    char charReceive[60] ,charReceiveCopy[60];
    char reponse[60];
    char requeteS[15];

    printf("\t[THREAD %ld] Créer\n",pthread_self());

    while(1)
    {
        // Attente d'une tâche
        printf("Thread %ld - Je suis libre\n",pthread_self());
        pthread_mutex_lock(&mutexSocketsAcceptees);
        while (indiceEcriture == indiceLecture)
        {
            pthread_cond_wait(&condSocketsAcceptees,&mutexSocketsAcceptees);
        }
        sService = socketsAcceptees[indiceLecture];
        socketsAcceptees[indiceLecture] = -1;
        indiceLecture++;
        if (indiceLecture == TAILLE_FILE_ATTENTE)
        {
            indiceLecture = 0;
        } 
        pthread_mutex_unlock(&mutexSocketsAcceptees);

        printf("\t[THREAD %ld] Je m'occupe de la socket %d\n", pthread_self(),sService);

        retour = true; 
        fini = false;
        while(retour == true && fini == false)
        {
            if((result = Socket::Receive(sService, charReceive)) == -1)
            {
                printf("\t[THREAD %ld] - Receive mal passe\n",pthread_self());
            }
            else
            {
                printf("\t[THREAD %ld] - j'ai reçu : %s\n",pthread_self(),charReceive);  
                strcpy(charReceiveCopy,charReceive);

                strcpy(requeteS,strtok(charReceiveCopy,"#"));
                printf("\tRequete reçue : %s\n",requeteS);
                if(strcmp(requeteS,"LOGOUT")==0)//ça ne sert a rien de bloquer la base de donnée pour rien
                { 
                    printf("\tDemande de logout\n");
                    fini = SMOP_Logout( reponse, sService);
                }
                else
                {
                    pthread_mutex_lock(&mutexBd);
                    retour = SMOP(charReceive,reponse, sService, connexion);
                    pthread_mutex_unlock(&mutexBd);
                }

                if((Socket::Send(sService, reponse, sizeof(reponse))) != -1)
                {
                    printf("\t[THREAD %ld] - Renvoyé au client %s\n\n\n",pthread_self(),reponse);
                }
            }
            strcpy(charReceive,"");
            strcpy(reponse,"");
        }
    }
    return 0;
}

void HandlerSIGINT(int s)
{
    printf("\nServeur - Arret du serveur.\n");
    close(sServeur);
    pthread_mutex_lock(&mutexSocketsAcceptees);
    for (int i=0 ; i<TAILLE_FILE_ATTENTE ; i++)
    if (socketsAcceptees[i] != -1) close(socketsAcceptees[i]);
    pthread_mutex_unlock(&mutexSocketsAcceptees);
    
    exit(0);
}

