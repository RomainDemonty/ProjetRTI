package Modele.ProtocoleSecurise.Facture;

import Modele.ProtocoleSecurise.Reponse;

public class ReponseGetFactureS  implements Reponse {

    private String idFacture ;
    private String montant ;

    private String date ;
    private boolean paye ;


    public ReponseGetFactureS(String id, String d , boolean p , String m ) {

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

    @Override
    public String toString() {
        return "facture n° "+getIdFacture() + " du " + getDate() + " total : " + getMontant()   ;
    }
}