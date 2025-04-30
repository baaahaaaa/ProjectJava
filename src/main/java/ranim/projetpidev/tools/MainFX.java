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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/FrontDashboard.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root,400, 300);
        stage.setWidth(1000);
        stage.setHeight(800);
        stage.setTitle("Internship List");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch(args); // Lancement de l'application JavaFX
    }
}
