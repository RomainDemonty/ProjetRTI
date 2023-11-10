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

    public JButton getLoginButton(){return loginButton;}

    public JButton getSubscribeButton(){return subscribeButton;}

    public Connexion(Controller controller) {
        setContentPane(connectionPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Client - Connexion");
        setSize(500,200);

        subscribeButton.addActionListener(controller);
        loginButton.addActionListener(controller);

        //Connection au serveur
        try {
            Utilisateur.getInstance().connect();
            setVisible(true);
        } catch (IOException e) {
            //Error.setText("Connection au serveur impossible !");
            setVisible(true);
            //throw new RuntimeException(e);
        }
    }
}
