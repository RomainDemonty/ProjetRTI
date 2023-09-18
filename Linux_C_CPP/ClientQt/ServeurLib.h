#ifndef TCP_H
#define TCP_H 
#include <stdio.h>
#include <stdlib.h>
#define TAILLE_MAX_DATA 1500

class Socket {

public:
    static int serverSocket ( int socket);
    static int accept(int ecoute ,char * ipclient ); 
    static int clientSocket(char * ipServeur , int portServeur) ; 
    static int send (int sSocket, char* data, int taille);
    static int receive(int sSocket , char* data);
} ; 

#endif