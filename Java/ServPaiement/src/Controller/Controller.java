package Controller;
import Vue.ServeurVue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.SQLException;

public class Controller implements ActionListener{

    ServeurVue svue ;
    boolean secu ;
    public Controller() throws IOException {
        svue = new ServeurVue(this) ;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==svue.getLancerButton())
        {
            System.out.println("Serveur lancé");
            secu = false ;
            svue.getLancerButton().setEnabled(false);
            svue.getLancerServeurSecu().setEnabled(false);
            svue.getArreterButton().setEnabled(true);
            try {
                svue.setThreadS();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            svue.getThreadS().start();
        }
        if(e.getSource()==svue.getArreterButton())
        {
            System.out.println("Serveur arreté");
            if(!secu)
            {
                svue.getThreadS().interrupt();
            }
            else{
                svue.getThreadSS().interrupt();
            }

            svue.getLancerButton().setEnabled(true);
            svue.getLancerServeurSecu().setEnabled(true);
            svue.getArreterButton().setEnabled(false);
        }
        if(e.getSource()==svue.getLancerServeurSecu())
        {
            System.out.println("Serveur securisé lancé");
            secu = true ;
            svue.getLancerButton().setEnabled(false);
            svue.getLancerServeurSecu().setEnabled(false);
            svue.getArreterButton().setEnabled(true);

            try {
                svue.setThreadSS();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            svue.getThreadSS().start();

        }

    }
}
