package Controller;

import Modele.ProtocoleSecurise.Facture.ReponseGetFactureS;
import Modele.Singleton;
import Vue.Application.Application;
import Vue.Connexion;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;

public class Controller implements ActionListener , WindowListener {

    private Connexion  c   ;
    private Application app ;

    public Controller()
    {
        c = new Connexion(this) ;
        app=new Application(this);
        app.setVisible(false);
    }
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==c.getSeConnecterButton())
        {
            System.out.println("bouton connecter");
            try {
                if(Singleton.getInstance().envoyerRequeteLOGIN(c.getLogin(),c.getPassword()))
                {
                    app.setVisible(true);
                    c.setVisible(false);
                }
                else {
                    c.setError("erreur de connexion");
                }
            }
            catch (ConnectException ex )
            {
                c.setError("error de connexion au seveur");
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (ClassNotFoundException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchProviderException ex) {
                throw new RuntimeException(ex);
            } catch (UnrecoverableKeyException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchPaddingException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalBlockSizeException ex) {
                throw new RuntimeException(ex);
            } catch (CertificateException ex) {
                throw new RuntimeException(ex);
            } catch (KeyStoreException ex) {
                throw new RuntimeException(ex);
            } catch (BadPaddingException ex) {
                throw new RuntimeException(ex);
            } catch (InvalidKeyException ex) {
                throw new RuntimeException(ex);
            }

        }

        if(e.getSource()==app.getConfirmer()) {
            System.out.println("bouton Acheter ");
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
            System.out.println("bouton voir factures ");
            try {
                Singleton.getInstance().envoyerRequeteGetFactures(Singleton.getInstance().getIdClient());
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
            } catch (UnrecoverableKeyException ex) {
                throw new RuntimeException(ex);
            } catch (CertificateException ex) {
                throw new RuntimeException(ex);
            } catch (KeyStoreException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException(ex);
            } catch (SignatureException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchProviderException ex) {
                throw new RuntimeException(ex);
            } catch (InvalidKeyException ex) {
                throw new RuntimeException(ex);
            } catch (NoSuchPaddingException ex) {
                throw new RuntimeException(ex);
            } catch (IllegalBlockSizeException ex) {
                throw new RuntimeException(ex);
            } catch (BadPaddingException ex) {
                throw new RuntimeException(ex);
            }

            app.resetPanelFacture();
            app.getPanelFacture().setLayout(new GridLayout(0, 1));
            System.out.println("avant boucle facture");
            for (int i = 0 ; i<Singleton.getInstance().getFactures().size() ; i++)
            {
                System.out.println(Singleton.getInstance().getFactures().get(i).toString());
                app.getPanelFacture().add(new JCheckBox(Singleton.getInstance().getFactures().get(i).toString())) ;
            }
            app.getFactureScrollPane().setViewportView(app.getPanelFacture());
            System.out.println("apres boucle facture");
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
