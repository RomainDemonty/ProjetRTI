package Vue;

import javax.swing.*;
import Controller.Controller ;

public class Connexion extends JFrame {
    private JTextField login;
    private JPasswordField passwordField;
    private JButton seConnecterButton;
    private JPanel contentPane;

    public Connexion(Controller c )
   {
        setContentPane(contentPane);
        login.setText("admin");
       passwordField.setText("admin");

       seConnecterButton.addActionListener(c);
       setSize(800, 300);
       setVisible(true);
   }
   public JButton getSeConnecterButton()
   {
       return seConnecterButton ;
   }

    public String getLogin() {
        return login.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }
}
