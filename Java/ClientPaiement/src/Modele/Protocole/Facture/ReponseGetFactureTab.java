package Modele.Protocole.Facture;

import Modele.Protocole.Reponse;

import java.util.ArrayList;

public class ReponseGetFactureTab implements Reponse {

    private ArrayList<ReponseGetFacture> tabFactures ;

    public ReponseGetFactureTab()
    {
        tabFactures = new ArrayList<>() ;
    }

    public void addFacture(ReponseGetFacture r)
    {
        tabFactures.add( r) ;
    }

    public ArrayList<ReponseGetFacture> getTabFactures() {
        return tabFactures;
    }
}