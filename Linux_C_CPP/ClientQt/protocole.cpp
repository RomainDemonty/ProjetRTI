#include "protocole.h"
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <iostream>
#include <mysql.h>

//***** Etat du protocole : liste des clients loggés ****************
int clients[NB_MAX_CLIENTS];
int nbClients = 0;
int estPresentServeur(int socket);
void ajoute(int socket);
void retire(int socket);


//Variables
MYSQL_RES *resultat;
MYSQL_ROW  Tuple;
int qtedispo,newqte;
char chaine[200];

pthread_mutex_t mutexClients = PTHREAD_MUTEX_INITIALIZER;

//***** Parsing de la requete et creation de la reponse *************
bool SMOP(char* requete, char* reponse,int socket, MYSQL * con)
{
    // ***** Récupération nom de la requete *****************
    char *cas = strtok(requete,"#");
    // ***** LOGIN ******************************************
    if (strcmp(cas,"LOGIN") == 0) 
    {
        char user[30], password[30];
        bool newuser;
        strcpy(user,strtok(NULL,"#"));
        strcpy(password,strtok(NULL,"#"));
        newuser = atoi(strtok(NULL,"#"));
        if (estPresentServeur(socket) >= 0) // client déjà loggé
        {
            sprintf(reponse,"LOGIN#ko#Client déjà loggé !");
            return false;
        }
        else
        {
            sprintf(chaine,"SELECT * FROM clients WHERE username = '%s';",user);

            //printf("\nVoici la requete envoyée : %s\n", chaine);

            if (mysql_query(con, chaine) != 0)
            {
                strcpy(reponse,"LOGIN#ko#ERREUR SQL0#-1");
                return false;
            }
            else
            {
                //printf("OK je suis bien arrivé apres le query\n");
                resultat = mysql_store_result(con);
                //printf("OK je suis bien arrivé apres le result\n");
                if ((Tuple = mysql_fetch_row(resultat)) != NULL)
                {
                    printf("LOGIN - Client trouvé: %s\n",Tuple[1]);
                }
                //printf("OK je suis bien arrivé apres le row\n");
                if(newuser == true)
                {
                    //printf("OK je suis bien arrivé apres le new User\n");
                    if(Tuple  != NULL )
                    {
                        if(strcmp(Tuple[1],user));
                        strcpy(reponse,"LOGIN#ko#Nom deja utiliser");
                        return false;
                    }
                    else
                    {
                        //printf("OK je suis bien arrivé apres le Deja presentr\n");
                        sprintf(chaine, "INSERT INTO clients (username, password) VALUES ('%s', '%s')", user, password);
                        if (mysql_query(con, chaine) != 0)
                        {
                            strcpy(reponse,"LOGIN#ko#ERREUR SQL INSERTION#-1");
                            return false;
                        }
                        strcpy(reponse,"LOGIN#ok#Inscription reussie");
                        return true;
                    }
                }
                else
                {
                    if(strcmp(Tuple[1],user)!=0)
                    {
                        strcpy(reponse,"LOGIN#ko#Client Inconnu");
                        return false;
                    }
                    else
                    {
                        if(strcmp(Tuple[2],password)==0)
                        {
                            strcpy(reponse,"LOGIN#ok#Connexion reussie");
                            return true;
                        }
                        else
                        {
                            strcpy(reponse,"LOGIN#ko#Mot de passe incorect");
                            return false;
                        }
                    }
                }
            }
        }
    }
    // ***** LOGOUT *****************************************
    if (strcmp(cas,"LOGOUT") == 0)
    {
        retire(socket);
        sprintf(reponse,"LOGOUT#ok");
        return false;
    }
    // ***** OPER *******************************************
    if (strcmp(cas,"LOGOUT") != 0 && strcmp(cas,"LOGIN") != 0)
    {
        int id, quantitedem;

        if (estPresentServeur(socket) == -1)
        {
            strcpy(reponse,"OPER#ko#Client non loggé !");
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
                //Strtok
                id = atoi(strtok(NULL,"#"));
                quantitedem = atoi(strtok(NULL,"#"));

                /*Si article non trouvé, retour -1. Si 
                trouvé mais que stock insuffisant, 
                retour d’une quantité 0 → Si ok, le 
                stock est mis à jour en BD et le 
                contenu du caddie est mémorisé au 
                niveau du serveur → actuellement 
                aucune action sur tables factures et 
                ventes*/

                // TO DO

                // Acces BD , comme en php l'annee passee 
                sprintf(requete,"select * from UNIX_FINAL where id = %d", id);
                if (mysql_query(con, requete) != 0)
                {
                    strcpy(reponse,"ACHAT#ko#ERREUR_SQL#-1");
                }
                if((resultat = mysql_store_result(con)) == NULL)
                {
                    strcpy(reponse,"ACHAT#ko#ERREUR_SQL#-1");
                }
                if ((Tuple = mysql_fetch_row(resultat)) != NULL)
                {
                    qtedispo = atoi(Tuple[3]);
 
                    if (quantitedem > qtedispo)
                    {
                        strcpy(reponse,"ACHAT#ko#Stock_Insufisant#0");
                    }
                    else
                    {
                        //maj dans la bd
                        newqte = qtedispo - quantitedem;
                        sprintf(requete,"UPDATE UNIX_FINAL SET stock = %d where id = %d", newqte, id);
                        if (mysql_query(con, requete) != 0) //requete de mise a jour
                        {
                            strcpy(reponse,"ACHAT#ko#ERREUR_SQL#-1");
                        }
                        else
                        {
                            strcpy(reponse,"ACHAT#ok#Achat_fait#1");//Il faut dire au serveur que je le rajoute au panier du coup
                        }
                    }
                }
            }
            if(strcmp(cas,"CADDIE") == 0)//Ne pas faire car discuté avec le prof et inutile de demander tout le cadis a chaque fois juste s'occuper de un tuple à la fois
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
/*
bool SMOP_Login(const char* user,const char* password, const bool newuser , char *reponse)
{
    strcpy(reponse,"BienReçuConnection");
    //Demander a la bd si le mot de passe etc est correct
    if (strcmp(user,"wagner")==0 && strcmp(password,"abc123")==0) 
    {
        return true;
    }
    if (strcmp(user,"charlet")==0 && strcmp(password,"xyz456")==0) 
    {
        return true;
    }
   // TO DO Oui ou non, message (+ idClient) ou raison !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! Adapter les estpresent etc par des requètes sql

    int res;
    char resChar[10];
    fprintf(stderr,"(SERVEUR) Requete LOGIN reçue");  

    if(newuser == true)
    {
        if ((estPresent(user))>0)
        {    
            strcpy(reponse,"LOGIN#ko#Deja_present");
        }
        else
        {
            strcpy(reponse,"LOGIN#ok#Client_cree");
            ajouteClient(user ,password);
        }
    }
    else
    {
        if ((res = estPresent(user)>0))
        {
        
            if (verifieMotDePasse(estPresent(user), password )==1)
            {
                strcpy(reponse,"LOGIN#ok#Connexion_reussie#");
                sprintf(resChar, "%d",res);
                strcat(reponse,resChar);
            }
            else
            {
                strcpy(reponse,"LOGIN#ko#Mot_de_passe_incorect");
            }
        }
        else
        {
            strcpy(reponse,"LOGIN#ko#Client_inconnu");
        }

    }     
    return true;  

}
*/

//***** Gestion de l'état du protocole ******************************
int estPresentServeur(int socket)
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
    int pos = estPresentServeur(socket);
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