package Modele.Protocole.Paiement;

import Modele.Protocole.Reponse;

public class ReponsePaiement implements Reponse {

    private boolean ok ;

    public ReponsePaiement(boolean bool )
    {
        ok=bool;
    }

    public boolean isOk() {
        return ok;
    }
}