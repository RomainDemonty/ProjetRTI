package Swing;

import Controller.Controller;
import Modele.Utilisateur;

import javax.swing.*;
import java.io.IOException;

public class Connexion extends JFrame {
    private JPanel connectionPanel;
    public JTextField Name;
    private JButton loginButton;
    private JButton subscribeButton;
    public JPasswordField password;
    public JLabel Error;
    private JButton Reconnect;

    public JButton getLoginButton(){return loginButton;}

    public JButton getSubscribeButton(){return subscribeButton;}

    public JButton getReconnect(){return Reconnect;}

    public Connexion(Controller controller) {
        setContentPane(connectionPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Client - Connexion");
        setSize(500,200);

        subscribeButton.addActionListener(controller);
        loginButton.addActionListener(controller);
        Reconnect.addActionListener(controller);

        //Connection au serveur
        try {
            Utilisateur.getInstance().connect();
            setVisible(true);
            Utilisateur.getInstance().conect = true;
            Reconnect.setVisible(false);
        } catch (IOException e) {

            setVisible(true);
            if(Utilisateur.getInstance().conect == false)
            {
                Reconnect.setVisible(true);
                Error.setText("Connexion au serveur ratee !");
            }
            //throw new RuntimeException(e);
        }
    }
}
