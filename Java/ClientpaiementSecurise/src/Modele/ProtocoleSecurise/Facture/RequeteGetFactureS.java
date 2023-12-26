package Modele.ProtocoleSecurise.Facture;

import Modele.ProtocoleSecurise.Requete;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.*;

public class RequeteGetFactureS implements Requete {

    private String idclient ;
    private byte[] signature;

    public RequeteGetFactureS(String id , PrivateKey clePriveeClient) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IOException, SignatureException {
        idclient=id;
        Signature s = Signature.getInstance("SHA1withRSA","BC");
        s.initSign(clePriveeClient);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(id);
        s.update(baos.toByteArray());
        signature = s.sign();

    }

    public boolean VerifierSignature(PublicKey clePubliqueClient) throws NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, IOException, SignatureException {

        Signature s = Signature.getInstance("SHA1withRSA","BC");
        s.initVerify(clePubliqueClient);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        dos.writeUTF(idclient);
        s.update(baos.toByteArray());
        return s.verify(signature);
    }


    public String getIdclient() {
        return idclient;
    }
}

