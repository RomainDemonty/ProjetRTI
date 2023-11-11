package Modele.Protocole.Facture;

import Modele.Protocole.Reponse;

public class ReponseGetFacture implements Reponse {

    private String idFacture ;
    private String montant ;

    private String date ;
    private boolean paye ;


    public ReponseGetFacture(String id, String d , boolean p , String m ) {

        date = d ;
        paye = p ;
        idFacture = id ;
        montant = m ;

    }

    public boolean isPaye() {
        return paye;
    }

    public String getIdFacture() {
        return idFacture;
    }



    public String getDate() {
        return date;
    }

    public String getMontant() {
        return montant;
    }
}