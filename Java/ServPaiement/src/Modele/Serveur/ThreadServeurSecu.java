package Modele.Serveur;

import Modele.ProtocoleSecurise.VESPAPS;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ThreadServeurSecu extends Thread{
    private ThreadGroup pool;
    private static int taillePool = 0 ;

    protected int port;

    protected VESPAPS protocole;
    protected ServerSocket ssocket;

    public ThreadServeurSecu(int port, VESPAPS protocole) throws
            IOException
    {
        super("TH Serveur (port=" + port + ",protocole=" + protocole.getNom() + ")");
        this.port = port;
        this.protocole = protocole;
        ssocket = new ServerSocket(port);
        pool = new ThreadGroup("DEMANDE");
        this.taillePool ++;
    }



    @Override
    public void run()
    {

        // Attente des connexions
        while(!this.isInterrupted())
        {
            Socket csocket;
            try
            {
                ssocket.setSoTimeout(2000);
                System.out.println(ssocket.getLocalPort());
                csocket = ssocket.accept();
                new ThreadPaiementSecu(protocole , csocket,pool).start();
                System.out.println("Creation du thread qui ecoute sur la socket : " + csocket);

            }
            catch (SocketTimeoutException ex)
            {
                //System.out.println("exception socketTimeout dans le thread Serveur secu ");
            }
            catch (IOException ex)
            {
                System.out.println("Erreur I/O");
            }
        }
        pool.interrupt();
        try {
            ssocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("le thread serveur securisé est interompu ");

    }
}

