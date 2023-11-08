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
        connexion = new Connexion(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (connexion.getLoginButton() == e.getSource())
        {
            try {
                String er = Utilisateur.getInstance().login(connexion.Name.getText(),connexion.password.getText(),false);
                if(er != "OK")
                {
                    connexion.Error.setText(er);
                }
                else
                {
                    home = new Home(this);
                    connexion.dispose();
                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (e.getSource()==connexion.getSubscribeButton())
        {
            try {
                String er = Utilisateur.getInstance().login(connexion.Name.getText(),connexion.password.getText(),true);
                if(er != "OK")
                {
                    connexion.Error.setText(er);
                }
                else
                {
                    home = new Home(this);
                    connexion.dispose();

                }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }

        if(home != null && e.getSource()==home.getLogoutButton())
        {
            System.out.println(" Bouton Logout !!");
            try {
                Utilisateur.getInstance().logout();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if(home != null && e.getSource()==home.getLeftButton())
        {
            try {
                Utilisateur.getInstance().precedent();
                home.setArticle(Utilisateur.getInstance().articleSelect);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            //System.out.println(" Bouton précédent !" + "NumArticle: " + Utilisateur.getInstance().getNumArticle());
        }
        if(home != null && e.getSource()==home.getRightButton())
        {
            try {
                Utilisateur.getInstance().suivant();
                home.setArticle(Utilisateur.getInstance().articleSelect);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            //System.out.println(" Bouton suivant !" + "NumArticle: " + Utilisateur.getInstance().getNumArticle());
        }
        if (home != null && e.getSource() == home.getAddBagButton())
        {
            try {
                Utilisateur.getInstance().achat(home.quantitySpinner.getValue());
                Utilisateur.getInstance().consult();
                home.setArticle(Utilisateur.getInstance().articleSelect);
                home.setBagPannel();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            //System.out.println(" Bouton achat !" );
        }
        if(home != null && e.getSource() == home.getDeleteBagButton())
        {
            try {
                Utilisateur.getInstance().cancellall();
                Utilisateur.getInstance().consult();
                home.setArticle(Utilisateur.getInstance().articleSelect);
                home.setBagPannel();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (home != null && e.getSource() == home.getDeleteArticleButton())
        {
            try {
                Utilisateur.getInstance().cancell(home.getArtSelect());
                Utilisateur.getInstance().consult();
                home.setArticle(Utilisateur.getInstance().articleSelect);
                home.setBagPannel();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        if (home != null && e.getSource() == home.getBuyButton())
        {
            try {
                Utilisateur.getInstance().confirm();
                home.setBagPannel();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
}
