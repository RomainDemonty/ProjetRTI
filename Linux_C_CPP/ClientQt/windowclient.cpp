#include "windowclient.h"
#include "ui_windowclient.h"
#include <QMessageBox>
#include <string>
#include "SocketLib.h" //Rajouter la librérie car aussi non on peut pas se connecter aux socket

using namespace std;

extern WindowClient *w;

#define REPERTOIRE_IMAGES "images/"

int sService, numarticle = 1;
bool logged=0;
char nomutilisateur[30];
char mdp[30];

typedef struct
{
  int   id;
  char  intitule[20];
  float prix;
  int   stock;  
  char  image[20];
} ARTICLE;

typedef struct
{
  int   id;
  float prix;
  int   quantite;  
} ARTICLEPANIER;

int stockglob;

ARTICLEPANIER tabPanier[20];

void Echange(char* requete, char* reponse);

//void Echange(char* requete, char* reponse) ; 

WindowClient::WindowClient(QWidget *parent) : QMainWindow(parent), ui(new Ui::WindowClient)
{
    ui->setupUi(this);

    // Configuration de la table du panier (ne pas modifer)
    ui->tableWidgetPanier->setColumnCount(3);
    ui->tableWidgetPanier->setRowCount(0);
    QStringList labelsTablePanier;
    labelsTablePanier << "Article" << "Prix à l'unité" << "Quantité";
    ui->tableWidgetPanier->setHorizontalHeaderLabels(labelsTablePanier);
    ui->tableWidgetPanier->setSelectionMode(QAbstractItemView::SingleSelection);
    ui->tableWidgetPanier->setSelectionBehavior(QAbstractItemView::SelectRows);
    ui->tableWidgetPanier->horizontalHeader()->setVisible(true);
    ui->tableWidgetPanier->horizontalHeader()->setDefaultSectionSize(160);
    ui->tableWidgetPanier->horizontalHeader()->setStretchLastSection(true);
    ui->tableWidgetPanier->verticalHeader()->setVisible(false);
    ui->tableWidgetPanier->horizontalHeader()->setStyleSheet("background-color: lightyellow");

    ui->pushButtonPayer->setText("Confirmer achat");
    setPublicite("!!! Bienvenue sur le Maraicher en ligne !!!");

    // Exemples à supprimer
    //setArticle("pommes",5.53,18,"pommes.jpg");
    //ajouteArticleTablePanier("cerises",8.96,2);

    // doit se connecter a la socket pour permetre d'echanger
    sService = Socket::ClientSocket(NULL , 1600); 

    for (int i = 0; i < 20; i++)
    {
      tabPanier[i].id = 0;
      tabPanier[i].prix = 0;
      tabPanier[i].quantite = 0;
    }

}

WindowClient::~WindowClient()
{
    delete ui;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions utiles : ne pas modifier /////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////

void WindowClient::setNom(const char* Text)
{
  if (strlen(Text) == 0 )
  {
    ui->lineEditNom->clear();
    return;
  }
  ui->lineEditNom->setText(Text);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
const char* WindowClient::getNom()
{
  strcpy(nom,ui->lineEditNom->text().toStdString().c_str());
  return nom;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setMotDePasse(const char* Text)
{
  if (strlen(Text) == 0 )
  {
    ui->lineEditMotDePasse->clear();
    return;
  }
  ui->lineEditMotDePasse->setText(Text);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
const char* WindowClient::getMotDePasse()
{
  strcpy(motDePasse,ui->lineEditMotDePasse->text().toStdString().c_str());
  return motDePasse;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setPublicite(const char* Text)
{
  if (strlen(Text) == 0 )
  {
    ui->lineEditPublicite->clear();
    return;
  }
  ui->lineEditPublicite->setText(Text);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setImage(const char* image)
{
  // Met à jour l'image
  char cheminComplet[80];
  sprintf(cheminComplet,"%s%s",REPERTOIRE_IMAGES,image);
  QLabel* label = new QLabel();
  label->setSizePolicy(QSizePolicy::Ignored, QSizePolicy::Ignored);
  label->setScaledContents(true);
  QPixmap *pixmap_img = new QPixmap(cheminComplet);
  label->setPixmap(*pixmap_img);
  label->resize(label->pixmap()->size());
  ui->scrollArea->setWidget(label);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
int WindowClient::isNouveauClientChecked()
{
  if (ui->checkBoxNouveauClient->isChecked()) return 1;
  return 0;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setArticle(const char* intitule,int stock,float prix,const char* image)
{
  ui->lineEditArticle->setText(intitule);
  if (prix >= 0.0)
  {
    char Prix[20];
    sprintf(Prix,"%.2f",prix);
    ui->lineEditPrixUnitaire->setText(Prix);
  }
  else ui->lineEditPrixUnitaire->clear();
  if (stock >= 0)
  {
    char Stock[20];
    sprintf(Stock,"%d",stock);
    ui->lineEditStock->setText(Stock);
  }
  else ui->lineEditStock->clear();
  setImage(image);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
int WindowClient::getQuantite()
{
  return ui->spinBoxQuantite->value();
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::setTotal(float total)
{
  char Total[6];
  if(total >= 0.0){/* code */
    sprintf(Total,"%.2f",total);
    ui->lineEditTotal->setText(Total);
  }
  else 
  {
    ui->lineEditTotal->clear();
  }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::loginOK()
{
  ui->pushButtonLogin->setEnabled(false);
  ui->pushButtonLogout->setEnabled(true);
  ui->lineEditNom->setReadOnly(true);
  ui->lineEditMotDePasse->setReadOnly(true);
  ui->checkBoxNouveauClient->setEnabled(false);

  ui->spinBoxQuantite->setEnabled(true);
  ui->pushButtonPrecedent->setEnabled(true);
  ui->pushButtonSuivant->setEnabled(true);
  ui->pushButtonAcheter->setEnabled(true);
  ui->pushButtonSupprimer->setEnabled(true);
  ui->pushButtonViderPanier->setEnabled(true);
  ui->pushButtonPayer->setEnabled(true);

}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::logoutOK()
{
  ui->pushButtonLogin->setEnabled(true);
  ui->pushButtonLogout->setEnabled(false);
  ui->lineEditNom->setReadOnly(false);
  ui->lineEditMotDePasse->setReadOnly(false);
  ui->checkBoxNouveauClient->setEnabled(true);

  ui->spinBoxQuantite->setEnabled(false);
  ui->pushButtonPrecedent->setEnabled(false);
  ui->pushButtonSuivant->setEnabled(false);
  ui->pushButtonAcheter->setEnabled(false);
  ui->pushButtonSupprimer->setEnabled(false);
  ui->pushButtonViderPanier->setEnabled(false);
  ui->pushButtonPayer->setEnabled(false);

  setNom("");
  setMotDePasse("");
  ui->checkBoxNouveauClient->setCheckState(Qt::CheckState::Unchecked);

  setArticle("",-1.0,-1,"");

  w->videTablePanier();
  w->setTotal(-1.0);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions utiles Table du panier (ne pas modifier) /////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::ajouteArticleTablePanier(const char* article,float prix,int quantite)
{
    char Prix[20],Quantite[20];

    sprintf(Prix,"%.2f",prix);
    sprintf(Quantite,"%d",quantite);

    // Ajout possible
    int nbLignes = ui->tableWidgetPanier->rowCount();
    nbLignes++;
    ui->tableWidgetPanier->setRowCount(nbLignes);
    ui->tableWidgetPanier->setRowHeight(nbLignes-1,10);

    QTableWidgetItem *item = new QTableWidgetItem;
    item->setFlags(Qt::ItemIsSelectable|Qt::ItemIsEnabled);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(article);
    ui->tableWidgetPanier->setItem(nbLignes-1,0,item);

    item = new QTableWidgetItem;
    item->setFlags(Qt::ItemIsSelectable|Qt::ItemIsEnabled);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(Prix);
    ui->tableWidgetPanier->setItem(nbLignes-1,1,item);

    item = new QTableWidgetItem;
    item->setFlags(Qt::ItemIsSelectable|Qt::ItemIsEnabled);
    item->setTextAlignment(Qt::AlignCenter);
    item->setText(Quantite);
    ui->tableWidgetPanier->setItem(nbLignes-1,2,item);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::videTablePanier()
{
    ui->tableWidgetPanier->setRowCount(0);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
int WindowClient::getIndiceArticleSelectionne()
{
    QModelIndexList liste = ui->tableWidgetPanier->selectionModel()->selectedRows();
    if (liste.size() == 0) return -1;
    QModelIndex index = liste.at(0);
    int indice = index.row();
    return indice;
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions permettant d'afficher des boites de dialogue (ne pas modifier ////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::dialogueMessage(const char* titre,const char* message)
{
   QMessageBox::information(this,titre,message);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::dialogueErreur(const char* titre,const char* message)
{
   QMessageBox::critical(this,titre,message);
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////// CLIC SUR LA CROIX DE LA FENETRE /////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::closeEvent(QCloseEvent *event)
{
  char messageRecu[1400];
  char messageEnvoye[1400];
  char tampon[50];
  if (logged==1)
  {
    on_pushButtonLogout_clicked();
  }

  
    strcpy(messageEnvoye, "");
    strcpy(messageEnvoye, "LOGOUT#test");
    Echange(messageEnvoye, messageRecu);

    strcpy(tampon,strtok(messageRecu,"#"));
    strcpy(tampon,strtok(NULL,"#"));
    if(strcmp(tampon,"ok")==0)
    {
      videTablePanier();
      logoutOK();
      logged = false;
      numarticle = 1;

      for (int i = 0; i < 20; i++)
      {
        tabPanier[i].id = 0;
        tabPanier[i].prix = 0;
        tabPanier[i].quantite = 0;
      }
      setTotal(0);
    }
    else
    {
      printf("Aie erreur logout\n");
    }

  
  
  exit(0);
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////
///// Fonctions clics sur les boutons ////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonLogin_clicked()
{
  char messageRecu[1400];
  char messageEnvoye[1400];
  char tampon[50];
    //on dmd au serv si on peut se connecter 
    // gerer l'erreur 
    // doit faire en sorte de mettre logged=1 ; 
    // appel a loginok  
    /*To do - envoyer une requète de login aprés avoir vérifié si logged*/
    ARTICLE articletampon;
    sprintf(messageEnvoye, "LOGIN#%s#%s#", getNom(), getMotDePasse());

    if (isNouveauClientChecked())
    {
      strcat(messageEnvoye,"1");
    }
    else{
      strcat(messageEnvoye,"0");
    }

    Echange(messageEnvoye, messageRecu);

    strcpy(tampon,strtok(messageRecu,"#"));
    strcpy(tampon,strtok(NULL,"#"));
    if(strcmp(tampon,"ok") == 0)
    {
      logged =1;
      if(strcmp(tampon,"Inscription_reussie") == 0)
      {
          setPublicite("Inscription reussie bienvenue;)");
      }
      else
      {
        setPublicite("Vous êtes bien connecté ;)");
      }

      //Afficher le premier article
      strcpy(messageEnvoye,"");
      sprintf(messageEnvoye, "CONSULT#%d",numarticle);

      Echange(messageEnvoye, messageRecu);

      strcpy(tampon,strtok(messageRecu,"#"));
      strcpy(tampon,strtok(NULL,"#"));
      
      articletampon.id = atof(strtok(NULL,"#"));
      strcpy(articletampon.intitule,strtok(NULL,"#"));
      articletampon.stock = atoi(strtok(NULL,"#"));
      articletampon.prix = atof(strtok(NULL,"#"));
      strcpy(articletampon.image,strtok(NULL,"#"));

      if(strcmp(tampon,"ok") == 0 )
      {
        setArticle(articletampon.intitule,articletampon.stock,articletampon.prix ,articletampon.image);//(const char* intitule,int stock,float prix,const char* image
      }
      else
      {
        setPublicite("Problème suivant :(");
      }
      loginOK();
    }
    else
    {
      strcpy(tampon,strtok(NULL,"#"));
      setPublicite(tampon);
    }
    return ;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonLogout_clicked()
{
  char messageRecu[1400];
  char messageEnvoye[1400];
  char tampon[50];
    //on dmd au serv si on peut se deconnecter 
    // gerer l'erreur 
    //logged = 0 a la fin . 
    //appel a logout ok 
    strcpy(messageEnvoye, "");
    strcpy(messageEnvoye, "LOGOUT#test");
    Echange(messageEnvoye, messageRecu);

    strcpy(tampon,strtok(messageRecu,"#"));
    strcpy(tampon,strtok(NULL,"#"));
    if(strcmp(tampon,"ok")==0)
    {
      videTablePanier();
      logoutOK();
      logged = false;
      numarticle = 1;

      for (int i = 0; i < 20; i++)
      {
        tabPanier[i].id = 0;
        tabPanier[i].prix = 0;
        tabPanier[i].quantite = 0;
      }
      setTotal(0);
    }
    else
    {
      printf("Aie erreur logout\n");
    }
}




/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonSuivant_clicked()
{
  char messageRecu[1400];
  char messageEnvoye[1400];
  char tampon[50];
  // envois d'une trame au serveur en demandant l'artcileencours+1
  //pas oublier de modif cet artcicle en cours var global
  ARTICLE articletampon;

  if(numarticle != 21)
  {
    numarticle ++;
  }
  else
  {
    numarticle = 1;
  }
  sprintf(messageEnvoye, "CONSULT#%d",numarticle);

  Echange(messageEnvoye, messageRecu);

  strcpy(tampon,strtok(messageRecu,"#"));
  strcpy(tampon,strtok(NULL,"#"));
  
  articletampon.id = atof(strtok(NULL,"#"));
  strcpy(articletampon.intitule,strtok(NULL,"#"));
  articletampon.stock = atoi(strtok(NULL,"#"));
  stockglob = articletampon.stock;
  articletampon.prix = atof(strtok(NULL,"#"));
  strcpy(articletampon.image,strtok(NULL,"#"));

  if(strcmp(tampon,"ok") == 0 )
  {
    setArticle(articletampon.intitule,articletampon.stock,articletampon.prix ,articletampon.image);//(const char* intitule,int stock,float prix,const char* image
  }
  else
  {
    setPublicite("Problème suivant :(");
  }
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonPrecedent_clicked()
{
  char messageRecu[1400];
  char messageEnvoye[1400];
  char tampon[50];
  ARTICLE articletampon;

  if(numarticle != 1)
  {
    numarticle --;
  }
  else
  {
    numarticle = 21;
  }
  sprintf(messageEnvoye, "CONSULT#%d",numarticle);

  Echange(messageEnvoye, messageRecu);

  strcpy(tampon,strtok(messageRecu,"#"));
  strcpy(tampon,strtok(NULL,"#"));
  
  articletampon.id = atof(strtok(NULL,"#"));
  strcpy(articletampon.intitule,strtok(NULL,"#"));
  articletampon.stock = atoi(strtok(NULL,"#"));
  articletampon.prix = atof(strtok(NULL,"#"));
  strcpy(articletampon.image,strtok(NULL,"#"));

  if(strcmp(tampon,"ok") == 0 )
  {
    setArticle(articletampon.intitule,articletampon.stock,articletampon.prix ,articletampon.image);//(const char* intitule,int stock,float prix,const char* image
  }
  else
  {
    setPublicite("Problème précédent :(");
  }

  return ;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonAcheter_clicked()
{
  // on verif la quantite ds la boite texte
  // on dit au serveur que on achete
  // on gere la reponse du serv 
  // supprimer de la base de donnée 
  char messageRecu[1400];
  char messageEnvoye[1400];
  char tampon[50];
  float prix, total;
  int quantite = getQuantite(), j;
  bool ok;
  char Stock[20];

  sprintf(messageEnvoye, "ACHAT#%d#%d",numarticle,quantite);
  Echange(messageEnvoye, messageRecu);

  strcpy(tampon,strtok(messageRecu,"#"));
  strcpy(tampon,strtok(NULL,"#"));

  if(strcmp(tampon,"ok") == 0 )
  {
    strcpy(tampon,strtok(NULL,"#"));
    prix = atof(strtok(NULL,"#"));
    ajouteArticleTablePanier(tampon, prix, quantite);

    stockglob = stockglob - quantite;
    sprintf(Stock,"%d",stockglob);
    ui->lineEditStock->setText(Stock);

    for (j = 0 ,ok = true; j< 20 && ok == true; j++)
    {
      if(tabPanier[j].id == 0 || tabPanier[j].id == numarticle)
      {
        ok = false;
        tabPanier[j].id = numarticle;
        tabPanier[j].prix = prix;
        tabPanier[j].quantite = tabPanier[j].quantite + quantite;
        printf("id = %d  -  prix = %f - qt = %d\n",tabPanier[j].id,tabPanier[j].prix,tabPanier[j].quantite);
      }
    }

    for (j = 0 ; j< 20; j++)
    {
       total = total + tabPanier[j].prix*tabPanier[j].quantite;
    }

    setTotal(total);
  }
  else
  {
    strcpy(tampon,strtok(NULL,"#"));
    setPublicite(tampon);
  }
  

}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonSupprimer_clicked()
{
  // verif si indice existe 
    // communiquer avec le caddie/panier  , 
    // communiquer avce le serveur 
    
  // rajouter dans la base de donnee les elts du caddie 
    }

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonViderPanier_clicked()
{
  // rajouter dans la base de donnee les elts du caddie 
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////
void WindowClient::on_pushButtonPayer_clicked()
{
    // vide le panier 
    //reset le prix a 0 
}


/////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
// fonction pour communiquer avec le serv. 
void Echange(char* requete, char* reponse)
{
  int nbEcrits, nbLus;
  // ***** Envoi de la requete ****************************
  printf("Envoye : %s\n",requete);
  if ((nbEcrits = Socket::Send(sService,requete,strlen(requete))) == -1)
  {
    perror("Erreur de Send");
    close(sService);
    exit(1);
  }
  // ***** Attente de la reponse **************************
  if ((nbLus = Socket::Receive(sService,reponse)) < 0)
  {
    perror("Erreur de Receive");
    close(sService);
    exit(1);
  }
  printf("Reçu : %s\n",reponse);
  if (nbLus == 0)
  {
    printf("Serveur arrete, pas de reponse reçue...\n");
    close(sService);
    exit(1);
  }
  reponse[nbLus] = 0;
}
