package Modele.ProtocoleSecurise.Facture;

import Modele.ProtocoleSecurise.Reponse;

import java.util.ArrayList;

public class ReponseGetFactureTabS implements Reponse {
    private boolean signIsValide  ;

    private ArrayList<ReponseGetFactureS> tabFactures ;

    public ReponseGetFactureTabS(boolean b)
    {
        tabFactures = new ArrayList<>() ;
        signIsValide = b ;
    }

    public void addFacture(ReponseGetFactureS r)
    {
        tabFactures.add( r) ;
    }

    public ArrayList<ReponseGetFactureS> getTabFactures() {
        return tabFactures;
    }

    public boolean isSignIsValide() {
        return signIsValide;
    }
}