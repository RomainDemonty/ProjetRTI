package Modele.Protocole;

import Modele.Protocole.Facture.ReponseGetFacture;
import Modele.Protocole.Facture.ReponseGetFactureTab;
import Modele.Protocole.Facture.RequeteGetFacture;
import Modele.Protocole.LOGOUT.RequeteLOGOUT;
import Modele.Protocole.Login.ReponseLOGIN;
import Modele.Protocole.Login.RequeteLOGIN;
import Modele.BD.* ;
import Modele.Protocole.Paiement.ReponsePaiement;
import Modele.Protocole.Paiement.RequetePaiement;

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
            return TraiteRequeteGetFacture((RequeteGetFacture) requete);
        if(requete instanceof RequetePaiement)
            return TraiteRequetePAIEMENT((RequetePaiement) requete);
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

    private synchronized ReponsePaiement TraiteRequetePAIEMENT(RequetePaiement requete) throws
            FinConnexionException, SQLException {
        System.out.println("dans ReponsePaiement");

        //TODO verifie la carte , si ok faire une requete qui place le booleen a true pour paye
        if(isCarteValid(requete.getNumeroVisa()))
        {
            String tmp[] = new String[1];
            tmp[0] = "idfacture = " + requete.getIdfacture();
            int rs = donnees.update("factures", "paye = true",tmp) ;
            if(rs!=0)
            {
                return new ReponsePaiement(true);
            }
        }
        return new ReponsePaiement(false);

    }


        private synchronized ReponseGetFactureTab TraiteRequeteGetFacture(RequeteGetFacture requete) throws FinConnexionException, SQLException, ClassNotFoundException, IOException {


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
                        boolean b = false ;
                        if(rs.getString("paye").equals("1")) b=true  ;
                        ReponseGetFacture rep = new ReponseGetFacture( rs.getString("idfacture"), rs.getString("date"),b,Float.toString(total));
                        reponse.addFacture(rep);
                    }

                }
                return reponse ;
            }


    public static boolean isCarteValid(String numeroCarte) {
        int somme = 0;
        boolean doubleDigit = false;

        for (int i = numeroCarte.length() - 1; i >= 0; i--) {
            int chiffre = numeroCarte.charAt(i) - '0';

            if (doubleDigit) {
                chiffre *= 2;

                if (chiffre > 9) {
                    chiffre -= 9;
                }
            }

            somme += chiffre;
            doubleDigit = !doubleDigit;
        }

        return somme % 10 == 0;
    }


    }
