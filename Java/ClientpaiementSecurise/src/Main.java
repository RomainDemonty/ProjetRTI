import Controller.Controller;
import Modele.ProtocoleSecurise.Login.RequeteLOGINS;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

public class Main {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {

        Security.addProvider(new BouncyCastleProvider());

        Controller controller = new Controller();
    }

}