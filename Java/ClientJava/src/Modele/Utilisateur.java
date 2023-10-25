package Modele;

import java.util.ArrayList;

public class Utilisateur {
    private int idUtilisateur = 0;
    public void setIdUtilisateur(int idUtil){this.idUtilisateur = idUtil;}
    public int getIdUtilisateur(){return idUtilisateur;}

    private String requete;
    public void setRequete(String requete){this.requete = requete;}
    public String getRequete(){return requete;}

    private int numArticle = 1;
    public void setNumArticle(int numArt){this.numArticle = numArt;}
    public int getNumArticle() {return numArticle;}

    private static Utilisateur instance = new Utilisateur() ;

    private ArrayList<Article> monPanier;

    public ArrayList<Article> getMonPanier(){
        return monPanier;
    }
    public void addArticlePanier(Article A){
        monPanier.add(A);
    }
    public void removeArticlePanier(Article A){
        monPanier.remove(A);
    }

    public static Utilisateur getInstance() {
        if (instance==null) instance = new Utilisateur();
        return instance;
    }
    /////////////////////////////////////////////////////
    //////////////Fabrication des requetes//////////////
    ///////////////////////////////////////////////////
    private void consult(){
        requete = "CONSULT#" + numArticle;
        echange();
    }

    private void achat(){
        requete = "ACHAT#" ;
        echange();
    }

    private void cancell(){
        requete = "CANCELL#";
        echange();
    }

    private void cancellall(){
        requete = "CANCELLALL#";
        echange();
    }

    private void confirm(){
        requete = "CONFIRM#";
        echange();
    }

    private void login(){
        requete = "LOGIN#";
        echange();
    }

    private void logout(){
        requete = "LOGOUT#";
        echange();
    }

    /////////////////////////////////////////////////////
    ///////////////////Send et receive//////////////////
    ///////////////////////////////////////////////////
    private void connect(){

    }

    private void echange(){
        send();
        receive();
    }

    private void send(){

    }

    private void receive(){

    }

    /////////////////////////////////////////////////////
    ///////////////////////AUTRES///////////////////////
    ///////////////////////////////////////////////////

    public void precedent(){
        if(numArticle == 1)
        {
            numArticle = 21;
        }
        else
        {
            numArticle--;
        }
        consult();
    }

    public void suivant(){
        if(numArticle == 21)
        {
            numArticle = 1;
        }
        else
        {
            numArticle++;
        }
        consult();
    }

}
