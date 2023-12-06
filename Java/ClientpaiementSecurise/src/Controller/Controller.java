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
