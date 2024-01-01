package Modele.ProtocoleSecurise.Paiement;

import Modele.ProtocoleSecurise.Reponse;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

public class ReponsePaiementS implements Reponse {

    private boolean ok ;

    private byte[] hmac ;

    public ReponsePaiementS(boolean bool , SecretKey cleSess) throws IOException, InvalidKeyException, NoSuchAlgorithmException, NoSuchProviderException {
        ok=bool;
        Mac hm = Mac.getInstance("HMAC-MD5","BC");
        hm.init(cleSess);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeBoolean(ok);
        hm.update(baos.toByteArray());
        hmac = hm.doFinal();
    }

    public boolean VerifyAuthenticity(SecretKey cleSession) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IOException {

        Mac hm = Mac.getInstance("HMAC-MD5","BC");
        hm.init(cleSession);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeBoolean(ok);
        hm.update(baos.toByteArray());

        return MessageDigest.isEqual(hmac,hm.doFinal());
    }

    public boolean isOk() {
        return ok;
    }
}