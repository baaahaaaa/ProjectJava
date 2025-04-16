package ranim.projetpidev.controllers;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import java.io.IOException;


public class FooterButtonsController {

    @FXML
    private Button btnQuitter; // ✅ Ajoute cette ligne

    @FXML
    private void handleRetour() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/hello-view.fxml")); // ← change ici le fichier cible si besoin
            Stage stage = (Stage) btnQuitter.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.setTitle("Accueil");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleQuitter() {
        Stage stage = (Stage) btnQuitter.getScene().getWindow();
        stage.close();
    }
}

