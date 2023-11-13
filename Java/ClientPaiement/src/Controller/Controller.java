package Controller;

import Modele.Singleton;
import Vue.Application.Application;
import Vue.Connexion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;

public class Controller implements ActionListener , WindowListener {

   private Connexion c ;

   private Application app ;
    public Controller()
    {
        //TODO faire en sorte que si le serveur soir pas connecte que on pervienne
        c = new Connexion(this) ;
        app=new Application(this);
        app.setVisible(false);

    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==c.getSeConnecterButton())
        {
            System.out.println("bouton connexion ");
            try {
                if(Singleton.getInstance().envoyerRequeteLOGIN(c.getLogin(), c.getPassword()))
                {
                    app.setVisible(true);
                    c.setVisible(false);
                }
                else
                {
                    c.setError("erreur de connexion");
                }
            }catch (ConnectException ex )
            {
                c.setError("error de connexion au seveur");
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        }

        if(e.getSource()==app.getConfirmer()) {

            System.out.println("bouton confirmer ");

            JPanel tmpPanel = app.getPanelFacture();
            for (Component check : tmpPanel.getComponents()) {
                if (check instanceof JCheckBox) {
                    JCheckBox checkBox = (JCheckBox) check;
                    if (checkBox.isSelected()) {
                        for (int i = 0; i < Singleton.getInstance().getFactures().size(); i++) {
                            if (checkBox.getText().equals(Singleton.getInstance().getFactures().get(i).toString())) {

                                try {
                                    if(Singleton.getInstance().envoyerRequetePayer(Singleton.getInstance().getFactures().get(i).getIdFacture(), app.getNomVISA(), app.getNumeroVISA()))
                                    {
                                        Singleton.getInstance().getFactures().remove(Singleton.getInstance().getFactures().get(i));
                                        tmpPanel.remove(checkBox);
                                        app.setErrorVisa("");
                                    }
                                    else
                                    {
                                        app.setErrorVisa("Numero de visa invalid ! ");
                                    }
                                }catch (SocketException ex )
                                {
                                    app.setVisible(false);
                                    c.setVisible(true);
                                    try {
                                        Singleton.getInstance().getCsocket().close();
                                    } catch (IOException exc) {
                                        throw new RuntimeException(exc);
                                    }
                                    c.setError("error de connexion au seveur");
                                }
                                catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                } catch (ClassNotFoundException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        }
                    }
                }
            }
        }

        if(e.getSource()==app.getLogoutButton())
        {
            System.out.println("bouton deconnexion ");
            try {
                Singleton.getInstance().envoyerRequeteLOGOUT();
            }
            catch (SocketException ex )
            {
                app.setVisible(false);
                c.setVisible(true);
                try {
                    Singleton.getInstance().getCsocket().close();
                } catch (IOException exc) {
                    throw new RuntimeException(exc);
                }
                c.setError("error de connexion au seveur");
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }
            app.setVisible(false);
            c.setVisible(true);
        }

        if(e.getSource()==app.getVoirFacturesButton())
        {
            System.out.println("bouton pour voir les factures ");
            try {
                Singleton.getInstance().envoyerRequeteGetFactures(app.getIdclient());
            }
            catch (SocketException ex )
            {
                app.setVisible(false);
                c.setVisible(true);
                try {
                    Singleton.getInstance().getCsocket().close();
                } catch (IOException exc) {
                    throw new RuntimeException(exc);
                }
                c.setError("error de connexion au seveur");
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            }

            app.resetPanelFacture();
            app.getPanelFacture().setLayout(new GridLayout(0, 1));
            for (int i = 0 ; i<Singleton.getInstance().getFactures().size() ; i++)
            {
                app.getPanelFacture().add(new JCheckBox(Singleton.getInstance().getFactures().get(i).toString())) ;
            }
            app.getFactureScrollPane().setViewportView(app.getPanelFacture());



        }

        c.repaint();
        c.revalidate();
        app.repaint();
        app.revalidate();
    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("bouton deconnexion ");
        try {
            Singleton.getInstance().envoyerRequeteLOGOUT();
        }
        catch (SocketException ex )
        {
            app.setVisible(false);
            c.setVisible(true);
            try {
                Singleton.getInstance().getCsocket().close();
            } catch (IOException exc) {
                throw new RuntimeException(exc);
            }
            c.setError("error de connexion au seveur");
        }catch (IOException ex) {
            throw new RuntimeException(ex);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
