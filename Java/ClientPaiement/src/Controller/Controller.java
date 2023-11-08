package Controller;

import Vue.Connexion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {

   private Connexion c ;
    public Controller()
    {
        c = new Connexion(this) ;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==c.getSeConnecterButton())
        {
            System.out.println("bouton connexion ");
        }
    }
}
