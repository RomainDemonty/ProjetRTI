
#include "ServeurLib.h"

int Socket::serverSocket ( int socket)
{
    // appelé par le serveur 
    // fait un appel a socket() , 
    // cosntruit l'adresse reseua grace a getaddrinfo()
    // bind() pour lier les 2 
    printf("hello world \n"); 
    return 0 ; 
}
int Socket::accept(int ecoute ,char * ipclient )
{
    // prend en parametre le descripteur de socket 
    //fais appel a listen() 
    //fais appel a accept() 
    // peut recup l'adresse ip du client , place dans ipclient si celui ci est non null 
    //retourne la socket de service obtenue apres conenction avec un client
    return 0 ; 
}
int Socket::clientSocket(char * ipServeur , int portServeur) 
{
    // appele par le processus client  
    // socket() 
    // construit l'addr reseau grace a getaddrinfo 
    // connect() au serveur 
    // retourne la socketservice qui va lui permetre de communiquer avec le serveur 
    return 0 ; 

}
int Socket::send (int sSocket, char* data, int taille)
{
    // utilise par le client et le serveur
    // retorune le nb bytes envoyé pour tester les erreurs 
    return 0 ; 
}
int Socket::receive(int sSocket , char* data)
{
    // utilise par le client et le serveur 
    // retourne le nb de byte lu 
    return 0 ; 
}

