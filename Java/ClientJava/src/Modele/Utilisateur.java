package Modele;

import java.util.ArrayList;
import java.io.*;
import java.net.*;

public class Utilisateur {

    public Article articleSelect = new Article();
    private Socket cSocket;
    private DataOutputStream dos;
    private DataInputStream dis;

    private String resultat;

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
    public void consult() throws IOException {
        requete = "CONSULT#" + numArticle;
        echange(requete);

        String[] mots = resultat.split("#");
        System.out.println(mots[1]);
        if(mots[1].equals("ok"))
        {
            articleSelect.setidAliment(Integer.parseInt(mots[2]));
            articleSelect.setintitule(mots[3]);
            articleSelect.setquantite(Integer.parseInt(mots[4]));
            articleSelect.setprix(Float.parseFloat(mots[5]));
            articleSelect.setAdrImage(mots[6]);
            System.out.println( articleSelect);
        }
        else
        {
            System.out.println("Erreur de consult");
        }
    }

    private void achat() throws IOException {
        requete = "ACHAT#" ;
        echange(requete);
    }

    private void cancell() throws IOException {
        requete = "CANCELL#";
        echange(requete);
    }

    private void cancellall() throws IOException {
        requete = "CANCELLALL#";
        echange(requete);
    }

    private void confirm() throws IOException {
        requete = "CONFIRM#";
        echange(requete);
    }

    public void login() throws IOException {
        requete = "LOGIN#romain#romain123#0";
        echange(requete);
    }

    public void logout() throws IOException {
        //requete = "LOGOUT#";
        //echange(requete);
        dos.close();
        dis.close();
        cSocket.close();
    }

    /////////////////////////////////////////////////////
    ///////////////////Send et receive//////////////////
    ///////////////////////////////////////////////////
    public void connect() throws IOException {
        //ipServeur = getcongfig()
        String ipServeur = "192.168.1.122";
        int socket = 1500;
        cSocket = new Socket(ipServeur,socket);

        //Création des flux d'entrée et de sortie
        dos = new DataOutputStream(cSocket.getOutputStream());
        dis = new DataInputStream(cSocket.getInputStream());
    }

    private void echange(String requete) throws IOException {
        resultat = "";
        send(requete);
        resultat = receive();
        System.out.println("Recu :"+ resultat);
    }

    private void send(String trame) throws IOException {
        trame = trame + "#)";
        System.out.println("Envoyé :" + trame);
        dos.write(trame.getBytes());
        dos.flush();
    }

    private String receive() throws IOException {
        StringBuffer buffer = new StringBuffer();
        boolean EOT = false;
        byte b1,b2;

        while(!EOT)
        {
            b1 = dis.readByte();
            if(b1 ==  (byte)'#')
            {
                b2 = dis.readByte();
                if(b2 == (byte)')')
                {
                    EOT = true;
                }
                else
                {
                    buffer.append((char)b1);
                    buffer.append((char)b2);
                }
            }
            else
            {
                buffer.append((char)b1);
            }
        }
        return buffer.toString();
    }

    /////////////////////////////////////////////////////
    ///////////////////////AUTRES///////////////////////
    ///////////////////////////////////////////////////

    public void precedent() throws IOException {
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

    public void suivant() throws IOException {
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
