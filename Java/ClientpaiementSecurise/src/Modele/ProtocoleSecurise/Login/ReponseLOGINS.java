package Modele.ProtocoleSecurise.Login;

import Modele.ProtocoleSecurise.Reponse;

public class ReponseLOGINS implements Reponse {


    private boolean valide;

    private byte[] cleSession ;


    public ReponseLOGINS(boolean v) {
        valide = v;
    } // utiliser quand false , pas besoin de cle de session

    public ReponseLOGINS(boolean v, byte[]  cleS) { // utiliser quand true
        valide = v;
        cleSession=cleS ;
    }
    public boolean isValide() {
        return valide;
    }

    public byte[] getCleSession() {
        return cleSession;
    }
}