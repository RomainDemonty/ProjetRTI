package Controller;

import Modele.Singleton;
import Vue.Application.Application;
import Vue.Connexion;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Controller implements ActionListener {

   private Connexion c ;

   public Singleton  si ;

   private Application app ;
    public Controller()
    {
        c = new Connexion(this) ;
        Singleton.getInstance();

    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==c.getSeConnecterButton())
        {
            System.out.println("bouton connexion ");
            //TODO
            // faire un dmd au serveur et voir si on est bien connecte
            if(true)
            {
                app = new Application(this);
                c.dispose();
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
            //TODO
            // PREVENIR SERVEUR QUE ON SE DECO
            if(true)
            {
                c = new Connexion(this);
                app.dispose();

            }
        }
        if(e.getSource()==app.getVoirFacturesButton())
        {

            System.out.println("bouton pour voir les factures ");
            //TODO

        }
    }
}
