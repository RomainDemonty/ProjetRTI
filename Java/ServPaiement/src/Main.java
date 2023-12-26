import Modele.BD.AccesBD;
import Vue.ServeurVue;

import Controller.Controller ;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.security.Security;
import java.sql.ResultSet;
import java.sql.SQLException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {

        Controller c = new Controller();
        Security.addProvider(new BouncyCastleProvider());
    }
}