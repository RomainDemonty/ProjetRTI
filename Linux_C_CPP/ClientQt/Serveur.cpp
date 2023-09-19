 #include <stdio.h> 
#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#include <string.h>

#include "SocketLib.h"

 int main() 
 {  
    int sServeur , sService;
    

    sServeur = Socket::ServerSocket(1500);

    sService = Socket::Accept(sServeur,NULL);//Redemander quand même au prof si on peut mettre null


    //Test de send
    char charstr[10] = "Test12";
    int envoye = Socket::Send(sService ,  charstr , sizeof(charstr));
    if(envoye != -1)
    {
        printf("%d",envoye);
    }
    else
    {
        printf("Trame trop longue/mal passe");
    }
    //fin test de send

    //Test de Receive
    int result;
    char charReceive[10];

    if((result = Socket::Receive(sService, charReceive)) == -1)
    {
        printf("/nErreur de receive");
    }
    else
    {
        printf("/ntrame lue : %d",result);//Renvoie le nombre de trame lue
    }
    //Fin test de Receive

    pause();
}