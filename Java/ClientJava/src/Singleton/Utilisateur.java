package Singleton;

import Article.Article;

import java.util.ArrayList;

public class Utilisateur {
    private int idUtilisateur;
    public void setIdUtilisateur(int idUtil){this.idUtilisateur = idUtil;}
    public int getIdUtilisateur(){return idUtilisateur;}

    private int numArticle;
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
        return instance;}

}
