package Modele.ProtocoleSecurise;



import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.ResultSet;
import java.sql.SQLException;


import Modele.Protocole.Paiement.ReponsePaiement;
import Modele.Protocole.Protocole;
import Modele.ProtocoleSecurise.Facture.ReponseGetFactureS;
import Modele.ProtocoleSecurise.Facture.ReponseGetFactureSer;
import Modele.ProtocoleSecurise.Facture.ReponseGetFactureTabS;
import Modele.ProtocoleSecurise.Facture.RequeteGetFactureS;
import Modele.ProtocoleSecurise.LOGOUT.RequeteLOGOUT;
import Modele.ProtocoleSecurise.Login.ReponseLOGINS;
import Modele.ProtocoleSecurise.Login.RequeteLOGINS;
import Modele.ProtocoleSecurise.Paiement.ReponsePaiementS;
import Modele.ProtocoleSecurise.Paiement.RequetePaiementS;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;

// protocole securisé
public class VESPAPS extends Protocole {

    private SecretKey cleSession ;
    public VESPAPS() throws SQLException, IOException, ClassNotFoundException {
        super() ;
    }

    public static void main(String[] args) {
         Security.addProvider(new BouncyCastleProvider());

        Provider p[] = Security.getProviders();
        for (int i = 0 ; i< p.length;i++)
        {
            System.out.println(p[i]);
        }
    }

    public synchronized Reponse TraiteRequeteS(Requete requete, Socket socket) throws
            FinConnexionException, SQLException, ClassNotFoundException, IOException, NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, IllegalBlockSizeException, CertificateException, KeyStoreException, BadPaddingException, InvalidKeyException, Modele.Protocole.FinConnexionException, SignatureException {
        if (requete instanceof RequeteLOGINS)
        {
            return TraiteRequeteLOGINS((RequeteLOGINS) requete, socket);
        }
        if (requete instanceof RequeteLOGOUT)  TraiteRequeteLOGOUTS((RequeteLOGOUT) requete);

        if(requete instanceof RequeteGetFactureS) return TraiteRequeteGetFactureS((RequeteGetFactureS)requete) ;
        if(requete instanceof RequetePaiementS) return TraiteRequetePaiementS((RequetePaiementS)requete) ;



        return null;
    }

    private synchronized ReponsePaiementS TraiteRequetePaiementS(RequetePaiementS requete) throws
            Modele.Protocole.FinConnexionException, SQLException, IOException, ClassNotFoundException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, NoSuchProviderException, InvalidKeyException, CertificateException, KeyStoreException {
        System.out.println("dans ReponsePaiementSecurisé ");
        //todo decrypté le msg

        byte [] messageDecrypte = MyCrypto.DecryptSymDES(cleSession,requete.getData());
        System.out.println("Decryptage du message...");

        // Récupération des données claires
        ByteArrayInputStream bais = new ByteArrayInputStream(messageDecrypte);
        DataInputStream dis = new DataInputStream(bais);
        int idFacture = dis.readInt();
        String nom = dis.readUTF();
        String visa = dis.readUTF();
        System.out.println("----serveur :   idfacture = "+idFacture +"nom =  " + nom +"visa = "+visa    );

        if(isCarteValid(visa))
        {
            String tmp[] = new String[1];
            tmp[0] = "idfacture = \'" + idFacture + "\'";
            int rs = donnees.update("factures", "paye = true",tmp) ;
            if(rs!=0)
            {
                return new ReponsePaiementS(true , cleSession);
            }
        }

        return new ReponsePaiementS(false,cleSession);

    }

    public synchronized ReponseGetFactureSer TraiteRequeteGetFactureS(RequeteGetFactureS requete) throws SQLException, CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, SignatureException, NoSuchProviderException, InvalidKeyException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos) ;

        if(!requete.VerifierSignature(RecupereClePubliqueClient()))
        {
            System.out.println(" [SERVEUR ] signature non validé ");
            ReponseGetFactureTabS rep = new ReponseGetFactureTabS(false );
            oos.writeObject(rep);
            return new ReponseGetFactureSer( MyCrypto.CryptSymDES(cleSession,bos.toByteArray()));
        }
        System.out.println(" [SERVEUR ] signature  validé ");
        ReponseGetFactureTabS reponse = new ReponseGetFactureTabS(true);
        System.out.println(" [protocole] dans Reponse GETFACTURE" );
        String[] condition1 = new String[1];
        condition1[0] = "username = \'" + requete.getIdclient() + "\'" ;
        ResultSet rss = donnees.selection(null, "clients", condition1);
        rss.next() ;
        System.out.println("reponse de la selection a partir de usrname" + rss.getString("id"));

            // todo essaye ne mettant directement en cond que idfacture = id actuel
        ResultSet rs = donnees.selection(null, "factures", null);
        while(rs.next())
        {

            if(rs.getString("idclient").equals(rss.getString("id")) )
            {
                System.out.println(rs.getString("idclient"));
                float total = 0 ;
                String[] condition = new String[1];
                condition[0] = "idfacture = " + rs.getString("idfacture") ;
                ResultSet s = donnees.selection(null, "articlesachetes", condition);
                while(s.next())
                {
                    total+=( Float.parseFloat(s.getString("prix")) * Float.parseFloat(s.getString("quantite")));
                }
                boolean b = false ;
                if(rs.getString("paye").equals("1")) b=true  ;
                ReponseGetFactureS rep = new ReponseGetFactureS( rs.getString("idfacture"), rs.getString("date"),b,Float.toString(total));
                reponse.addFacture(rep);
            }

        }

        oos.writeObject(reponse);
        return new ReponseGetFactureSer( MyCrypto.CryptSymDES(cleSession,bos.toByteArray()));
    }

    private void TraiteRequeteLOGOUTS(RequeteLOGOUT requete) throws Modele.Protocole.FinConnexionException {
        System.out.println("dans ReponseLOGOUT");
        getClientsConnectes().remove(requete.getLogin());
        System.out.println("apres retirer clientconnecte");
        throw new Modele.Protocole.FinConnexionException(null);
    }


    private synchronized ReponseLOGINS TraiteRequeteLOGINS(RequeteLOGINS requete, Socket socket) throws FinConnexionException, SQLException, ClassNotFoundException, IOException, NoSuchAlgorithmException, NoSuchProviderException, CertificateException, KeyStoreException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {

        System.out.println(" [protocole] dans ReponseLOGIN  avec user = "+requete.getLogin());

        boolean trouve = false  ;

        ResultSet rs = getDonnees().selection(null, "clients", null);

        while(rs.next())
        {
            if(rs.getString("username").equals(requete.getLogin()))
            {
                System.out.println("meme username , comparaison des digest");
                MessageDigest md = MessageDigest.getInstance("SHA-1","BC");
                md.update(requete.getLogin().getBytes());
                md.update(rs.getString("password").getBytes());
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(baos);
                dos.writeLong(requete.getD().getTime());
                dos.writeDouble(requete.getAlea());
                md.update(baos.toByteArray());
                byte[] digestLocal = md.digest();


                 if(MessageDigest.isEqual(requete.getDigest(),digestLocal))
                {
                    System.out.println("--connexion etablie protocole serveur securise et le digest");
                    trouve = true ;
                    System.out.println("generation cle de sess ");
                    KeyGenerator cleGen = KeyGenerator.getInstance("DES","BC");
                    cleGen.init(new SecureRandom());
                     cleSession = cleGen.generateKey();
                    System.out.println("Génération d'une clé de session : " + cleSession);
                    break ;
                }




            }

        }
        System.out.println("je passe apres rs next");

        if(trouve==true)
        {
            getClientsConnectes().put(requete.getLogin(), socket);
            return new ReponseLOGINS(true ,MyCrypto.CryptAsymRSA(RecupereClePubliqueClient(),cleSession.getEncoded()));
        }
        else
        {
            throw new FinConnexionException((Reponse) new ReponseLOGINS(false));
        }

    }


    public static PublicKey RecupereClePubliqueClient() throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {

        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("KeystoreServeur.jks"),"Serveur".toCharArray());
        X509Certificate certif = (X509Certificate)ks.getCertificate("Client");
        PublicKey cle = certif.getPublicKey();
        return cle;
    }



}
