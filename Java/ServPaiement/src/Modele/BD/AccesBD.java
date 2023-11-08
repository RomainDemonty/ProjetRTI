package Modele.BD;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AccesBD  {


    private SqlBean sql ;

    public AccesBD() throws SQLException, ClassNotFoundException {

        sql = new SqlBean("MySql",
                "192.168.0.14",
                "PourStudent",
                "Student",
                "PassStudent1_");
    }

    public ResultSet selection (String[] attributs , String table , String[] conditions) throws SQLException {

        String requete= "SELECT ";
        if(attributs==null)
            requete+= " * " ;
        else
        {
            for (int i = 0; attributs[i] != null; i++) {
                if(i!=0)requete+= " , " ;

                requete+= attributs[i] ;
            }
        }

        requete += " FROM "+ table ;

       if(conditions!=null)
       {
           requete += " WHERE " ;
           for (int i = 0; conditions[i] != null; i++) {
               if(i!=0)requete+= " AND  " ;

               requete+= conditions[i] ;
           }
       }

        requete+= ";";
        System.out.println(requete );
        ResultSet rs = sql.executeQuery(requete);
        return rs ;
    }
    public int update ( String table, String modif, String[] conditions) throws SQLException {

        String requete= "UPDATE ";
        requete +=  table +" SET " + modif ;

        if(conditions!=null)
        {
            requete += " WHERE " ;
            for (int i = 0; conditions[i] != null; i++) {
                if(i!=0)requete+= " AND  " ;

                requete+= conditions[i] ;
            }
        }

        requete+= ";";
        System.out.println(requete );
        int rs = sql.executeUpdate(requete);
        return rs ;
    }
}
