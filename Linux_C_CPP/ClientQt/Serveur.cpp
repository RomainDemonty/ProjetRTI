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

    pause();
}