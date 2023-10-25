package Controller;

import Modele.Utilisateur;
import Swing.Connexion;
import Swing.Home;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {

    private Home home ;
    private Connexion connexion ;


    public Controller()
    {
        home = new Home(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==home.getLogoutButton())
        {
            System.out.println(" Bouton Logout !!");
        }
        if(e.getSource()==home.getLeftButton())
        {
            Utilisateur.getInstance().precedent();
            System.out.println(" Bouton précédent !!" + "numArticle: " + Utilisateur.getInstance().getNumArticle());
        }
        if(e.getSource()==home.getRightButton())
        {
            Utilisateur.getInstance().suivant();
            System.out.println(" Bouton suivant !!" + "numArticle: " + Utilisateur.getInstance().getNumArticle());
        }
       
    }
}
