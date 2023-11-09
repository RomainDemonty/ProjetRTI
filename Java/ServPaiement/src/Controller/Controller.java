package Controller;
import Vue.ServeurVue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Controller implements ActionListener{

    ServeurVue svue ;
    public Controller() throws IOException {
        svue = new ServeurVue(this) ;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==svue.getLancerButton())
        {
            System.out.println("Serveur lancé");
            svue.getLancerButton().setEnabled(false);
            svue.getArreterButton().setEnabled(true);
            try {
                svue.setThreadS();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            svue.getThreadS().start();
        }
        if(e.getSource()==svue.getArreterButton())
        {
            System.out.println("Serveur arreté");
            svue.getLancerButton().setEnabled(true);
            svue.getArreterButton().setEnabled(false);
        }
    }
}
