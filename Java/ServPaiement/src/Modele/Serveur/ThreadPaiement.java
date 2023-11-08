package Modele.Serveur;

import Modele.Protocole.FinConnexionException;
import Modele.Protocole.Protocole;
import Modele.Protocole.Reponse;
import Modele.Protocole.Requete;

import java.io.*;
import java.net.Socket;

public class ThreadPaiement extends Thread {

    private FileAttente connexionsEnAttente;


    protected Protocole protocole;
    protected Socket csocket;
    private int numero;

    private static int numCourant = 1;

    public ThreadPaiement(Protocole protocole, Socket csocket) throws
            IOException {
        super("TH Paiement " + numCourant + " (protocole=" + protocole.getNom() + ")");
        this.protocole = protocole;
        this.csocket = csocket;

        this.numero = numCourant++;
    }

    public ThreadPaiement(Protocole protocole, FileAttente file, ThreadGroup groupe)
            throws IOException {
        super(groupe, "TH Paiement " + numCourant + " (protocole=" + protocole.getNom()
                + ")");
        this.protocole = protocole;
        this.csocket = null;
        this.numero = numCourant++;

        this.connexionsEnAttente = file ;
    }

    @Override
    public void run() {

        System.out.println("TH Client (Pool) démarre...");
        boolean interrompu = false;
        while(!interrompu)
        {
            try
            {
                System.out.println("Attente d'une connexion...");
                csocket = connexionsEnAttente.getConnexion();
                System.out.println("Connexion prise en charge.");
                ObjectOutputStream oos = null;
                ObjectInputStream ois = null;

                try {


                    OutputStreamWriter osr = new OutputStreamWriter(csocket.getOutputStream());
                    InputStreamReader isr = new InputStreamReader(csocket.getInputStream());

                    BufferedWriter bw = new BufferedWriter(osr);
                    BufferedReader br = new BufferedReader(isr);

                    // Echanges de textes
                    String requete;
                    requete= br.readLine();
                    System.out.println(requete);

                    //TODO
                    //RECEPTION ET AFFICHAGE D'UN OBJET LOGIN

                   /*  avec objket
                     ois = new ObjectInputStream(csocket.getInputStream());
                    oos = new ObjectOutputStream(csocket.getOutputStream());
                    while (true) {
                        Requete requete = (Requete) ois.readObject();
                        Reponse reponse = protocole.TraiteRequete(requete, csocket);
                        oos.writeObject(reponse);
                    }*/
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            catch (InterruptedException ex)
            {
                System.out.println("Demande d'interruption...");
                interrompu = true;
            }
        }
        System.out.println("TH Client (Pool) se termine.");


    }
}
