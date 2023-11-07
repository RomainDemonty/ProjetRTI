package Modele.Protocole;

import java.net.Socket;

public class Protocole {
    private String nom ;
    public String getNom()
    {
        return nom ;
    }
    public Reponse TraiteRequete(Requete requete, Socket socket) throws
            FinConnexionException
    {
        return null ;
    }
}
