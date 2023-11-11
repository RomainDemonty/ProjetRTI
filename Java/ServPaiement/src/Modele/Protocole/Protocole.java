package Modele.Protocole;

import Modele.Protocole.Facture.ReponseGetFacture;
import Modele.Protocole.Facture.ReponseGetFactureTab;
import Modele.Protocole.Facture.RequeteGetFacture;
import Modele.Protocole.LOGOUT.RequeteLOGOUT;
import Modele.Protocole.Login.ReponseLOGIN;
import Modele.Protocole.Login.RequeteLOGIN;
import Modele.BD.* ;

import java.io.IOException;
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
            FinConnexionException, SQLException, ClassNotFoundException, IOException {
        if (requete instanceof RequeteLOGIN)
        {
            return TraiteRequeteLOGIN((RequeteLOGIN) requete, socket);
        }
        if (requete instanceof RequeteLOGOUT) TraiteRequeteLOGOUT((RequeteLOGOUT)
                requete);
        if(requete instanceof RequeteGetFacture)
            return TraiteRequeteGetFacture((RequeteGetFacture) requete, socket);
        return null;
    }

    private synchronized ReponseLOGIN TraiteRequeteLOGIN(RequeteLOGIN requete, Socket socket) throws FinConnexionException, SQLException, ClassNotFoundException, IOException {

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


        private synchronized ReponseGetFactureTab TraiteRequeteGetFacture(RequeteGetFacture requete, Socket socket) throws FinConnexionException, SQLException, ClassNotFoundException, IOException {


                ReponseGetFactureTab reponse = new ReponseGetFactureTab();
                System.out.println(" [protocole] dans Reponse GETFACTURE" );
                donnees  = new AccesBD();

                ResultSet rs = donnees.selection(null, "factures", null);
                while(rs.next())
                {
                    if(rs.getString("idclient").equals(requete.getIdclient()) )
                    {
                        System.out.println(rs.getString("idclient"));
                        float total = 0 ;
                        String[] condition = new String[1];
                        condition[0] = "idfacture = " + rs.getString("idfacture") ;
                        ResultSet s = donnees.selection(null, "articlesachetes", condition);
                        while(s.next())
                        {
                            total+=( Float.parseFloat(s.getString("prix")) * Float.parseFloat(s.getString("quantite")));
                        }
                       ReponseGetFacture rep = new ReponseGetFacture( rs.getString("idfacture"), rs.getString("date"),Boolean.parseBoolean(rs.getString("paye")),Float.toString(total));
                        reponse.addFacture(rep);
                    }

                }
                return reponse ;
            }



    }
