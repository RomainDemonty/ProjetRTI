package Modele;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccesBD  {


    private SqlBean sql ;

    public AccesBD() throws SQLException, ClassNotFoundException {

        sql = new SqlBean("MySql",
                "192.168.0.55",
                "PourStudent",
                "Student",
                "PassStudent1_");
    }

    public ResultSet selection () throws SQLException {
        return sql.executeQuery("select * from articles;");
    }

}
