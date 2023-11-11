package Modele;

import Modele.Protocole.LOGOUT.RequeteLOGOUT;
import Modele.Protocole.Login.ReponseLOGIN;
import Modele.Protocole.Login.RequeteLOGIN;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class Singleton {
    private static Singleton instance;

    static {
        try {
            instance = new Singleton();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Singleton getInstance() {
        return instance;
    }

    private String numVISA ;
    private String nomVISA ;
    private String idClient ;
    private String nomEmploye ;
    private String mdpEmploye;

    Socket csocket;

    // Création de la socket et connexion sur le serveur


    private ObjectOutputStream oos;
    private ObjectInputStream ois;


    private Singleton() throws IOException {


        oos =null ;
        ois = null  ;

        //TODO
        //ENVOI D'UN OBJET LOGIN


        //dois encore le lire apres


    }
    public void seConnecter()
    {

    }

    public boolean envoyerRequeteLOGIN(String u , String mdp) throws IOException, ClassNotFoundException {

        System.out.println("avant csocket ");
        csocket = new Socket("localhost",50000);
        System.out.println("apres csocket  -> valeur : " +csocket);

        oos = new ObjectOutputStream(csocket.getOutputStream());
        ois = new ObjectInputStream(csocket.getInputStream());

        RequeteLOGIN requete = new RequeteLOGIN(u,mdp);

        oos.writeObject(requete);
        ReponseLOGIN reponse = (ReponseLOGIN) ois.readObject();
        System.out.println("reponse du serveur pour login : " + reponse.isValide());
        if (reponse.isValide())
        {
            System.out.println("connexion confirmée");
            return true  ;
        }
        else {
            return false  ;
        }

    }
    public void envoyerRequeteLOGOUT() throws IOException, ClassNotFoundException {

        RequeteLOGOUT requete = new RequeteLOGOUT("admin");
        oos.writeObject(requete);
        csocket.close();


    }


    public String getIdClient() {
        return idClient;
    }

    public String getMdpEmploye() {
        return mdpEmploye;
    }

    public String getNomEmploye() {
        return nomEmploye;
    }

    public String getNomVISA() {
        return nomVISA;
    }

    public String getNumVISA() {
        return numVISA;
    }

}

