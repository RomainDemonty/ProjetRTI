
#include "SocketLib.h"

int Socket::ServerSocket ( int sock)//Demander si on peut faire un string a la place de int
{
    // appelé par le serveur 
    // fait un appel a socket() , 
    // cosntruit l'adresse reseua grace a getaddrinfo()
    // bind() pour lier les 2 


    int sServeur;
    printf("Serveur- id = %d\n",getpid());

    // Creation de la socket
    if ((sServeur = socket(AF_INET, SOCK_STREAM, 0)) == -1)
    {
        perror("Serveur - Erreur de socket()");
        exit(1);
    }
    printf("Serveur - socket creee = %d\n",sServeur);

    struct addrinfo *results;
    // Construction de l'adresse
    struct addrinfo hints;
    
    memset(&hints,0,sizeof(struct addrinfo));
    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_PASSIVE | AI_NUMERICSERV; // pour une connexion passive

    if (getaddrinfo(NULL,"1500",&hints,&results) != 0)
    {
        close(sServeur);
        exit(1);
    }

    char host[NI_MAXHOST];
    char port[NI_MAXSERV];
    struct addrinfo* info;
    getnameinfo(results->ai_addr,results->ai_addrlen,
    host,NI_MAXHOST,port,NI_MAXSERV,
    NI_NUMERICSERV | NI_NUMERICHOST);
    printf("Serveur - Mon Adresse IP: %s -- Mon Port: %s\n",host,port);

   

    if (bind(sServeur,results->ai_addr,results->ai_addrlen) < 0)
    {
        perror("Serveur - Erreur de bind()");
        exit(1);
    }
    freeaddrinfo(results);
    printf("Serveur - bind() reussi !\n");

    return sServeur ; 
}
int Socket::Accept(int ecoute ,char * ipclient )
{
    // prend en parametre le descripteur de socket 
    //fais appel a listen() 
    //fais appel a accept() 
    // peut recup l'adresse ip du client , place dans ipclient si celui ci est non null 
    //retourne la socket de service obtenue apres conenction avec un client

    if (listen(ecoute,10) == -1)//Faire attention au 10 juste pour le teste. Maxcon
    {
        perror("Serveur - Erreur de listen()");
        exit(1);
    }
    printf("Serveur - listen() reussi !\n");

    // Attente d'une connexion
    int sService;
    if ((sService = accept(ecoute,NULL,NULL)) == -1)
    {
        perror("Serveur - Erreur de accept()");
        exit(1);
    }
    printf("Serveur -accept() reussi !\n");
    printf("Serveur -socket de service = %d\n",sService);
    return sService ; 
}
int Socket::ClientSocket(char * ipServeur , int portServeur) 
{
    // appele par le processus client  
    // socket() 
    // construit l'addr reseau grace a getaddrinfo 
    // connect() au serveur 
    // retourne la socketservice qui va lui permetre de communiquer avec le serveur 


    int sClient;
    if ((sClient = socket(AF_INET, SOCK_STREAM, 0)) == -1)
    {
        perror("Client - Erreur de socket()");
        exit(1);
    }

    printf("Client - socket du client réusssis!\n");

    struct addrinfo hints;
    struct addrinfo *results;
    memset(&hints,0,sizeof(struct addrinfo));
    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_NUMERICSERV;
    if (getaddrinfo(ipServeur,"1500",&hints,&results) != 0)
            exit(1);
    // Demande de connexion
    printf("Client - dmd de connect()\n");
    //printf("addr = %s  , result = %d \n ",results->ai_addr,results->ai_addrlen);
    if (connect(sClient,results->ai_addr,results->ai_addrlen) == -1)
    {
        perror("Client - Erreur de connect()");
        exit(1);
    }
    printf("Client - connect() reussi !\n");
    return sClient ; 

}
int Socket::Send (int sSocket, char* data, int taille)
{
    //Test d'une autre manière
    /*
    int nbEcrits;
    // utilise par le client et le serveur
    // retorune le nb bytes envoyé pour tester les erreurs 
    if((nbEcrits = Send(sSocket,data,taille)) < 0)
    {
        perror("Erreur de Send");
        close(sSocket);
        exit(1);
    }
    printf("NbEcrits = %d\n",nbEcrits);
    printf("Ecrit = --%s--\n",data);

    return nbEcrits; //On peut changer par la taille envoyée
    */

    //Test de manière 2 mais pas optimal on préfére mettre la taille devant 
     if (taille > TAILLE_MAX_DATA)
    return -1;

    // Preparation de la charge utile
    char trame[TAILLE_MAX_DATA+2];
    memcpy(trame,data,taille);
    trame[taille] = '#';
    trame[taille+1] = ')';

    printf("%s",trame);
    // Ecriture sur la socket
    int test = write(sSocket,trame,taille+2)-2;
    printf("%d",test);
    return test;
    //Fin Test 2


    /*Test 3
    if (taille > TAILLE_MAX_DATA)
    return -1;



    Fin test 3*/


}
int Socket::Receive(int sSocket , char* data)
{
    // utilise par le client et le serveur 
    // retourne le nb de byte lu 
    bool fini = false;
    int nbLus, i = 0;
    char lu1,lu2;
    while(!fini)
    {
        if ((nbLus = read(sSocket,&lu1,1)) == -1)
        return -1;

        if (nbLus == 0) 
        return i; // connexion fermee par client
    
        if (lu1 == '#')
        {
            if ((nbLus = read(sSocket,&lu2,1)) == -1)
                return -1;
    
            if (nbLus == 0) 
                return i; // connexion fermee par client
    
            if (lu2 == ')') 
                fini = true;
            else
            {
                data[i] = lu1;
                data[i+1] = lu2;
                i += 2;
            }
        }
        else
        {
            data[i] = lu1;
            i++;
        }
    }
    return i;
}

