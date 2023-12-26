package Modele.ProtocoleSecurise.Facture;

import Modele.ProtocoleSecurise.Reponse;

public class ReponseGetFactureSer implements Reponse {
    private byte[] data;
    public ReponseGetFactureSer(byte[] rep)
    {
        data=rep;
    }
    public byte[] getData() { return data; }
}
