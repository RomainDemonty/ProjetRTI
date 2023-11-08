package Vue;

import javax.swing.*;
import Controller.Controller ;
import Modele.Protocole.Protocole;
import Modele.Serveur.ThreadServeur;

import java.io.IOException;

public class ServeurVue extends JFrame {


    ThreadServeur threadS ;
    private JPanel contentPane;
    private JButton buttonLancer;
    private JTextField port;
    private JButton arreterLeServeurButton;

    public ServeurVue(Controller c ) throws IOException {
        setContentPane(contentPane);


        arreterLeServeurButton.setEnabled(false);
        port.setText("50000");
        threadS = new ThreadServeur(Integer.parseInt(port.getText()),new Protocole());
        setSize(400,300);
        setVisible(true );

        buttonLancer.addActionListener(c) ;
        arreterLeServeurButton.addActionListener(c) ;
    }

    public ThreadServeur getThreadS(){
        return threadS ;
    }
    public JButton getLancerButton(){
        return buttonLancer ;
    }
    public JButton getArreterButton(){
        return arreterLeServeurButton ;
    }

    public static void main(String[] args) {

    }
}
