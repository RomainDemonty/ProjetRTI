package Swing;

import Controller.Controller;

import javax.swing.*;
import java.awt.*;

public class Home extends JFrame {
    private JPanel mainPanel;
    private JPanel connectionPanel;
    private JPanel MarketPannel;
    private JButton logoutButton;
    private JPanel BagPannel;
    private JPanel imagePannel;
    private JTextField articleTextField;
    private JTextField priceTextField;
    private JTextField stockTextField;
    private JSpinner quantitySpinner;
    private JButton leftButton;
    private JButton rightButton;
    private JButton chooseButton;
    private JScrollPane bagScrollPane;
    private JButton deleteArticleButton;
    private JButton buyButton;
    private JButton deleteBagButton;
    private JList list1;
    private JLabel namelabel;
    private JLabel Total;

    public JButton getLogoutButton() {
        return logoutButton;
    }
    public JButton getLeftButton() {return leftButton;}

    public JButton getRightButton(){return rightButton;}

    public  Home(Controller controller) {

        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Client");
        setSize(800,600);
        // debut du code
        // Chargez une image à partir d'un fichier
        ImageIcon imageIcon = new ImageIcon(System.getProperty("user.dir")+ "\\image\\"+"ail"+".jpg");
        System.out.println(System.getProperty("user.dir")+ "/image"+"ail"+".jpg");

        // Créez un composant JLabel pour afficher l'image
        JLabel imageLabel = new JLabel(imageIcon);
        imagePannel.setLayout(new GridLayout(1, 1));
        imagePannel.add(imageLabel);


        logoutButton.addActionListener(controller);
        leftButton.addActionListener(controller);
        rightButton.addActionListener(controller);
        // fin du code
        setVisible(true);
    }
}
