#ifndef SMOP_H
#define SMOP_H
#define NB_MAX_CLIENTS 100

#include <mysql.h>

bool SMOP(char* requete, char* reponse,int socket, MYSQL * con);
bool SMOP_Login(const char* user,const char* password, const bool newuser , char* reponse);
//int SMOP_Operation(char op,int a,int b);
void SMOP_Close();

#endif