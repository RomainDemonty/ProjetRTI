import Modele.BD.AccesBD;
import Vue.ServeurVue;

import Controller.Controller ;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {
        AccesBD a = new AccesBD();
        System.out.println("creation de l'objet accesBD ok ... ");
        String att[] = new String[5] ;
        att[0]="intitule" ;
        att[1]="stock" ;

        int b = a.update("articles", "stock = 20", null);

        ResultSet rs = a.selection(att, "articles", null);
        while(rs.next())
        {
            System.out.println(rs.getString("intitule")+rs.getString("stock"));
        }

        Controller c = new Controller();
    }
}