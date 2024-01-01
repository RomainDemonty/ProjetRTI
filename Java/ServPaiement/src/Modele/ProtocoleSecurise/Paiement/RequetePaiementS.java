package Modele.ProtocoleSecurise.Paiement;

import Modele.ProtocoleSecurise.Requete;

public class RequetePaiementS implements Requete {

    private byte[] data ;

    public RequetePaiementS(byte[] d )
    {
        data = d ;
    }

    public byte[] getData() {
        return data;
    }
}
