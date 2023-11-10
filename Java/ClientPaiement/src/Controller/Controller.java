package Controller;

import Modele.Singleton;
import Vue.Application.Application;
import Vue.Connexion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.function.ToDoubleBiFunction;

public class Controller implements ActionListener {

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
            //TODO

        }
    }
}