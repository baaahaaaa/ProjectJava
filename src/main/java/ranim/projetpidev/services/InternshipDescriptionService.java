package ranim.projetpidev.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class InternshipDescriptionService {

    private static final String API_URL = "https://text-generator.p.rapidapi.com/api/v1/generate";  // URL de l'API
    private static final String API_KEY = "361c9add59msh688858a9f90ea96p12eec8jsne1e118d8030b";  // Votre clé API
    private static final String API_HOST = "text-generator.p.rapidapi.com";  // Hôte de l'API

    // Méthode pour générer la description du stage
    public String generateDescription(String title, String location, String requirements, String duration, String otherRequirements, String s) throws IOException {

        // Créer le prompt avec tous les détails du stage
        String prompt = String.format(
                "Internship Title: %s\nLocation: %s\nRequirements: %s\nDuration: %s\nOther Requirements: %s\n\nGenerate a detailed description for the above internship.",
                title, location, requirements, duration, otherRequirements
        );

        // Créer l'URL et la connexion
        URL url = new URL(API_URL);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Configuration de la requête
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("X-RapidAPI-Key", API_KEY);
        connection.setRequestProperty("X-RapidAPI-Host", API_HOST);
        connection.setDoOutput(true);

        // Données à envoyer dans la requête JSON
        String jsonInputString = String.format(
                "{\"text\":\"%s\",\"number_of_results\":1,\"max_length\":200,\"num_sentences\":3,\"min_probability\":0.0,\"stop_sequences\":[\"\"]}",
                prompt
        );

        // Envoi de la requête
        try (var os = connection.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        // Vérification de la réponse
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP response code: " + responseCode);
        }

        // Lire la réponse
        try (var in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            // Extraire la description générée depuis la réponse JSON
            return parseDescriptionFromResponse(response.toString());
        }
    }

    // Extraire la description du JSON de réponse
    private String parseDescriptionFromResponse(String responseBody) {
        int startIdx = responseBody.indexOf("\"generated_text\":\"") + "\"generated_text\":\"".length();
        int endIdx = responseBody.indexOf("\"}", startIdx);
        if (startIdx != -1 && endIdx != -1) {
            return responseBody.substring(startIdx, endIdx);
        } else {
            throw new RuntimeException("Error parsing description from response: " + responseBody);
        }
    }
}
