
#include "SocketLib.h"

int Socket::ServerSocket ( int sock)//Demander si on peut faire un string a la place de int
{
    // appelé par le serveur 
    // fait un appel a socket() , 
    // cosntruit l'adresse reseua grace a getaddrinfo()
    // bind() pour lier les 2 


    int sServeur;
    printf("pid = %d\n",getpid());

    // Creation de la socket
    if ((sServeur = socket(AF_INET, SOCK_STREAM, 0)) == -1)
    {
        perror("Erreur de socket()");
        exit(1);
    }
    printf("socket creee = %d\n",sServeur);

    // Construction de l'adresse
    struct addrinfo hints;
    struct addrinfo *results;
    memset(&hints,0,sizeof(struct addrinfo));
    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_PASSIVE | AI_NUMERICSERV; // pour une connexion passive

    if (getaddrinfo(NULL,"1500",&hints,&results) != 0)
    {
        close(sServeur);
        exit(1);
    }

    if (bind(sServeur,results->ai_addr,results->ai_addrlen) < 0)
    {
        perror("Erreur de bind()");
        exit(1);
    }
    freeaddrinfo(results);
    printf("bind() reussi !\n");

    if (listen(sServeur,10) == -1)//Faire attention au 10 juste pour le teste. Maxcon
    {
        perror("Erreur de listen()");
        exit(1);
    }
    printf("listen() reussi !\n");

    return sServeur ; 
}
int Socket::Accept(int ecoute ,char * ipclient )
{
    // prend en parametre le descripteur de socket 
    //fais appel a listen() 
    //fais appel a accept() 
    // peut recup l'adresse ip du client , place dans ipclient si celui ci est non null 
    //retourne la socket de service obtenue apres conenction avec un client

    // Attente d'une connexion
    int sService;
    if ((sService = accept(ecoute,NULL,NULL)) == -1)
    {
        perror("Erreur de accept()");
        exit(1);
    }
    printf("accept() reussi !");
    printf("socket de service = %d\n",sService);
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
        perror("Erreur de socket()");
        exit(1);
    }

    printf("socket du client réusssis!\n");

    struct addrinfo hints;
    struct addrinfo *results;
    memset(&hints,0,sizeof(struct addrinfo));
    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_NUMERICSERV;
    if (getaddrinfo(NULL,"1500",&hints,&results) != 0)
    exit(1);
    // Demande de connexion
    if (connect(sClient,results->ai_addr,results->ai_addrlen) == -1)
    {
        perror("Erreur de connect()");
        exit(1);
    }
    printf("connect() reussi !\n");
    return 0 ; 

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

     if (taille > TAILLE_MAX_DATA)
    return -1;

    // Preparation de la charge utile
    char trame[TAILLE_MAX_DATA+2];
    memcpy(trame,data,taille);
    trame[taille] = '#';
    trame[taille+1] = ')';

    // Ecriture sur la socket
    return write(sSocket,trame,taille+2)-2;
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

