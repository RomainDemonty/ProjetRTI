package Controller;

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
            System.out.println(" actionlistener marche !!");
        }
       
    }
}
