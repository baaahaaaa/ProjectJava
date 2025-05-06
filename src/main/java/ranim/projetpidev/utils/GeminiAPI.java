package ranim.projetpidev.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.sql.Date;

public class GeminiAPI {
    private static final String API_KEY = "9ebb1ab027msh0b93bc228506651p167151jsn692a2a632c4d";
    private static final String API_URL = "https://generate-text-ai-gemini.p.rapidapi.com/generateText";
    private static int requestCount = 0;
    private static final int MAX_REQUESTS_PER_MINUTE = 5;
    private static long lastRequestTime = 0;

    public static String generateCourseDescription(String courseTitle) {
        // Vérifier le rate limiting
        if (!canMakeRequest()) {
            return generateFallbackDescription(courseTitle);
        }

        try {
            HttpClient client = HttpClient.newHttpClient();
            
            // Construire le corps de la requête
            JSONObject requestBody = new JSONObject();
            requestBody.put("text", "Générer une description professionnelle et détaillée en français pour un cours intitulé : '" + 
                            courseTitle + "'. La description doit inclure les objectifs d'apprentissage et les points clés du cours.");
            requestBody.put("temperature", 0.7);
            requestBody.put("max_tokens", 800);
            
            System.out.println("Requête envoyée: " + requestBody.toString(2)); // Debug

            // Créer la requête HTTP avec les en-têtes RapidAPI
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("X-RapidAPI-Key", API_KEY)
                .header("X-RapidAPI-Host", "generate-text-ai-gemini.p.rapidapi.com")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody.toString()))
                .build();

            incrementRequestCount();

            // Envoyer la requête et obtenir la réponse
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            
            String responseBody = response.body();
            System.out.println("Code de réponse: " + response.statusCode()); // Debug
            System.out.println("Réponse reçue: " + responseBody); // Debug
            
            if (response.statusCode() == 429) {
                System.out.println("Rate limit atteint, utilisation du fallback");
                return generateFallbackDescription(courseTitle);
            }
            
            if (response.statusCode() == 403) {
                System.out.println("Problème d'authentification API, utilisation du fallback");
                return generateFallbackDescription(courseTitle);
            }
            
            // Parser la réponse
            try {
                JSONObject jsonResponse = new JSONObject(responseBody);
                
                if (response.statusCode() != 200) {
                    if (jsonResponse.has("message")) {
                        System.out.println("Erreur API: " + jsonResponse.getString("message"));
                        return generateFallbackDescription(courseTitle);
                    }
                    return generateFallbackDescription(courseTitle);
                }
                
                if (jsonResponse.has("generated_text")) {
                    return jsonResponse.getString("generated_text");
                }
                
                return generateFallbackDescription(courseTitle);
            } catch (JSONException e) {
                System.err.println("Erreur de parsing JSON: " + e.getMessage());
                return generateFallbackDescription(courseTitle);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return generateFallbackDescription(courseTitle);
        }
    }

    private static boolean canMakeRequest() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastRequestTime > 60000) { // Reset après 1 minute
            requestCount = 0;
            lastRequestTime = currentTime;
            return true;
        }
        return requestCount < MAX_REQUESTS_PER_MINUTE;
    }

    private static void incrementRequestCount() {
        requestCount++;
        if (requestCount == 1) {
            lastRequestTime = System.currentTimeMillis();
        }
    }

    private static String generateFallbackDescription(String courseTitle) {
        // Utiliser des templates prédéfinis basés sur le titre du cours
        String domain = detectDomain(courseTitle.toLowerCase());
        
        switch (domain) {
            case "java":
                return "Ce cours de Java offre une formation complète et pratique pour les développeurs.\n\n" +
                       "Objectifs d'apprentissage :\n" +
                       "- Maîtriser les fondamentaux de la programmation Java\n" +
                       "- Comprendre la programmation orientée objet\n" +
                       "- Développer des applications robustes et évolutives\n\n" +
                       "Points clés du cours :\n" +
                       "- Syntaxe et concepts de base Java\n" +
                       "- Classes, objets et héritage\n" +
                       "- Collections et gestion des exceptions\n" +
                       "- Interfaces graphiques avec JavaFX\n" +
                       "- Accès aux bases de données\n" +
                       "- Bonnes pratiques de développement";
            
            case "python":
                return "Ce cours de Python propose une approche pratique de la programmation.\n\n" +
                       "Objectifs d'apprentissage :\n" +
                       "- Maîtriser la syntaxe Python\n" +
                       "- Développer des applications modernes\n" +
                       "- Comprendre la programmation fonctionnelle\n\n" +
                       "Points clés du cours :\n" +
                       "- Fondamentaux Python\n" +
                       "- Structures de données\n" +
                       "- Programmation orientée objet\n" +
                       "- Gestion des fichiers et données\n" +
                       "- Frameworks web populaires\n" +
                       "- Tests et déploiement";
            
            default:
                return "Ce cours de " + courseTitle + " offre une formation professionnelle complète.\n\n" +
                       "Objectifs d'apprentissage :\n" +
                       "- Maîtriser les concepts fondamentaux\n" +
                       "- Développer des compétences pratiques\n" +
                       "- Appliquer les meilleures pratiques\n\n" +
                       "Points clés du cours :\n" +
                       "- Introduction aux concepts de base\n" +
                       "- Méthodologies modernes\n" +
                       "- Exercices pratiques\n" +
                       "- Études de cas réels\n" +
                       "- Projets appliqués\n" +
                       "- Évaluation des compétences";
        }
    }

    private static String detectDomain(String title) {
        if (title.contains("java")) return "java";
        if (title.contains("python")) return "python";
        return "general";
    }

    /**
     * Génère une image via DeepAI à partir du titre et de la description du cours.
     * Retourne toujours une URL d'image (DeepAI ou défaut si erreur).
     */
    public static String genererImagePourCours(String titre, String description) {
        String apiKey = "8f1fdd95-4666-47ef-8630-639790946505";
        String prompt = titre + " : " + description;
        String imageUrl = null;

        try {
            URL url = new URL("https://api.deepai.org/api/text2img");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Api-Key", apiKey);
            conn.setDoOutput(true);

            String data = "text=" + URLEncoder.encode(prompt, "UTF-8");
            try (OutputStream os = conn.getOutputStream()) {
                os.write(data.getBytes(StandardCharsets.UTF_8));
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder content = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            // Extraction robuste de l'URL de l'image
            JSONObject json = new JSONObject(content.toString());
            if (json.has("output_url")) {
                imageUrl = json.getString("output_url");
            } else {
                System.err.println("Erreur DeepAI : pas d'output_url dans la réponse");
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de la génération d'image DeepAI : " + e.getMessage());
        }

        // Image par défaut si erreur
        if (imageUrl == null) {
            imageUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80";
        }
        return imageUrl;
    }

    /**
     * Applique l'image en fond du Pane JavaFX avec un overlay moderne pour la lisibilité.
     */
    public static void setBackgroundImageWithOverlay(String imageUrl, Pane pane) {
        Image image = new Image(imageUrl, true);
        BackgroundImage backgroundImage = new BackgroundImage(
            image,
            BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false)
        );
        pane.setBackground(new Background(backgroundImage));

        // Overlay semi-transparent pour la lisibilité
        javafx.scene.shape.Rectangle overlay = new javafx.scene.shape.Rectangle();
        overlay.setWidth(pane.getWidth());
        overlay.setHeight(pane.getHeight());
        overlay.setArcWidth(20); // coins arrondis
        overlay.setArcHeight(20);
        overlay.setFill(new javafx.scene.paint.Color(0, 0, 0, 0.35)); // noir, 35% d'opacité

        // Adapter la taille de l'overlay si le pane est redimensionné
        pane.widthProperty().addListener((obs, oldVal, newVal) -> overlay.setWidth(newVal.doubleValue()));
        pane.heightProperty().addListener((obs, oldVal, newVal) -> overlay.setHeight(newVal.doubleValue()));

        // Ajouter l'overlay en premier plan
        if (!pane.getChildren().contains(overlay)) {
            pane.getChildren().add(overlay);
        }
    }
} 