package Vue;

import javax.swing.*;
import Controller.Controller ;
import Modele.Protocole.Protocole;
import Modele.Serveur.ThreadServeur;

import java.io.IOException;
import java.sql.SQLException;

public class ServeurVue extends JFrame {


    private ThreadServeur threadS ;
    private JPanel contentPane;
    private JButton buttonLancer;
    private JTextField port;
    private JButton arreterLeServeurButton;
    private JTextField nbthread;


    public ServeurVue(Controller c ) throws IOException {
        setContentPane(contentPane);
        threadS = null ;

        arreterLeServeurButton.setEnabled(false);
        port.setText("50000");
        nbthread.setText("1");
        setLocationRelativeTo(null);
        setSize(400,300);
        setVisible(true);

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

    public void setThreadS() throws IOException, SQLException, ClassNotFoundException {
        Protocole p = new Protocole() ;
        System.out.println(p);
        threadS = new ThreadServeur(Integer.parseInt(port.getText()),p,getNbthread());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    public static void main(String[] args) {

    }

    public JTextField getPort() {
        return port;
    }

    public int getNbthread() {
        return Integer.parseInt(nbthread.getText());
    }

    
}
