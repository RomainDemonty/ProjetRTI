package Modele.Protocole;

import Modele.Protocole.LOGOUT.RequeteLOGOUT;
import Modele.Protocole.Login.ReponseLOGIN;
import Modele.Protocole.Login.RequeteLOGIN;
import Modele.BD.* ;
import java.net.Socket;
import java.util.HashMap;

public class Protocole {

    private AccesBD  donnees;
    private String nom ;
    public String getNom()
    {
        return nom ;
    }
    private HashMap<String,Socket> clientsConnectes;

    public Protocole()
    {
        clientsConnectes = new HashMap<>();
        donnees = null ;
    }


    public synchronized Reponse TraiteRequete(Requete requete, Socket socket) throws
            FinConnexionException
    {
        if (requete instanceof RequeteLOGIN)
        {
            return TraiteRequeteLOGIN((RequeteLOGIN) requete, socket);
        }
        if (requete instanceof RequeteLOGOUT) TraiteRequeteLOGOUT((RequeteLOGOUT)
                requete);
        return null;
    }

    private synchronized ReponseLOGIN TraiteRequeteLOGIN(RequeteLOGIN requete, Socket socket) throws FinConnexionException {

        System.out.println("dans ReponseLOGIN");

        //TODO verif de passwords dans la bd
        clientsConnectes.put(requete.getLogin(), socket);

        return new ReponseLOGIN(true);


    }
    private synchronized void TraiteRequeteLOGOUT(RequeteLOGOUT requete) throws
            FinConnexionException
    {
        System.out.println("dans ReponseLOGOUT");
        clientsConnectes.remove(requete.getLogin());
        throw new FinConnexionException(null);
    }


}
