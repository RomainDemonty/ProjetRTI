#include "protocole.h"
#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <pthread.h>
#include <mysql.h>

//***** Etat du protocole : liste des clients loggés ****************
int clients[NB_MAX_CLIENTS];
int nbClients = 0;
int estPresent(int socket);
void ajoute(int socket);
void retire(int socket);

pthread_mutex_t mutexClients = PTHREAD_MUTEX_INITIALIZER;

//***** Parsing de la requete et creation de la reponse *************
bool SMOP(char* requete, char* reponse,int socket, MYSQL * con)
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

                // TO DO

                      // Acces BD , comme en php l'annee passee 
                      sprintf(requete,"select * from UNIX_FINAL where id = %d", id);
                      if (mysql_query(con, requete) != 0)
                      {
                        fprintf (stderr, "Erreur de Mysql-query");
                      }
                      if((resultat = mysql_store_result(connexion)) == NULL)
                      {
                        fprintf (stderr, "Erreur de mysql store");
                      }
                      if ((Tuple = mysql_fetch_row(resultat)) != NULL)
                      {
                            
                        qtedispo = atoi(Tuple[3]);
                        qtedemandee = atoi(m.data2);//conversion pour pouvoir faire les calcus 
                        reponse.type = m.expediteur; 
                        reponse.expediteur = getpid();
                        reponse.requete = ACHAT;
                        reponse.data1 = atoi(Tuple[0]);
                        strcpy(reponse.data2, Tuple[1]);
                        strcpy(reponse.data4, Tuple[4]);

                        if (qtedemandee > qtedispo)
                        {
                         sprintf(reponse.data3,"0"); // condition donne par le prof , on renvoi 0 quand pas possible 
                        }
                        else
                        {
                          //maj
                          newqte = qtedispo - qtedemandee;

                          sprintf(reponse.data3,  m.data2);//qte
                          sprintf(requete,"UPDATE UNIX_FINAL SET stock = %d where id = %d", newqte, reponse.data1);
                          if (mysql_query(connexion, requete) != 0) //requete de mise a jour
                          {
                            fprintf (stderr, "Erreur de Mysql-query");
                          }

                        }


                        // Finalisation et envoi de la reponse
                        if(msgsnd(idQ,&reponse,sizeof(MESSAGE)-sizeof(long),0) == -1)
                        {
                          perror("(AccesBD) Erreur de msgsnd");
                          msgctl(idQ,IPC_RMID,NULL);
                          exit(1);
                        }

                      }
                      // Finalisation et envoi de la reponse
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
    //Demander a la bd si le mot de passe etc est correct
    /*
    if (strcmp(user,"wagner")==0 && strcmp(password,"abc123")==0) 
    {
        return true;
    }
    if (strcmp(user,"charlet")==0 && strcmp(password,"xyz456")==0) 
    {
        return true;
    }
    */

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