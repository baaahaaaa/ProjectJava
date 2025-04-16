package ranim.projetpidev.tools;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainQFx extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {
            // Charger le fichier FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/addQuiz.fxml"));
            AnchorPane root = loader.load();

            // Créer la scène avec le contenu du fichier FXML
            Scene scene = new Scene(root);

            // Définir le titre de la fenêtre
            primaryStage.setTitle("Ajouter un Quiz");

            // Ajouter la scène à la fenêtre principale (Stage)
            primaryStage.setScene(scene);

            // Afficher la fenêtre
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();  // Si une erreur survient lors du chargement du FXML
        }
    }
}
