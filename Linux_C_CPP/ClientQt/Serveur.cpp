#include <stdio.h> 
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>

#include "SocketLib.h"

#define DECONNECT 0

 int main() 
 {  
    int sServeur , sService;
    

    sServeur = Socket::ServerSocket(1500);

    sService = Socket::Accept(sServeur,NULL);//Redemander quand même au prof si on peut mettre null

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
}

