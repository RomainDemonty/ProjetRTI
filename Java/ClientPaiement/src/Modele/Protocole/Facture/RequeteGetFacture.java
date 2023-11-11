package Modele.Protocole.Facture;

import Modele.Protocole.Requete;

public class RequeteGetFacture implements Requete {

    private String idclient ;

    public RequeteGetFacture(String id)
    {
        idclient=id;
    }

    public String getIdclient() {
        return idclient;
    }
}
