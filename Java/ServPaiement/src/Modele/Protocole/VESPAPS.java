package Modele.Protocole;

import Modele.Protocole.Protocole;

import java.io.IOException;
import java.security.*;
import java.sql.SQLException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

//todo cree les classes en securisé
// protocole securisé
public class VESPAPS extends Protocole {
    public VESPAPS() throws SQLException, IOException, ClassNotFoundException {
    }

    //TODO override les fonctiosn utilise en implementatnt un systeme de securité
    public static void main(String[] args) {
         Security.addProvider(new BouncyCastleProvider());

        Provider p[] = Security.getProviders();
        for (int i = 0 ; i< p.length;i++)
        {
            System.out.println(p[i]);
        }
    }

}
