#ifndef SMOP_H
#define SMOP_H
#define NB_MAX_CLIENTS 100

#include <mysql.h>

bool SMOP(char* requete, char* reponse,int socket, MYSQL * con);
bool SMOP_Logout(int socket, char* reponse);
void SMOP_Close();

#endif