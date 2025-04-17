package ranim.projetpidev.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.image.Image;

public class HomeViewController {

    @FXML
    private StackPane rootPane;

    @FXML
    public void initialize() {
        System.out.println("Page HomeView.fxml chargée !");
        try {
            // ✅ Mise à jour avec logo.jpg
            Image image = new Image(getClass().getResource("/ranim/projetpidev/images/logo.jpg").toExternalForm());

            BackgroundImage bgImage = new BackgroundImage(
                    image,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(100, 100, true, true, true, false)
            );
            rootPane.setBackground(new Background(bgImage));
        } catch (Exception e) {
            System.out.println("Erreur de chargement de l'image : " + e.getMessage());
        }
    }

    @FXML
    private void handleGetStarted() {
        System.out.println("Bouton Get Started cliqué !");
    }
}
