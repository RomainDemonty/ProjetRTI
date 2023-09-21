#include <stdio.h> 
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>
#include <pthread.h>

#include "SocketLib.h"

void* FctCaddie(void*);

pthread_t threadCaddie[10];

int nbCaddie = 0;

 int main() 
 {  
    int sServeur , sService;
    

    sServeur = Socket::ServerSocket(1500);

    for(int i  = 0 ; i<10 ; i++)
    {
        threadCaddie[i] = 0;
    }


    while(1)
    {
        if(nbCaddie != 10)
        {
            printf("Attente d'un nouuveau client\n");
            sService = Socket::Accept(sServeur,NULL);//Faire une boucle avec plusieurs thread pour accepter

            for(int i  = 0 ; i<10 ; i++)
            {
                if(threadCaddie[i] == 0)
                {
                    
                    pthread_create(&threadCaddie[i], NULL , (void*(*)(void*))FctCaddie,NULL);
                    i = 11;
                }
            }
            nbCaddie++;
            printf("Nombre de client connecte: %d\n",nbCaddie);
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

void*FctCaddie(void *)
{
    printf("Hello ceci est un caddie");

    pause();

    nbCaddie--;
    return 0;
}

