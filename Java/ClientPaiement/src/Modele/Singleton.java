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
        System.out.println("avant csocket ");
        csocket = new Socket("192.168.0.12",50000);
        System.out.println("apres csocket  -> valeur : " +csocket);

        //TODO
        //ENVOI D'UN OBJET LOGIN


        //dois encore le lire apres


    }
    public void seConnecter()
    {

    }

    public boolean envoyerRequeteLOGIN() throws IOException, ClassNotFoundException {

        RequeteLOGIN requete = new RequeteLOGIN("admin","admin");
        ObjectOutputStream oos = new ObjectOutputStream(csocket.getOutputStream());
        ois = new ObjectInputStream(csocket.getInputStream());

        oos.writeObject(requete);
        ReponseLOGIN reponse = (ReponseLOGIN) ois.readObject();
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
        ObjectOutputStream oos = new ObjectOutputStream(csocket.getOutputStream());
        ois = new ObjectInputStream(csocket.getInputStream());
        oos.writeObject(requete);


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

