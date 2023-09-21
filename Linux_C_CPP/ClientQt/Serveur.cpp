#include <stdio.h> 
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>
#include <pthread.h>

#include "SocketLib.h"

struct  ts{
    int indiceThread;
    int sService;
};

void* FctCaddie(ts *threadsService);

pthread_t threadCaddie[10];

ts tabClientConnect [10];

int nbCaddie = 0;

 int main() 
 {  
    int sServeur , sService;
    

    sServeur = Socket::ServerSocket(1500);

    for(int i  = 0 ; i<10 ; i++)
    {
        threadCaddie[i] = 0;
        tabClientConnect [i].indiceThread = i;
    }


    while(1)
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




        /*
        //Test de Receive
        int result;
        char charReceive[15];
        while (1)
        {
            if((result = Socket::Receive(sService, charReceive)) == -1)
            {
                printf("Erreur de receive\n");
            }
            else
            {
                printf("Taille trame lue : %d\n",result);//Renvoie le nombre de carractére lue
                printf("Lue : %s\n",charReceive);
            }
            //Fin test de Receive

            if(strcmp(charReceive,"DECONNECT") ==0 )
            {
                close(sService);
                close(sServeur);
                printf("\nFin du serveur\n");
                exit(0);
            }

        }
        */
    }
}

void*FctCaddie(ts *threadsService)
{
    printf("Thread %d - Hello pret a repondre\n",threadsService->indiceThread);
            
        //Test de Receive
        int result;
        char charReceive[15];
        while (1)
        {
            if((result = Socket::Receive(threadsService->sService, charReceive)) == -1)
            {
                printf("Thread %d - Erreur de receive\n",threadsService->indiceThread);
            }
            else
            {
                printf("\n***********\n");
                printf("Thread %d - Taille trame lue : %d\n",threadsService->indiceThread,result);//Renvoie le nombre de carractére lue
                printf("Thread %d - Lue : %s\n",threadsService->indiceThread,charReceive);
                if(strcmp(charReceive,"DECONNECT") ==0 )
                {
                    sleep(5);
                    close(threadsService->sService);
                    threadCaddie[threadsService->indiceThread] = 0;//Remettre à l indice du tableau de thread 0 pour dire que il est libre
                    nbCaddie--;
                    printf("Thread %d - Fin du thread\n",threadsService->indiceThread);
                    printf("***********\n");
                    pthread_exit(0);
                }
                else
                {
                    printf("***********\n");
                }
                
            }
        }
    return 0;
}

