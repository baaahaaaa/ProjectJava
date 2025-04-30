package ranim.projetpidev.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Parent;

public class BackDashboardController {

    @FXML
    private StackPane contentArea;

    @FXML
    public void initialize() {
        // Vue par défaut au démarrage : Event
        showEvent();
    }

    public void showProfile() {
        loadView("/ranim/projetpidev/ListUser.fxml"); // <== CORRIGÉ ICI
    }

    public void showCours() {
        loadView("/ranim/projetpidev/MainLayout.fxml");
    }

    public void showEvent() {
        loadView("/ranim/projetpidev/addEvent.fxml");
    }

    public void showInternships() {
        loadView("/ranim/projetpidev/internship/Backlisteinternship.fxml");
    }

    public void showQuiz() {
        loadView("/ranim/projetpidev/addQuiz.fxml"); // <== CORRIGÉ ICI
    }

    public void logout() {
        System.out.println("Déconnecté !");
        // Redirection à la page de connexion si nécessaire
    }

    private void loadView(String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().setAll(view);
        } catch (Exception e) {
            System.err.println("Erreur de chargement du fichier FXML : " + fxmlPath);
            e.printStackTrace();
        }
    }
}
