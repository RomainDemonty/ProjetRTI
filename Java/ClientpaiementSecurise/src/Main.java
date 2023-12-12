import Controller.Controller;
import Modele.Protocole.Login.RequeteLOGIN;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {

        //todo dmd au prof si le provider est bien msi
        Security.addProvider(new BouncyCastleProvider());

        Controller controller = new Controller();
        new RequeteLOGIN("s","s");
    }

}