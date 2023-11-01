import Modele.AccesBD;

import java.sql.ResultSet;
import java.sql.SQLException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        AccesBD a = new AccesBD();
        System.out.println("creation de l'objet accesBD ok ... ");
        ResultSet rs = a.selection();
        while(rs.next())
        {
            System.out.println(rs.getString("intitule"));
        }


    }
}