#include "protocole.h"
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>

//***** Etat du protocole : liste des clients loggés ****************
int clients[NB_MAX_CLIENTS];
int nbClients = 0;
int estPresent(int socket);
void ajoute(int socket);
void retire(int socket);

pthread_mutex_t mutexClients = PTHREAD_MUTEX_INITIALIZER;

//***** Parsing de la requete et creation de la reponse *************
bool SMOP(char* requete, char* reponse,int socket)
{
    // ***** Récupération nom de la requete *****************
    char *cas = strtok(requete,"#");
    // ***** LOGIN ******************************************
    if (strcmp(cas,"LOGIN") == 0) 
    {
        char user[50], password[50];
        strcpy(user,strtok(NULL,"#"));
        strcpy(password,strtok(NULL,"#"));
        printf("\t[THREAD %p] LOGIN de %s\n",pthread_self(),user);
        if (estPresent(socket) >= 0) // client déjà loggé
        {
            sprintf(reponse,"LOGIN#ko#Client déjà loggé !");
            return false;
        }
        else
        {
            if (SMOP_Login(user,password))
            {
                sprintf(reponse,"LOGIN#ok");
                ajoute(socket);
            } 
            else
            {
                sprintf(reponse,"LOGIN#ko#Mauvais identifiants !");
                return false;
            }
        }
    }
    // ***** LOGOUT *****************************************
    if (strcmp(cas,"LOGOUT") == 0)
    {
        printf("\t[THREAD %p] LOGOUT\n",pthread_self());
        retire(socket);
        sprintf(reponse,"LOGOUT#ok");
        return false;
    }
    // ***** OPER *******************************************
    if (strcmp(cas,"LOGOUT") != 0 && strcmp(cas,"LOGIN") != 0)
    {
        int id, quantite;

        if (estPresent(socket) == -1)
        {
            sprintf(reponse,"OPER#ko#Client non loggé !");
        } 
        else
        {

            if(strcmp(cas,"CONSULT") == 0)
            {                
                id = atoi(strtok(NULL,"#"));
                //Consultation d’un article en BD → si article non trouvé, retour -1 au client
            }
            if(strcmp(cas,"ACHAT") == 0)
            {
                id = atoi(strtok(NULL,"#"));
                quantite = atoi(strtok(NULL,"#"));
                /*Si article non trouvé, retour -1. Si 
                trouvé mais que stock insuffisant, 
                retour d’une quantité 0 → Si ok, le 
                stock est mis à jour en BD et le 
                contenu du caddie est mémorisé au 
                niveau du serveur → actuellement 
                aucune action sur tables factures et 
                ventes*/
            }
            if(strcmp(cas,"CADDIE") == 0)
            {                
                id = atoi(strtok(NULL,"#"));
                //Retourne l’entièreté du contenu du caddie au client
            }
            if(strcmp(cas,"CANCEL") == 0)
            {                
                id = atoi(strtok(NULL,"#"));
                //Supprime un article du caddie et met à jour à la BD
            }
            if(strcmp(cas,"CANCELALL") == 0)
            {                
                //Supprime tous les articles du caddie et met à jour la BD
            }
            if(strcmp(cas,"CONFIRMER") == 0)
            {                
                //Création d’une facture et BD et ajout des éléments du caddie dans la BD
            }
        }
    }
    return true;
 }
//***** Traitement des requetes *************************************
bool SMOP_Login(const char* user,const char* password)
{
    if (strcmp(user,"wagner")==0 && strcmp(password,"abc123")==0) 
    {
        return true;
    }
    if (strcmp(user,"charlet")==0 && strcmp(password,"xyz456")==0) 
    {
        return true;
    }

    return false;
}
/*
int SMOP_Operation(char op,int a,int b)
{
    if (op == '+') return a+b;
    if (op == '-') return a-b;
    if (op == '*') return a*b;
    if (op == '/')
    {
        if (b == 0) 
        {
            throw 1;
        }
        return a/b;
    }
    return 0;
}
*/


//***** Gestion de l'état du protocole ******************************
int estPresent(int socket)
{
    int indice = -1;
    pthread_mutex_lock(&mutexClients);
    for(int i=0 ; i<nbClients ; i++)
    {
            if (clients[i] == socket) 
            { 
                indice = i; 
                break; 
            }
    }

    pthread_mutex_unlock(&mutexClients);
    return indice;
}

void ajoute(int socket)
{
    pthread_mutex_lock(&mutexClients);
    clients[nbClients] = socket;
    nbClients++;
    pthread_mutex_unlock(&mutexClients);
}

void retire(int socket)
{
    int pos = estPresent(socket);
    if(pos == -1)
    {
        return;
    } 
    pthread_mutex_lock(&mutexClients);
    for (int i=pos ; i<=nbClients-2 ; i++)
    {
        clients[i] = clients[i+1];
    }
    nbClients--;
    pthread_mutex_unlock(&mutexClients);
}

//***** Fin prématurée **********************************************
void SMOP_Close()
{
    pthread_mutex_lock(&mutexClients);
    for (int i=0 ; i<nbClients ; i++)
    {
        close(clients[i]);
    }
    pthread_mutex_unlock(&mutexClients);
}