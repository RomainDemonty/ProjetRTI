package Vue.Application;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import Controller.Controller ;
public class Application extends JFrame {
    private JButton confirmer;
    private JTextField nomVISA;
    private JTextField numeroVISA;
    private JButton logoutButton;
    private JTextField idclient;
    private JScrollPane factureScrollPane;
    private JButton voirFacturesButton;
    private JPanel contentPane;


    public Application(Controller c )
    {

        setContentPane(contentPane);
        // A enlever

        //jusque ici

        // abonnement au controller
        logoutButton.addActionListener(c);
        voirFacturesButton.addActionListener(c);
        confirmer.addActionListener(c);


        addWindowListener((WindowListener) c);

        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 300);
        setVisible(true);
    }

    public JButton getConfirmer() {
        return confirmer;
    }

    public JScrollPane getFactureScrollPane()
    {
        return factureScrollPane ;
    }
    public JButton getLogoutButton() {
        return logoutButton;
    }
    public JButton getVoirFacturesButton()
    {
        return voirFacturesButton ;
    }

    public JTextField getNumeroVISA() {
        return numeroVISA;
    }

    public JTextField getNomVISA() {
        return nomVISA;
    }

    public JTextField getIdclient() {
        return idclient;
    }
}
