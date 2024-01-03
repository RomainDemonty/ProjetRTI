package serveurwebhtmlcss;

import com.sun.net.httpserver.*;
import java.io.*;
import java.net.URLDecoder;

import java.nio.charset.StandardCharsets;
import java.util.*;
public class HandlerFormulaire implements HttpHandler
{
    @Override
    public void handle(HttpExchange exchange) throws IOException
    {
        if (exchange.getRequestMethod().equalsIgnoreCase("POST"))
        {
            String requestMethod = exchange.getRequestMethod();
            String requestPath = exchange.getRequestURI().getPath();
            byte[] requestBody = exchange.getRequestBody().readAllBytes();
            String formData = new String(requestBody,
                    StandardCharsets.UTF_8);
            System.out.println("HandlerFormulaire (methode " + requestMethod
                    + ") = " + requestPath + " --" + formData + "--");
            Map<String, String> formDataMap = parseFormData(formData);

            // Traiter les données du formulaire

            int idArticle = Integer.parseInt(formDataMap.get("idArticle"));
            String nomArticle = formDataMap.get("nomArticle");
            float prixArticle = Float.parseFloat(formDataMap.get("prixArticle"));
            int quantiteArticle = Integer.parseInt(formDataMap.get("quantiteArticle"));

            String Reponse = "Bien marche";
            /*mettre a jour les données dans la bd et renvoyer une réponse adéquoite
            String Reponse = "Nom : " + nomArticle;
            Reponse += "\nidArticle : " + idArticle;
            Reponse += "\nprix : " + prixArticle;
            Reponse += "\nquantite : " + quantiteArticle;

            */

            exchange.sendResponseHeaders(200, Reponse.length());
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            OutputStream outputStream = exchange.getResponseBody();
            outputStream.write(Reponse.getBytes());
            outputStream.close();
        }
        else
        {
            exchange.sendResponseHeaders(405, 0); // Méthode non autorisée -POST requis
            exchange.close();
        }
    }
    private Map<String, String> parseFormData(String formData) throws
            UnsupportedEncodingException
    {
        // Parse les données du formulaire et les retourne sous forme de Map clé-valeur
        Map<String, String> formDataMap = new HashMap<>();
        String[] formDataPairs = formData.split("&");
        for (String pair : formDataPairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                String key = URLDecoder.decode(keyValue[0],
                        StandardCharsets.UTF_8.name());
                String value = URLDecoder.decode(keyValue[1],
                        StandardCharsets.UTF_8.name());
                formDataMap.put(key, value);
            }
        }
        return formDataMap;
    }
}
