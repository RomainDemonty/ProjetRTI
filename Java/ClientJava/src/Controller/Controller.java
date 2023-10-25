package Controller;

import Singleton.Utilisateur;
import Swing.Connexion;
import Swing.Home;

import javax.swing.*;
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
            System.out.println(" Bouton précédent !!" + "numArticle: " + Utilisateur.getInstance().getNumArticle());
            Utilisateur.getInstance().precedent();
        }
        if(e.getSource()==home.getRightButton())
        {
            System.out.println(" Bouton suivant !!" + "numArticle: " + Utilisateur.getInstance().getNumArticle());
            Utilisateur.getInstance().suivant();
        }
       
    }
}
