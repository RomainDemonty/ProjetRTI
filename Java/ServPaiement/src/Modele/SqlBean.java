package Modele;

import java.sql.*;
import java.util.Hashtable;
public class SqlBean
{
    private Connection connection;

    private static Hashtable<String,String> drivers;

    static
    {
        drivers = new Hashtable<>();
        drivers.put("MySql","com.mysql.cj.jdbc.Driver");
    }

    public SqlBean(String type,String server,String dbName,String  user,String password) throws ClassNotFoundException, SQLException
    {
        // Chargement du driver
        Class leDriver = Class.forName(drivers.get(type));
        System.out.println("Obtention du driver OK...");

        // Création de l'URL
        String url =  "jdbc:mysql://" + server + "/" + dbName;

        System.out.println("creation de l'url : "+ url);
        // Connexion à la BD
        connection = DriverManager.getConnection(url,user,password);
        System.out.println("Obtention de connection OK...");
    }

    public  synchronized ResultSet executeQuery(String sql) throws SQLException
    {
        Statement statement = connection.createStatement();
        return statement.executeQuery(sql);
    }

    public synchronized int executeUpdate(String sql) throws SQLException
    {
        Statement statement = connection.createStatement();
        return statement.executeUpdate(sql);
    }

    public synchronized void close() throws SQLException
    {
        if (connection != null && !connection.isClosed())
            connection.close();
    }
}

