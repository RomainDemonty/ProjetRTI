#ifndef SMOP_H
#define SMOP_H
#define NB_MAX_CLIENTS 100

#include <mysql.h>

typedef struct
{
  int   id;
  float prix;
  int   quantite;  
} ARTICLEPANIER;

bool SMOP(char* requete, char* reponse,int socket, MYSQL * con, ARTICLEPANIER *tabPanier);
bool SMOP_Logout(char* reponse,int socket);
void SMOP_Close();

#endif