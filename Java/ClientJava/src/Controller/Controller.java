package Controller;
import javax.swing.*;
import java.awt.*;

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
        if(connexion.getReconnect() == e.getSource())
        {
            try {
                Utilisateur.getInstance().connect();
                Utilisateur.getInstance().conect = true;
                connexion.getReconnect().setVisible(false);
                connexion.Error.setText("Reconnexion réussie!");
                Color col = Color.green;
                connexion.Error.setForeground(col);
            } catch (IOException ex) {
                System.out.println("Reconnexion ratee");
                connexion.Error.setText("Reconnexion ratée ! Réessayer ultérieurement!");
            }
        }
        if (connexion.getLoginButton() == e.getSource() && Utilisateur.getInstance().conect == true)
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
        if (e.getSource()==connexion.getSubscribeButton() && Utilisateur.getInstance().conect == true)
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
        else
        {
            if(e.getSource()==home.getLogoutButton())
            {
                System.out.println(" Bouton Logout !!");
                try {
                    Utilisateur.getInstance().cancellall();
                    if((Utilisateur.getInstance().logout())==true)
                    {
                        connexion= new Connexion(this);
                        home.dispose();
                    }
                    else
                    {
                        System.out.println("Error_logout");
                    }
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
                //System.out.println(" Bouton précédent !" + "NumArticle: " + Utilisateur.getInstance().getNumArticle());
            }
            if(e.getSource()==home.getRightButton())
            {
                try {
                    Utilisateur.getInstance().suivant();
                    home.setArticle(Utilisateur.getInstance().articleSelect);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                //System.out.println(" Bouton suivant !" + "NumArticle: " + Utilisateur.getInstance().getNumArticle());
            }
            if (e.getSource() == home.getAddBagButton())
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
            if(e.getSource() == home.getDeleteBagButton())
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
            if (e.getSource() == home.getDeleteArticleButton())
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
            if (e.getSource() == home.getBuyButton())
            {
                try {
                    Utilisateur.getInstance().confirm();
                    home.setBagPannel();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            home.MessageErreur.setText(Utilisateur.getInstance().MessageErr);
        }
    }
}
