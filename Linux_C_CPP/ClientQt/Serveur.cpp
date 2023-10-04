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

pthread_t threadCaddie[10];


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
    /* test sur le fait de recup une requete CA MARCHE 


        if (mysql_query(connexion, "select * from UNIX_FINAL where id = 1") != 0)   // cense renvoyer les elts sur carottes 
        {
        fprintf (stderr, "Serveur - Erreur de Mysql-query");
        }

        if((resultat = mysql_store_result(connexion)) == NULL)
        {
        fprintf (stderr, "Serveur - Erreur de mysql store");
        }
        //
        // Preparation de la reponse
        if ((Tuple = mysql_fetch_row(resultat)) != NULL)
        {
        printf("Serveur -  le resultat de la requete sql est :  %s \n", Tuple[1]); // censé donner carotes 
        }
        else printf("Serveur -  le resultat de la requete sql ne donne aucune reponse");*/
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



    /*while(1)
    {
        if(nbCaddie != 10)
        {
            printf("Serveur - Attente d'un nouuveau client\n");
            sService = Socket::Accept(sServeur,NULL);//Faire une boucle avec plusieurs thread pour accepter

            for(int i  = 0 ; i<10 ; i++)
            {
                if(threadCaddie[i] == 0)
                {
                    tabClientConnect[i].sService = sService;
                    if(pthread_create(&threadCaddie[i], NULL , (void*(*)(void*))FctCaddie,&tabClientConnect[i])!=0)
                    {
                        printf("Serveur - Probleme de creation de thread");
                    }
                    else
                    {
                        nbCaddie++;
                        printf("Serveur - Creation d un thread caddie\nServeur - Place restante : %d\n",10-nbCaddie);
                    }
                    i = 11;
                }
            }
        }
        else
        {
            printf("Serveur - Le nombre maximum de client connecte au serveur est atteint\n");
        }


    }*/



}

void*FctCaddie(void * )
{

    int sService;
    int result;
    bool retour;
    char charReceive[60];
    char reponse[60];
    while(1)
    {
        printf("\t[THREAD %ld] Attente socket...\n",pthread_self());
        // Attente d'une tâche
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
        while(retour == true)
        {
            if((result = Socket::Receive(sService, charReceive)) == -1)
            {
                printf("Mal passe\n");
                // printf("Thread %d - Erreur de receive\n",threadsService->indiceThread);
            }

            printf("Attente d'une requete :\n");
            printf("Reçu : %s\n",charReceive);
            retour = SMOP(charReceive,reponse, sService, connexion);
            printf("Retour client : %d\n",retour);

            if((Socket::Send(sService, reponse, sizeof(reponse))) != -1)
            {
                printf("Renvoyé %s\n",reponse);
            }
        }

        //Fin test
        // debut trait tache

    }
    //printf("Thread %d - Hello pret a repondre\n",threadsService->indiceThread);
        /* 
        //Test de Receive
        int result;
        char charReceive[60];
        char reponse[60];

        while (1)
        {
            if((result = Socket::Receive(sService, charReceive)) == -1)
            {
                printf("Mal passe\n");
               printf("Thread %d - Erreur de receive\n",threadsService->indiceThread);
            }
        
            else
            {
                sleep(5);
                printf("\n***********\n");
                printf("Thread %d - Taille trame lue : %d\n",threadsService->indiceThread,result);//Renvoie le nombre de carractére lue
                printf("Thread %d - Lue : %s\n",threadsService->indiceThread,charReceive);
                
                if(strcmp(charReceive,"DECONNECT") ==0 )
                {
                    close(threadsService->sService);
                    threadCaddie[threadsService->indiceThread] = 0;//Remettre à l indice du tableau de thread 0 pour dire que il est libre
                    nbCaddie--;
                    printf("Thread %d - Fin du thread\n",threadsService->indiceThread);
                    printf("***********\n");
                    pthread_exit(0);
                }
                else
                {
                    
                    SMOP(charReceive,reponse, sService, connexion);
                    printf("Reponse : %s\n",reponse);
                    
                }
                
                
            }
            
        }
        */
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

