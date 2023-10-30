package Controller;

import Modele.Utilisateur;
import Swing.Connexion;
import Swing.Home;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;


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
            try {
                Utilisateur.getInstance().logout();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if(e.getSource()==home.getLeftButton())
        {
            try {
                Utilisateur.getInstance().precedent();
                home.setArticle(Utilisateur.getInstance().articleSelect);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println(" Bouton précédent !" + "NumArticle: " + Utilisateur.getInstance().getNumArticle());
        }
        if(e.getSource()==home.getRightButton())
        {
            try {
                Utilisateur.getInstance().suivant();
                home.setArticle(Utilisateur.getInstance().articleSelect);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println(" Bouton suivant !" + "NumArticle: " + Utilisateur.getInstance().getNumArticle());
        }
        if (e.getSource() == home.getAddBagButton())
        {
            try {
                Utilisateur.getInstance().achat(home.quantitySpinner.getValue());
                Utilisateur.getInstance().consult();
                home.setArticle(Utilisateur.getInstance().articleSelect);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println(" Bouton achat !" );
        }
       
    }
}
