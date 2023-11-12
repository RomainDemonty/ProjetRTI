package Vue.Application;

import javax.swing.*;
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
    private JPanel panelFacture;
    private JLabel errorVisa;


    public Application(Controller c )
    {

        setContentPane(contentPane);

        // abonnement au controller
        logoutButton.addActionListener(c);
        voirFacturesButton.addActionListener(c);
        confirmer.addActionListener(c);
        panelFacture = new JPanel();
        addWindowListener((WindowListener) c);
        idclient.setText("1");
        nomVISA.setText("cedric");
        numeroVISA.setText("4532759562301283"); // termine par 4 = valide , remplace le derneir chiffre pour montre invalide

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
    public JPanel getPanelFacture()
    {
        return panelFacture ;
    }
    public void resetPanelFacture()
    {
        panelFacture = new JPanel() ;
    }
    public JButton getLogoutButton() {
        return logoutButton;
    }
    public JButton getVoirFacturesButton()
    {
        return voirFacturesButton ;
    }

    public String getNumeroVISA() {
        return numeroVISA.getText();
    }

    public String getNomVISA() {
        return nomVISA.getText();
    }

    public String getIdclient() {
        return idclient.getText();
    }

    public void setErrorVisa(String t) {
        this.errorVisa.setText(t);
    }
}
