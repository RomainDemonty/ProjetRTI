package Vue;

import javax.swing.*;

public class Serveur extends JFrame {


    private JPanel contentPane;
    private JButton buttonLancer;
    private JTextField port;

    Serveur()
    {
        setContentPane(contentPane);

        port.setText("50000");

        setSize(400,300);
        setVisible(true );


    }

    public static void main(String[] args) {
        Serveur s = new Serveur();
    }
}
