package Modele.ProtocoleSecurise.Login;

import Modele.ProtocoleSecurise.Requete;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.*;
import java.util.Date;


public class RequeteLOGINS implements Requete {



    private String login;
    private Date d ;
    private double alea ;
    private byte[] digest;
    public RequeteLOGINS(String l, String p) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
        login = l;
        d = new Date();
        alea =  Math.random()%10;
        System.out.println("vos parametre de requete login sont  :  "+login + d + "  " + alea  );
        MessageDigest md =MessageDigest.getInstance("SHA-1","BC") ;
        md.update(login.getBytes());
        md.update(p.getBytes());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeLong(d.getTime());
        dos.writeDouble(alea);
        md.update(baos.toByteArray());
        digest = md.digest();
        System.out.println("votre digest est : "+digest);

    }

    public String getLogin() {
        return login;
    }

    public double getAlea() {
        return alea;
    }

    public Date getD() {
        return d;
    }

    public byte[] getDigest() {
        return digest;
    }
}