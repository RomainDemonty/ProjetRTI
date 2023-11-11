package Controller;

import Modele.Singleton;
import Vue.Application.Application;
import Vue.Connexion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.function.ToDoubleBiFunction;

public class Controller implements ActionListener , WindowListener {

   private Connexion c ;

   public Singleton  si ;

   private Application app ;
    public Controller()
    {
        //TODO faire en sorte que si le serveur soir pas connecte que on pervienne
        c = new Connexion(this) ;
        app=new Application(this);
        app.setVisible(false);
        Singleton.getInstance();

    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==c.getSeConnecterButton())
        {
            System.out.println("bouton connexion ");
            try {

            if(Singleton.getInstance().envoyerRequeteLOGIN(c.getLogin(), c.getPassword()))
            {
                app.setVisible(true);
                c.setVisible(false);
            }
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }

        if(e.getSource()==app.getConfirmer())
        {

            System.out.println("bouton confirmer ");
            //TODO

        }

        if(e.getSource()==app.getLogoutButton())
        {
            System.out.println("bouton deconnexion ");
            try {
                Singleton.getInstance().envoyerRequeteLOGOUT();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            app.setVisible(false);
            c.setVisible(true);
        }

        if(e.getSource()==app.getVoirFacturesButton())
        {
            System.out.println("bouton pour voir les factures ");
            try {
                Singleton.getInstance().envoyerRequeteGetFactures("1");
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }

            JPanel PanelFacture = new JPanel();
            PanelFacture.setLayout(new GridLayout(0, 1));
            for (int i = 0 ; i<Singleton.getInstance().getFactures().size() ; i++)
            {
                    PanelFacture.add(new JCheckBox(Singleton.getInstance().getFactures().get(i))) ; 
            }
            app.getFactureScrollPane().setViewportView(PanelFacture);



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
        System.out.println("bouton deconnexion ");
        try {
            Singleton.getInstance().envoyerRequeteLOGOUT();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
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
