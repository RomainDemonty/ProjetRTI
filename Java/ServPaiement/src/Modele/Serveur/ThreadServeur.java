package Modele.Serveur;

import Modele.Protocole.Protocole;

import java.io.IOException;
import java.net.ServerSocket;
public abstract class ThreadServeur extends Thread
{
    protected int port;
    protected Protocole protocole;

    protected ServerSocket ssocket;

    public ThreadServeur(int port, Protocole protocole) throws
            IOException
    {
        super("TH Serveur (port=" + port + ",protocole=" + protocole.getNom() + ")");
        this.port = port;
        this.protocole = protocole;
        ssocket = new ServerSocket(port);
    }
}
