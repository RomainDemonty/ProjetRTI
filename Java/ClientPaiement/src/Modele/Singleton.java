package Modele;

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



    private Singleton() throws IOException {

        System.out.println("avant csocket ");
        csocket = new Socket("192.168.0.12",50000);
        System.out.println("apres csocket  -> valeur : " +csocket);

        //TODO
        //ENVOI D'UN OBJET LOGIN

        OutputStreamWriter osr = new OutputStreamWriter(csocket.getOutputStream());
        InputStreamReader isr = new InputStreamReader(csocket.getInputStream());

        BufferedWriter bw = new BufferedWriter(osr);
        BufferedReader br = new BufferedReader(isr);

        // Echanges de textes
        bw.write("[Client] Bonjour !");
        bw.newLine();

        bw.flush();


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

