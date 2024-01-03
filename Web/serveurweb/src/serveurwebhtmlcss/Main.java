package serveurwebhtmlcss;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.sql.SQLException;

public class Main
{

    public static void main(String[] args)
    {

        HttpServer serveur = null;
        HttpServer servApi = null;
        try
        {
            serveur = HttpServer.create(new InetSocketAddress(8080),0);
            serveur.createContext("/",new HandlerHtml());
            serveur.createContext("/css",new HandlerCss());
            serveur.createContext("/image",new HandlerImages());
            serveur.createContext("/image/logo",new HandlerPng());
            serveur.createContext("/app",new HandlerJs());
            //serveur.createContext("/FormArticle", new HandlerFormulaire());

            servApi = HttpServer.create(new InetSocketAddress(8081),0);
            servApi.createContext("/api/tasks", new TodoApi.TaskHandler());
            System.out.println("Demarrage du serveur HTTP...");
            System.out.println("Demarrage du serveurApi HTTP...");

            serveur.start();
            servApi.start();
        }
        catch (IOException e)
        {
            System.out.println("Erreur: " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}