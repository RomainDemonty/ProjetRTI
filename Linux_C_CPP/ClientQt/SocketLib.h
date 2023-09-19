#ifndef TCP_H
#define TCP_H 
#include <stdio.h>
#include <iostream>
#include <string>
#include <unistd.h>
#include <stdlib.h>
#include <string.h> // pour memset
#include <sys/types.h>
#include <sys/socket.h>
#include <netdb.h>
#define TAILLE_MAX_DATA 1500

using namespace std; 
class Socket {

public:
    static int ServerSocket ( int sock);
    static int Accept(int ecoute ,char * ipclient ); 
    static int ClientSocket(char * ipServeur , int portServeur) ; 
    static int Send (int sSocket, char* data, int taille);
    static int Receive(int sSocket , char* data);
} ; 

#endif