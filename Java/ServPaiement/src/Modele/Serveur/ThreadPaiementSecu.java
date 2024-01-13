package Modele.Serveur;

import Modele.ProtocoleSecurise.*;

import Modele.ProtocoleSecurise.VESPAPS;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.*;
import java.security.cert.CertificateException;
import java.sql.SQLException;

public class ThreadPaiementSecu extends Thread {




    protected VESPAPS protocole;
    protected Socket csocket;
    private int numero;

    private static int numCourant = 1;

    public ThreadPaiementSecu(VESPAPS protocole, Socket csocket) throws
            IOException {
        super("TH Paiement securisé " + numCourant + " (protocole=" + protocole.getNom() + ")");
        this.protocole = protocole;
        this.csocket = csocket;
        this.numero = numCourant++;
    }

    public ThreadPaiementSecu(VESPAPS protocole, Socket clientSocket, ThreadGroup groupe)
            throws IOException {
        super(groupe, "TH Paiement securisé  " + numCourant + " (protocole=" + protocole.getNom()
                + ")");
        this.protocole = protocole;
        this.csocket = clientSocket;
        this.numero = numCourant++;
    }

    @Override
    public void run() {
        System.out.println("TH Client (dmd) démarre...");
        boolean interrompu = false;

        while(!interrompu && !this.isInterrupted())
        {
            try
            {
                boolean deconnecte = true ;
                System.out.println("[ThreadPaiementSecu] Attente d'une connexion...");
                System.out.println("[ThreadPaiementSecu] Connexion prise en charge.");
                ObjectOutputStream oos = null;
                ObjectInputStream ois = null;
                ois = new ObjectInputStream(csocket.getInputStream());
                oos = new ObjectOutputStream(csocket.getOutputStream());

                while(deconnecte && !this.isInterrupted()) {
                    try {
                        csocket.setSoTimeout(2000);
                        Requete requete = (Requete) ois.readObject();
                        System.out.println("la requete =  " + requete.toString());
                        Reponse reponse = (Reponse) protocole.TraiteRequeteS(requete, csocket);
                        oos.writeObject(reponse);

                    }catch (SocketTimeoutException e) {
                        //System.out.println(" [ThreadPaiementSecu] time out 2 sec");
                    } catch (SocketException e){
                        csocket.close();
                        break;
                    }
                    catch (IOException e) {
                        System.out.println("[ThreadPaiementSecu] dans ioexception ");
                        throw new RuntimeException(e);
                    } catch (ClassNotFoundException e) {
                        System.out.println("[ThreadPaiementSecu] dans classnotfoundexception ");
                        throw new RuntimeException(e);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    } catch (FinConnexionException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchProviderException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchPaddingException e) {
                        throw new RuntimeException(e);
                    } catch (IllegalBlockSizeException e) {
                        throw new RuntimeException(e);
                    } catch (CertificateException e) {
                        throw new RuntimeException(e);
                    } catch (KeyStoreException e) {
                        throw new RuntimeException(e);
                    } catch (BadPaddingException e) {
                        throw new RuntimeException(e);
                    } catch (InvalidKeyException e) {
                        throw new RuntimeException(e);
                    } catch (Modele.Protocole.FinConnexionException e) {
                        throw new RuntimeException(e);
                    } catch (SignatureException e) {
                        throw new RuntimeException(e);
                    }

                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("TH Client (dmd) se termine.");
        try {
            if(csocket!=null)
                csocket.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }


    }
}