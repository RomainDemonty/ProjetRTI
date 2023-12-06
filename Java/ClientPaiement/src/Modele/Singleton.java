package Modele;

import Modele.Protocole.Facture.ReponseGetFacture;
import Modele.Protocole.Facture.ReponseGetFactureTab;
import Modele.Protocole.Facture.RequeteGetFacture;
import Modele.Protocole.LOGOUT.RequeteLOGOUT;
import Modele.Protocole.Login.ReponseLOGIN;
import Modele.Protocole.Login.RequeteLOGIN;
import Modele.Protocole.Paiement.ReponsePaiement;
import Modele.Protocole.Paiement.RequetePaiement;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;

public class Singleton {

    ArrayList<ReponseGetFacture> factures ;
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



    }
    public void seConnecter()
    {

    }

    public boolean envoyerRequeteLOGIN(String u , String mdp) throws IOException, ClassNotFoundException {

        Properties P = new Properties() ;
        P.load(new FileInputStream(System.getProperty("user.dir")+"\\properties.properties"));

        System.out.println("avant csocket ");
        csocket = new Socket(P.getProperty("ipAdress"),Integer.parseInt(P.getProperty("socket")));
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

            csocket.close();
            return false  ;

        }

    }
    public void envoyerRequeteLOGOUT() throws IOException, ClassNotFoundException {

        RequeteLOGOUT requete = new RequeteLOGOUT("admin");
        oos.writeObject(requete);
        csocket.close();


    }
    public boolean envoyerRequeteGetFactures(String id) throws IOException, ClassNotFoundException {

        factures=new ArrayList<>() ;
        RequeteGetFacture requete = new RequeteGetFacture(id);
        oos.writeObject(requete);
        ReponseGetFactureTab reponse = (ReponseGetFactureTab) ois.readObject();

        for(int i = 0 ; i<reponse.getTabFactures().size();i++)
        {
            if(reponse.getTabFactures().get(i).isPaye()==false)
            {
                System.out.println(reponse.getTabFactures().get(i).getMontant());
                factures.add(reponse.getTabFactures().get(i)) ;
            }

        }
        if(reponse.getTabFactures().size()!=0)
        {
            return true ;
        }
        else return false ;

    }

    public boolean envoyerRequetePayer(String id , String nom ,String visa ) throws IOException, ClassNotFoundException {


        RequetePaiement requete = new RequetePaiement(id , nom , visa);
        oos.writeObject(requete);
        ReponsePaiement  reponse = (ReponsePaiement) ois.readObject();
        System.out.println("reponse du serveur pour la requete de payement de la facture : id " + reponse);



        return reponse.isOk() ;
    }


    public ArrayList<ReponseGetFacture> getFactures()
    {
        return factures ;
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

    public Socket getCsocket() {
        return csocket;
    }
}

