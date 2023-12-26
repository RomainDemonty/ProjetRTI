package Modele;

import Modele.ProtocoleSecurise.LOGOUT.RequeteLOGOUT;
import Modele.ProtocoleSecurise.Login.ReponseLOGINS;
import Modele.ProtocoleSecurise.Login.RequeteLOGINS;
import Modele.ProtocoleSecurise.Facture.*;
import Modele.ProtocoleSecurise.MyCrypto;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Properties;


public class Singleton {
    private static Singleton instance;
    ArrayList<ReponseGetFactureS> factures ;

    static {
        try {
            instance = new Singleton();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Singleton getInstance() {
        return instance;
    }

    private String numVISA ;
    private String nomVISA ;
    private String idClient ;
    private String nomEmploye ;
    private String mdpEmploye;

    Socket csocket;

    // Création de la socket et connexion sur le serveur

    private SecretKey cleSession ;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;


    private Singleton() throws IOException {

        oos =null ;
        ois = null  ;

    }





    public boolean envoyerRequeteLOGIN(String u , String mdp) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchProviderException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        Properties P = new Properties() ;
        P.load(new FileInputStream(System.getProperty("user.dir")+"\\properties.properties"));

        System.out.println("avant csocket ");
        csocket = new Socket(P.getProperty("ipAdress"),Integer.parseInt(P.getProperty("socket")));
        System.out.println("apres csocket  -> valeur : " +csocket);

        oos = new ObjectOutputStream(csocket.getOutputStream());
        ois = new ObjectInputStream(csocket.getInputStream());

        RequeteLOGINS requete = new RequeteLOGINS(u,mdp);
        System.out.println("avant l'ecriture de requete");
        oos.writeObject(requete);

        ReponseLOGINS reponse = (ReponseLOGINS) ois.readObject();
        System.out.println("reponse du serveur pour login : " + reponse.isValide());
        if (reponse.isValide())
        {
            System.out.println("connexion confirmée");
            idClient =  u  ;
            byte[] cleSessionDecryptee;
            System.out.println("Clé session cryptée reçue = " + new
                    String(reponse.getCleSession()));
            cleSessionDecryptee =
                    MyCrypto.DecryptAsymRSA(RecupereClePriveeClient(),reponse.getCleSession());
             cleSession = new SecretKeySpec(cleSessionDecryptee,"DES");
            System.out.println("Decryptage asymétrique de la clé de session...");
            System.out.println(cleSession.toString());

            return true  ;
        }
        else {

            csocket.close();
            return false  ;

        }

    }
    public void envoyerRequeteLOGOUT() throws IOException, ClassNotFoundException {

        RequeteLOGOUT requete = new RequeteLOGOUT(idClient);
        oos.writeObject(requete);
        csocket.close();

    }


    public boolean envoyerRequeteGetFactures(String id) throws IOException, ClassNotFoundException, UnrecoverableKeyException, CertificateException, KeyStoreException, NoSuchAlgorithmException, SignatureException, NoSuchProviderException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        factures=new ArrayList<>() ;
        //todo signer la requete pour assurer que on est bien nous meme
        RequeteGetFactureS requete = new RequeteGetFactureS(id , RecupereClePriveeClient());
        oos.writeObject(requete);

        //todo decrypte la lise des facture symetriquement ( avec cle session)
        ReponseGetFactureSer rep = (ReponseGetFactureSer) ois.readObject();

        byte[] messagedecrypte =  MyCrypto.DecryptSymDES(cleSession,rep.getData());
        ByteArrayInputStream bis = new ByteArrayInputStream(messagedecrypte);
        ObjectInputStream ois = new ObjectInputStream(bis) ;
        ReponseGetFactureTabS reponse = (ReponseGetFactureTabS) ois.readObject();

        if(!reponse.isSignIsValide())
        {
            System.out.println("---signature invalide !---");
            return false ;
        }


        for(int i = 0 ; i<reponse.getTabFactures().size();i++)
        {
            if(reponse.getTabFactures().get(i).isPaye()==false)
            {
                System.out.println(reponse.getTabFactures().get(i).getMontant());
                factures.add(reponse.getTabFactures().get(i)) ;
            }

        }
        if(reponse.getTabFactures().size()!=0)
        {
            return true ;
        }
        else return false ;

    }

    public static PrivateKey RecupereClePriveeClient() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {

        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new
                FileInputStream("KeystoreClient.jks"), "Client".toCharArray());
        PrivateKey cle = (PrivateKey)
                ks.getKey("Client", "Client".toCharArray());
        return cle;
    }



    public Socket getCsocket() {
        return csocket;
    }

    public ArrayList<ReponseGetFactureS> getFactures()
    {
        return factures ;
    }

    public String getIdClient() {
        return idClient;
    }
}
