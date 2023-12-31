package Swing;

import Controller.Controller;
import Modele.Article;
import Modele.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Home extends JFrame {
    private JPanel mainPanel;
    private JPanel connectionPanel;
    private JPanel MarketPannel;
    private JButton logoutButton;
    private JPanel BagPannel;
    private JPanel imagePannel;
    public JSpinner quantitySpinner;
    private JButton leftButton;
    private JButton rightButton;
    private JButton addBagButton;
    private JButton deleteArticleButton;
    private JButton buyButton;
    private JButton deleteBagButton;
    private JLabel Total;
    private JLabel caseNom;
    private JLabel casePrix;
    private JLabel caseStock;
    private JLabel image;
    private JScrollPane scrollPanebag;
    public JLabel MessageErreur;

    public JButton getLogoutButton() {
        return logoutButton;
    }
    public JButton getLeftButton() {return leftButton;}

    public JButton getRightButton(){return rightButton;}

    public JButton getAddBagButton(){return addBagButton;}

    public JButton getDeleteBagButton() {return deleteBagButton;}

    public JButton getDeleteArticleButton() {return deleteArticleButton;}

    public JButton getBuyButton() {return buyButton;}

    public  Home(Controller controller) {

        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Client");
        setSize(800,600);


        logoutButton.addActionListener(controller);
        leftButton.addActionListener(controller);
        rightButton.addActionListener(controller);
        addBagButton.addActionListener(controller);
        deleteBagButton.addActionListener(controller);
        deleteArticleButton.addActionListener(controller);
        buyButton.addActionListener(controller);

        //Connection au serveur
        try {
            //Utilisateur.getInstance().connect();
            //Utilisateur.getInstance().login();
            Utilisateur.getInstance().consult();
            setArticle(Utilisateur.getInstance().articleSelect);
            setVisible(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setArticle(Article art) throws IOException {
        caseNom.setText(art.getIntitule());
        casePrix.setText(String.valueOf(art.getPrix()));
        caseStock.setText(String.valueOf(art.getQuantite()));

        String nouvelImagePath = "image/" + art.getAdrImage();
        ImageIcon nouvelleImageIcon = new ImageIcon(nouvelImagePath);
        image.setIcon(nouvelleImageIcon);
        //System.out.println("Test " + art.getIntitule());
    }

    public void setBagPannel(){
        ButtonGroup buttonGroup = new ButtonGroup();
        JRadioButton temp;

        JPanel bagPanel = new JPanel();
        bagPanel.setLayout(new GridLayout(0, 1));
        for (int i = 0 ; i<Utilisateur.getInstance().getMonPanier().size();i++)
        {
            temp = new JRadioButton(Utilisateur.getInstance().getMonPanier().get(i).toStringBag());
            bagPanel.add(temp);
            buttonGroup.add(temp);
        }
        scrollPanebag.setViewportView(bagPanel);
        Total.setText("Total : "+ String.valueOf(Utilisateur.getInstance().getTotal()) + "€");
    }

    public int getArtSelect(){
        int numArt = -1;

        JPanel tmpPanel = (JPanel) scrollPanebag.getViewport().getView();
        if(tmpPanel == null)
        {
            return numArt;
        }

        for (Component c : tmpPanel.getComponents()) {
            if (c instanceof JRadioButton) {
                JRadioButton checkRadio = (JRadioButton) c;
                if (checkRadio.isSelected()) {

                    for (int i = 0; i < Utilisateur.getInstance().getMonPanier().size(); i++) {
                        if (checkRadio.getText().equals(Utilisateur.getInstance().getMonPanier().get(i).toStringBag())) {
                           numArt = Utilisateur.getInstance().getMonPanier().get(i).getIdAliment();
                           return numArt;
                        }
                    }
                }

            }
        }

        return numArt;
    }
}
