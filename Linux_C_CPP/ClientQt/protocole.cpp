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

pthread_mutex_t mutexClients = PTHREAD_MUTEX_INITIALIZER;

//***** Parsing de la requete et creation de la reponse *************
bool SMOP(char* requete, char* reponse,int socket, MYSQL * con , ARTICLEPANIER *tabPanier)
{
    //Variables
    MYSQL_RES *resultat;
    MYSQL_ROW  Tuple;
    int qtedispo,newqte , j;
    char chaine[200];
    char usern[50], password[30];
    char cas[30];
    bool newuser;
    bool ok;
    int idClient = 0;
    char date[20];
    // ***** Récupération nom de la requete *****************
   // char *cas = strtok(requete,"#");
    strcpy(cas,strtok(requete,"#"));
    // ***** LOGIN ******************************************
    if(strcmp(cas,"LOGOUT") == 0)
    {
        for (j = 0; j < 20; j++)
        {
            //vider le caddy et mettre a jour dans la bd
            sprintf(requete,"select * from articles where id = %d", tabPanier[j].id);
            if (mysql_query(con, requete) != 0)
            {
                strcpy(reponse,"LOGOUT#ko#ERREUR_SQL#-1");
            }
            if((resultat = mysql_store_result(con)) == NULL)
            {
                strcpy(reponse,"LOGOUT#ko#ERREUR_SQL#-1");
            }
            if ((Tuple = mysql_fetch_row(resultat)) != NULL)
            {
                qtedispo = atoi(Tuple[3]);
    
                //maj dans la bd
                newqte = qtedispo + tabPanier[j].quantite;
                printf("Nouveau stock :%d\n", newqte);
                sprintf(requete,"UPDATE articles  SET stock = %d where id = %d", newqte, tabPanier[j].id);
                if (mysql_query(con, requete) != 0) //requete de mise a jour
                {
                    strcpy(reponse,"LOGOUT#ko#ERREUR_SQL#-1");
                }
                else
                {
                    tabPanier[j].id = 0;
                    tabPanier[j].prix = 0;
                    tabPanier[j].quantite = 0;
                }
            }
        }
        retire(socket);
        sprintf(reponse,"LOGOUT#ok");

        return true;
    }
    if (strcmp(cas,"LOGIN") == 0) 
    {
        strcpy(usern,strtok(NULL,"#"));
        strcpy(password,strtok(NULL,"#"));
        newuser = atoi(strtok(NULL,"#"));
        if (estPresentServeur(socket) >= 0) // client déjà loggé
        {
            sprintf(reponse,"LOGIN#ko#Client déjà loggé !");
            return true;
        }
        else
        {
            sprintf(chaine,"SELECT * FROM clients WHERE username = '%s';",usern);

            //printf("\nVoici la requete envoyée : %s\n", chaine);

            if (mysql_query(con, chaine) != 0)
            {
                strcpy(reponse,"LOGIN#ko#ERREUR SQL0#-1");
                return true;
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
                        if(strcmp(Tuple[1],usern));
                        strcpy(reponse,"LOGIN#ko#Nom deja utiliser");
                        return true;
                    }
                    else
                    {
                        //printf("OK je suis bien arrivé apres le Deja presentr\n");
                        sprintf(chaine, "INSERT INTO clients (username, password) VALUES ('%s', '%s')", usern, password);
                        if (mysql_query(con, chaine) != 0)
                        {
                            strcpy(reponse,"LOGIN#ko#ERREUR SQL INSERTION#-1");
                            return true;
                        }
                        sprintf(chaine,"SELECT * FROM clients WHERE username = '%s';",usern);//Recherche de l'identifiant du client enregistré
                        if (mysql_query(con, chaine) != 0)
                        {
                            strcpy(reponse,"LOGIN#ko#ERREUR SQL0#-1");
                            return true;
                        }
                        if((resultat = mysql_store_result(con)) == NULL)
                        {
                            strcpy(reponse,"LOGIN#ko#ERREUR_SQL#-1");
                        }
                        if ((Tuple = mysql_fetch_row(resultat)) != NULL)
                        {
                            sprintf(reponse,"LOGIN#ok#Inscription reussie#%d",atoi(Tuple[0]));
                        }
                        ajoute(socket);
                        return true;
                    }
                }
                else
                {
                    if(Tuple == NULL || strcmp(Tuple[1],usern)!=0)
                    {
                        strcpy(reponse,"LOGIN#ko#Client Inconnu");
                        return true;
                    }
                    else
                    {
                        if(strcmp(Tuple[2],password)==0)
                        {
                            sprintf(reponse,"LOGIN#ok#Connection reussie#%d",atoi(Tuple[0]));
                            ajoute(socket);
                            return true;
                        }
                        else
                        {
                            strcpy(reponse,"LOGIN#ko#Mot de passe incorect");
                            return true;
                        }
                    }
                }
            }
        }
    }
    else
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

                sprintf(requete,"select * from articles where id = %d", id);
                if (mysql_query(con, requete) != 0)
                {
                    strcpy(reponse,"CONSULT#ko#ERREUR_SQL#-1");
                }
                if((resultat = mysql_store_result(con)) == NULL)
                {
                    strcpy(reponse,"CONSULT#ko#ERREUR_SQL#-1");
                }
                if ((Tuple = mysql_fetch_row(resultat)) != NULL)
                {
                    sprintf(reponse,"CONSULT#ok#%d#%s#%d#%f#%s",atoi(Tuple[0]),Tuple[1],atoi(Tuple[3]),atof(Tuple[2]),Tuple[4]);//id,intitule,stock,prix,image
                }
                else
                {
                    sprintf(reponse,"CONSULT#ko#-1");
                }
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

                // Acces BD 
                sprintf(requete,"select * from articles where id = %d", id);
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
                        printf("Nouvelle quantite: %d\n",newqte);
                        sprintf(requete,"UPDATE articles  SET stock = %d where id = %d", newqte, id);
                        if (mysql_query(con, requete) != 0) //requete de mise a jour
                        {
                            strcpy(reponse,"ACHAT#ko#ERREUR_SQL#-1");
                        }
                        else
                        {
                            for (j = 0 ,ok = true; j< 20 && ok == true; j++)
                            {
                                if(tabPanier[j].id == 0 || tabPanier[j].id == id)
                                {
                                    ok = false;
                                    tabPanier[j].id = id;
                                    tabPanier[j].prix = atof(Tuple[2]);
                                    tabPanier[j].quantite = tabPanier[j].quantite + quantitedem;
                                }
                            }
                            
                            sprintf(reponse,"ACHAT#ok#%s#%f#1",Tuple[1],atof(Tuple[2]));
                        }
                    }
                }
            }
            /*
            if(strcmp(cas,"CADDIE") == 0)//Ne pas faire car discuté avec le prof et inutile de demander tout le cadis a chaque fois juste s'occuper de un tuple à la fois
            {                
                id = atoi(strtok(NULL,"#"));
                //Retourne l’entièreté du contenu du caddie au client
            }
            */
            if(strcmp(cas,"CANCEL") == 0)
            {                
                id = atoi(strtok(NULL,"#"));
                //Supprime un article du caddie et met à jour à la BD

                //vider le caddy et mettre a jour dans la bd
                sprintf(requete,"select * from articles where id = %d", id);
                if (mysql_query(con, requete) != 0)
                {
                    strcpy(reponse,"CANCEL#ko#ERREUR_SQL#-1");
                }
                if((resultat = mysql_store_result(con)) == NULL)
                {
                    strcpy(reponse,"CANCEL#ko#ERREUR_SQL#-1");
                }
                if ((Tuple = mysql_fetch_row(resultat)) != NULL)
                {
                    qtedispo = atoi(Tuple[3]);
            
                    //maj dans la bd
                    newqte = qtedispo + tabPanier[j].quantite;
                    printf("Nouveau stock :%d\n", newqte);
                    sprintf(requete,"UPDATE articles  SET stock = %d where id = %d", newqte,id);
                    if (mysql_query(con, requete) != 0) //requete de mise a jour
                    {
                        strcpy(reponse,"CANCEL#ko#ERREUR_SQL#-1");
                    }
                    else
                    {
                        for(j = 0 , ok = true; j < 20 && ok == true ; j++)
                        {
                            if(tabPanier[j].id == id)
                            {
                                ok = false;
                                tabPanier[j].id = 0;
                                tabPanier[j].prix = 0;
                                tabPanier[j].quantite = 0;
                                sprintf(reponse,"CANCEL#ok");
                            }
                        }
                    }
                }
            }
            if(strcmp(cas,"CANCELALL") == 0)
            {                
                //Supprime tous les articles du caddie et met à jour la BD
                                //Supprime un article du caddie et met à jour à la BD
                for(j = 0 , ok = true; j < 20 && ok == true ; j++)
                {
                    //vider le caddy et mettre a jour dans la bd
                    sprintf(requete,"select * from articles where id = %d", tabPanier[j].id);
                    if (mysql_query(con, requete) != 0)
                    {
                        strcpy(reponse,"CANCELALL#ko#ERREUR_SQL#-1");
                    }
                    if((resultat = mysql_store_result(con)) == NULL)
                    {
                        strcpy(reponse,"CANCELALL#ko#ERREUR_SQL#-1");
                    }
                    if ((Tuple = mysql_fetch_row(resultat)) != NULL)
                    {
                        qtedispo = atoi(Tuple[3]);
            
                        //maj dans la bd
                        newqte = qtedispo + tabPanier[j].quantite;
                        printf("Nouveau stock :%d\n", newqte);
                        sprintf(requete,"UPDATE articles  SET stock = %d where id = %d", newqte,tabPanier[j].id);
                        if (mysql_query(con, requete) != 0) //requete de mise a jour
                        {
                            strcpy(reponse,"CANCELALL#ko#ERREUR_SQL#-1");
                            ok = false;
                        }
                        else
                        {
                            tabPanier[j].id = 0;
                            tabPanier[j].prix = 0;
                            tabPanier[j].quantite = 0;
                            sprintf(reponse,"CANCELALL#ok");
                        }
                    }
                }
            }
            if(strcmp(cas,"CONFIRMER") == 0)
            {         
                idClient = atoi(strtok(NULL,"#"));  
                strcpy(date,"2023-10-08"); // Date
                bool paye= false; // Montant
                int idFact = 0;

                ok = true;
                sprintf(requete,"INSERT INTO factures ( idclient, date , paye) VALUES ( %d, '%s', %d);",idClient,date,paye );
                if (mysql_query(con, requete) != 0) 
                {
                    strcpy(reponse,"CONFIRMER#ko#ERREUR_SQL#-1#1");
                    ok = false;
                }  
                else
                {
                    sprintf(chaine, "SELECT MAX(idfacture) FROM factures;");
                    if (mysql_query(con, chaine) != 0)
                    {
                        strcpy(reponse,"CONFIRMER#ko#ERREUR_SQL#-1#2");
                        ok = false;
                    }
                    else
                    {
                        if((resultat = mysql_store_result(con)) == NULL)
                        {
                            strcpy(reponse,"CONFIRMER#ko#ERREUR_SQL#-1");
                        }
                        if ((Tuple = mysql_fetch_row(resultat)) != NULL)
                        {
                            idFact = atoi(Tuple[0]);
                        }
                    }
                }

                for(j = 0 ; j < 20 && ok == true ; j++)
                {
                    if(tabPanier[j].id != 0)
                    {
                        sprintf(requete,"INSERT INTO articlesachetes ( idarticle, prix, stock , idfacture) VALUES (%d, '%.2f', %d, %d);",tabPanier[j].id,tabPanier[j].prix,tabPanier[j].quantite,idFact);
                        if (mysql_query(con, requete) != 0) 
                        {
                            strcpy(reponse,"CONFIRMER#ko#ERREUR_SQL#-1#3");
                            ok = false;
                        }
                        else
                        {
                            tabPanier[j].id = 0;
                            tabPanier[j].prix = 0;
                            tabPanier[j].quantite = 0;
                            sprintf(reponse,"CONFIRMER#ok");
                        }
                    }
                }
            }
        }
    }
    return true;
 }

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