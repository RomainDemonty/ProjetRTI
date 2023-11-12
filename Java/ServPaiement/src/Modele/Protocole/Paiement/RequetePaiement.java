package Modele.Protocole.Paiement;
import Modele.Protocole.Requete;

public class RequetePaiement implements Requete {

    private String idfacture ;

    private String nomVisa ;

    private String numeroVisa ;


    public RequetePaiement(String id , String nom , String numero )
    {
        idfacture=id;
        nomVisa = nom ;
        numeroVisa = numero ;

    }

    public String getIdfacture() {
        return idfacture;
    }

    public String getNomVisa() {
        return nomVisa;
    }

    public String getNumeroVisa() {
        return numeroVisa;
    }
}
