package Controller;

import Vue.Application.Application;
import Vue.Connexion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;

public class Controller implements ActionListener , WindowListener {

    private Connexion  c   ;
    private Application app ;
    public Controller()
    {
        c = new Connexion(this) ;
        app=new Application(this);
        app.setVisible(false);

    }
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==c.getSeConnecterButton())
        {
            //TODO demande au prof si c'es notre serveur qui doit faire un digest pour comparere les 2 digest ou si tout se fait dans la classe requeteLOGIN
            // avec du coup des get dans la classe en plus pour recup els apram or pswd
            System.out.println("bouton connecter");
            //todo envoyer un digest sale pour se connecter ou le password est "crypté"
            // reception d'une cle de session
            app.setVisible(true);
            c.setVisible(false);
        }

        if(e.getSource()==app.getConfirmer()) {

            System.out.println("bouton Acheter ");


        }

        if(e.getSource()==app.getLogoutButton())
        {
            System.out.println("bouton deconnexion ");

        }

        if(e.getSource()==app.getVoirFacturesButton())
        {
            System.out.println("bouton voir factures ");

        }

        c.repaint();
        c.revalidate();
        app.repaint();
        app.revalidate();
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
