package Modele.Serveur;

import Modele.Protocole.Protocole;
import Modele.Protocole.VESPAPS;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ThreadServeurSecu extends Thread{
    private FileAttente connexionsEnAttente;
    private ThreadGroup pool;
    private int taillePool;

    protected int port;

    protected VESPAPS protocole;
    protected ServerSocket ssocket;

    public ThreadServeurSecu(int port, VESPAPS protocole, int taillePool) throws
            IOException
    {
        super("TH Serveur (port=" + port + ",protocole=" + protocole.getNom() + ")");
        this.port = port;
        this.protocole = protocole;
        ssocket = new ServerSocket(port);

        connexionsEnAttente = new FileAttente();
        pool = new ThreadGroup("POOL");
        this.taillePool = taillePool;
    }



    @Override
    public void run()
    {

            for (int i=0 ; i<taillePool ; i++)
            {
               //todo : lance le threadpaiementsecu +try catch
            }

        // Attente des connexions
        while(!this.isInterrupted())
        {
            Socket csocket;
            try
            {
                ssocket.setSoTimeout(2000);
                System.out.println(ssocket.getLocalPort());
                csocket = ssocket.accept();
                connexionsEnAttente.addConnexion(csocket);

            }
            catch (SocketTimeoutException ex)
            {
                System.out.println("exception socketTimeout dans le thread Serveur");
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

