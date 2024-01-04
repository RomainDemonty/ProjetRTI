package serveurwebhtmlcss;

import BD.AccesBD;
import com.sun.net.httpserver.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static serveurwebhtmlcss.TodoApi.TaskHandler.listeArticle;

public class TodoApi
{
    static class TaskHandler implements HttpHandler
    {
        public static List<Article> listeArticle = new ArrayList<>();

        private AccesBD accesBD ;

        TaskHandler() throws SQLException, IOException, ClassNotFoundException {
            accesBD = new AccesBD();
            ResultSet result = accesBD.selection(null , "articles" ,null );

            while (result.next()) {
                int id = result.getInt("id");
                String intitule = result.getString("intitule");
                float prix = result.getFloat("prix");
                int stock = result.getInt("stock");
                String image = result.getString("image");

                Article article = new Article(id, intitule, prix, stock, image);
                listeArticle.add(article);
            }
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException
        {
            // CORS (Cross-Origin Resource Sharing)
            exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
            exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
            exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "ContentType");

            String requestMethod = exchange.getRequestMethod();
            if (requestMethod.equalsIgnoreCase("GET"))
            {
                System.out.println("--- Requête GET reçue (obtenir la liste) ---");
                // Récupérer la liste des tâches au format JSON
                String response = convertListeArticleToJson();
                sendResponse(exchange, 200, response);
            }
            else if (requestMethod.equalsIgnoreCase("POST"))
            {
                System.out.println("--- Requête POST reçue (ajout) ---");
                // Ajouter une nouvelle tâche
                String requestBody = readRequestBody(exchange);
                System.out.println("requestbody = " + requestBody);

                // Analyser le corps de la requête pour extraire les données
                Map<String, String> formData = parseQueryParams(requestBody);
                // Récupérer les données de la requête
                int idArticle = Integer.parseInt(formData.get("id"));
                float nouveauPrix = Float.parseFloat(formData.get("prix"));
                int nouvelleQuantite = Integer.parseInt(formData.get("stock"));
                // Mettre à jour l'article
                try {
                    updateArticle(idArticle, nouveauPrix, nouvelleQuantite);
                    System.out.println("Stock mise a jour");
                    sendResponse(exchange, 201, "Article mise a jour");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            else sendResponse(exchange, 405, "Methode non autorisee");
        }
    }
    private static void sendResponse(HttpExchange exchange, int statusCode, String
            response) throws IOException
    {
        System.out.println("Envoi de la réponse (" + statusCode + ") : --" + response
                + "--");
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
    private static String readRequestBody(HttpExchange exchange) throws IOException
    {
        BufferedReader reader = new BufferedReader(new
                InputStreamReader(exchange.getRequestBody()));
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
        {
            requestBody.append(line);
        }
        reader.close();
        return requestBody.toString();
    }
    private static Map<String, String> parseQueryParams(String query)
    {
        Map<String, String> queryParams = new HashMap<>();
        if (query != null)
        {
            String[] params = query.split("&");
            for (String param : params)
            {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2)
                {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }
        return queryParams;
    }
    private static String convertListeArticleToJson() {
        // Convertir la liste des articles en format JSON
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < listeArticle.size(); i++) {
            Article article = listeArticle.get(i);
            json.append("{\"id\": ").append(article.getId())
                    .append(", \"intitule\":\"").append(article.getIntitule())
                    .append("\", \"prix\":").append(article.getPrix())
                    .append(", \"stock\":").append(article.getStock())
                    .append(", \"image\":\"").append(article.getImage()).append("\"}");

            if (i < listeArticle.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }
    private static void addTask(Article article)
    {
        listeArticle.add(article);
    }
    private static void updateArticle(int idUpdateArticle, float updatedPrix, int updatedStock) throws SQLException, IOException, ClassNotFoundException {
        if (idUpdateArticle >= 1 && idUpdateArticle <= listeArticle.size()) {
            Article articleToUpdate = listeArticle.get(idUpdateArticle - 1);
            articleToUpdate.setPrix(updatedPrix);
            articleToUpdate.setStock(updatedStock);
            AccesBD accesBD = new AccesBD();
            String[] condition = new String[]{"id =" + idUpdateArticle};
            String update = "prix = " + updatedPrix + ",stock = " + updatedStock;
            accesBD.update("articles" , update ,condition );
        }
    }

}
