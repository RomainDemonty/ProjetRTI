package Modele.Protocole.Login;

import Modele.Protocole.Reponse;

public class ReponseLOGIN implements Reponse {

    private boolean valide;

    public ReponseLOGIN(boolean v) {
        valide = v;
    }
    public boolean isValide() {
        return valide;
    }
}


