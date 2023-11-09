package Modele.Protocole;

import Modele.Protocole.LOGOUT.RequeteLOGOUT;
import Modele.Protocole.Login.ReponseLOGIN;
import Modele.Protocole.Login.RequeteLOGIN;
import Modele.BD.* ;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
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
            FinConnexionException, SQLException, ClassNotFoundException {
        if (requete instanceof RequeteLOGIN)
        {
            return TraiteRequeteLOGIN((RequeteLOGIN) requete, socket);
        }
        if (requete instanceof RequeteLOGOUT) TraiteRequeteLOGOUT((RequeteLOGOUT)
                requete);
        return null;
    }

    private synchronized ReponseLOGIN TraiteRequeteLOGIN(RequeteLOGIN requete, Socket socket) throws FinConnexionException, SQLException, ClassNotFoundException {

        System.out.println(" [protocole] dans ReponseLOGIN  avec user = "+requete.getLogin()+ " et mdp = "+requete.getPassword() );
        donnees  = new AccesBD();
        boolean trouve = false  ;

        ResultSet rs = donnees.selection(null, "employes", null);
        while(rs.next())
        {
            if(rs.getString("username").equals(requete.getLogin()) && rs.getString("password").equals(requete.getPassword()))
            {
                System.out.println("connexion etablie protocole serveur");
                trouve = true ;
                break ;
            }

        }

        if(trouve==true)
        {
            clientsConnectes.put(requete.getLogin(), socket);
            return new ReponseLOGIN(true);
        }
        else
        {
            throw new FinConnexionException(new ReponseLOGIN(false));
        }

    }
    private synchronized void TraiteRequeteLOGOUT(RequeteLOGOUT requete) throws
            FinConnexionException
    {
        System.out.println("dans ReponseLOGOUT");
        clientsConnectes.remove(requete.getLogin());
        System.out.println("apres retirer clientconnecte");
        throw new FinConnexionException(null);
    }


}
