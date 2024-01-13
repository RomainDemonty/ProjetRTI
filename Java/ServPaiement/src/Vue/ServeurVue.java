package Vue;

import javax.swing.*;
import Controller.Controller ;
import Modele.Protocole.Protocole;
import Modele.ProtocoleSecurise.VESPAPS;
import Modele.Serveur.ThreadServeur;
import Modele.Serveur.ThreadServeurSecu;

import java.io.IOException;
import java.sql.SQLException;

public class ServeurVue extends JFrame {


    private ThreadServeur threadS ;

    private ThreadServeurSecu threadSS ;
    private JPanel contentPane;
    private JButton buttonLancer;
    private JTextField port;
    private JButton arreterLeServeurButton;
    private JTextField nbthread;
    private JTextField portsecu;
    private JButton lancerServeurSecu;


    public ServeurVue(Controller c ) throws IOException {
        setContentPane(contentPane);
        threadS = null ;

        arreterLeServeurButton.setEnabled(false);
        port.setText("50000");
        portsecu.setText("50010");
        nbthread.setText("1");
        setLocationRelativeTo(null);
        setSize(600,300);
        setVisible(true);

        buttonLancer.addActionListener(c) ;
        arreterLeServeurButton.addActionListener(c) ;

        lancerServeurSecu.addActionListener(c) ;
    }

    public ThreadServeur getThreadS(){
        return threadS ;
    }
    public ThreadServeurSecu getThreadSS(){
        return threadSS ;
    }
    public JButton getLancerButton(){
        return buttonLancer ;
    }
    public JButton getArreterButton(){
        return arreterLeServeurButton ;
    }
    public JButton getLancerServeurSecu(){
        return lancerServeurSecu ;
    }

    public void setThreadS() throws IOException, SQLException, ClassNotFoundException {
        Protocole p = new Protocole() ;
        threadS = new ThreadServeur(Integer.parseInt(port.getText()),p,getNbthread());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void setThreadSS() throws IOException, SQLException, ClassNotFoundException {
        VESPAPS p = new VESPAPS() ;
        threadSS = new ThreadServeurSecu(Integer.parseInt(portsecu.getText()),p);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }



    public JTextField getPortSecu() {
        return portsecu;
    }
    public JTextField getPort() {
        return port;
    }

    public int getNbthread() {
        return Integer.parseInt(nbthread.getText());
    }

    
}
