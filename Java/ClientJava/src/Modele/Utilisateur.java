package Modele;

import java.util.ArrayList;
import java.io.*;
import java.net.*;

public class Utilisateur {

    public Article articleSelect = new Article();
    private Socket cSocket;
    private DataOutputStream dos;
    private DataInputStream dis;

    private String resultat = "";

    private ReadProperties prop ;

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

    private ArrayList<Article> monPanier = new ArrayList<>();

    public ArrayList<Article> getMonPanier(){
        return monPanier;
    }

    public float getTotal(){
        float total =0;
        for (int i = 0; getMonPanier() != null && monPanier.size() > i ; i++) {
            total = total + monPanier.get(i).getPrix()*monPanier.get(i).getQuantite();
        }
        return total;
    }

    public void addArticlePanier(Article A){
        monPanier.add(A);
    }
    public void addArt(Article A){
        Article addTemp = new Article();
        boolean trouve = false;
        for (int i = 0;( getMonPanier() != null && monPanier.size() > i ) && trouve == false; i++) {
            if(monPanier.get(i).getIdAliment() == A.getIdAliment())
            {
                addTemp = monPanier.get(i);
                addTemp.setquantite(addTemp.getQuantite()+ A.getQuantite());
                trouve = true;
            }
        }

        if (trouve == false)
        {
            addArticlePanier(A);
        }

        for (int i = 0; getMonPanier() != null && monPanier.size() > i ; i++) {
            System.out.println("Panier client " + i + ":" + monPanier.get(i));
        }
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
        if(mots[1].equals("ok"))
        {
            articleSelect.setidAliment(Integer.parseInt(mots[2]));
            articleSelect.setintitule(mots[3]);
            articleSelect.setquantite(Integer.parseInt(mots[4]));
            articleSelect.setprix(Float.parseFloat(mots[5]));
            articleSelect.setAdrImage(mots[6]);
            //System.out.println( articleSelect);
        }
        else
        {
            System.out.println("Erreur de consult");
        }
    }

    public void achat(Object quantite) throws IOException {
        if((Integer)quantite >0)
        {
            requete = "ACHAT#" + articleSelect.getIdAliment() + "#" + quantite;
            echange(requete);

            String[] mots = resultat.split("#");
            //System.out.println(mots[1]);
            if(mots[1].equals("ok"))
            {
                Article tampon;
                tampon = new Article();
                tampon.setidAliment(articleSelect.getIdAliment());
                tampon.setintitule(mots[2]);
                tampon.setquantite((Integer) quantite);
                tampon.setprix(Float.parseFloat(mots[3]));
                //System.out.println( "Ajout bag : " + tampon);

                addArt(tampon);
            }
            else
            {
                System.out.println("Erreur de consult");
            }
        }
        else
        {
            System.out.println("Quantite pas valide");
        }
    }

    public void cancell(int numArt) throws IOException {
        if(numArt != -1 )
        {
            requete = "CANCEL#" + numArt;
            echange(requete);

            String[] mots = resultat.split("#");
            if(mots[1].equals("ok"))
            {
                for (Article artPass : monPanier) {
                    if(artPass.getIdAliment() == numArt)
                    {
                        removeArticlePanier(artPass);
                        System.out.println("CANCELL_OK");
                        return;
                    }
                }
            }
            else
            {
                System.out.println("Erreur_CANCELL");
            }
        }
        else
        {
            System.out.println("CANCEL_NO_SELECT");
        }

    }

    public void cancellall() throws IOException {
        requete = "CANCELALL";
        echange(requete);

        String[] mots = resultat.split("#");
        if(mots[1].equals("ok"))
        {
            monPanier.clear();
            System.out.println("CANCELALL_OK");
        }
        else
        {
            System.out.println("CANCELALL_ERROR");
        }
    }

    public void confirm() throws IOException {
        requete = "CONFIRMER#" + idUtilisateur;
        echange(requete);

        String[] mots = resultat.split("#");
        if(mots[1].equals("ok"))
        {
            monPanier.clear();
            System.out.println("Confirm_OK");
        }
        else
        {
            System.out.println("Confirm_ERROR");
        }
    }

    public String login(String nom , String mdp, Boolean newuser) throws IOException {
        if(newuser == true)
        {
            requete = "LOGIN#" + nom + "#" + mdp + "#1";
        }
        else
        {
            requete = "LOGIN#" + nom + "#" + mdp + "#0";
        }
        echange(requete);

        String[] mots = resultat.split("#");
        if(mots[1].equals("ok"))
        {
            System.out.println("Connect_OK");
            idUtilisateur = Integer.parseInt(mots[3]);
            return "OK";
        }
        else
        {
            System.out.println("Connect_error");
            return mots[2];
        }
    }

    public Boolean logout() throws IOException {
        requete = "LOGOUT";
        echange(requete);

        String[] mots = resultat.split("#");
        if(mots[1].equals("ok"))
        {
            System.out.println("Deconnect_OK");
            idUtilisateur = 0;
            numArticle = 1;
            return true;
        }
        else
        {
            System.out.println("Deconnect_error");
            return false;
        }
        //dos.close();
        //dis.close();
        //cSocket.close();
    }

    /////////////////////////////////////////////////////
    ///////////////////Send et receive//////////////////
    ///////////////////////////////////////////////////
    public void connect() throws IOException {
        //ipServeur = getcongfig()

        prop   = new ReadProperties();

        String ipServeur = prop.getServerAddress();
        int socket = prop.getServerPort();
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
                    buffer.append((char)b1);
                    buffer.append((char)b2);
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
