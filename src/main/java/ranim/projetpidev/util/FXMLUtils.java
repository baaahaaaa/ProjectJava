package ranim.projetpidev.util;

import java.io.IOException;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import ranim.projetpidev.HelloApplication;

public class FXMLUtils {
    
    /**
     * Load an FXML file and return the loader
     * @param fxmlPath The path to the FXML file (e.g., "editEvent.fxml")
     * @return The FXMLLoader with the loaded FXML
     * @throws IOException If loading fails
     */
    public static FXMLLoader loadFXML(String fxmlPath) throws IOException {
        // First try through HelloApplication
        URL url = HelloApplication.class.getResource(fxmlPath);
        if (url == null) {
            // Try with leading slash
            url = HelloApplication.class.getResource("/" + fxmlPath);
        }
        
        if (url == null) {
            throw new IOException("Cannot find FXML file: " + fxmlPath);
        }
        
        System.out.println("Loading FXML from URL: " + url);
        
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        fxmlLoader.load(); // Pre-load the FXML
        
        return fxmlLoader;
    }
} 