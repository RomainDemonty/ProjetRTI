package Modele.Protocole.LOGOUT;

import Modele.Protocole.Requete;

public class RequeteLOGOUT implements Requete {
    private String login;

    public RequeteLOGOUT(String l) {
        login = l;
    }
    public String getLogin() {
        return login;
    }
}
