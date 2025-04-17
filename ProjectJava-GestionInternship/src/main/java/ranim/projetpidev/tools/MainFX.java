package ranim.projetpidev.tools;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class MainFX extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Tester la scène Backlisteinternship.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/internship/Backlisteinternship.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root,400, 300);
        stage.setWidth(1000);  // Largeur de la fenêtre
        stage.setHeight(800);
        stage.setTitle("Internship List");
        stage.setScene(scene);
        stage.show();

        // Vous pouvez tester d'autres scènes ici en décommentant les lignes suivantes si nécessaire

        // Tester la scène Backinternshipdetails.fxml
        // FXMLLoader loaderDetails = new FXMLLoader(getClass().getResource("/ranim/projetpidev/views/Backinternshipdetails.fxml"));
        // Parent rootDetails = loaderDetails.load();
        // Scene sceneDetails = new Scene(rootDetails);
        // stage.setTitle("Internship Details");
        // stage.setScene(sceneDetails);
        // stage.show();

        // Tester la scène Backupdateinternship.fxml
        // FXMLLoader loaderUpdate = new FXMLLoader(getClass().getResource("/ranim/projetpidev/views/Backupdateinternship.fxml"));
        // Parent rootUpdate = loaderUpdate.load();
        // Scene sceneUpdate = new Scene(rootUpdate);
        // stage.setTitle("Edit Internship");
        // stage.setScene(sceneUpdate);
        // stage.show();
    }

    public static void main(String[] args) {
        launch(args); // Lancement de l'application JavaFX
    }
}
