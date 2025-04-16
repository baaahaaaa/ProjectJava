package ranim.projetpidev.tools;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class MainFX extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/esprit/views/MainLayout.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setTitle("Liste");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args); // ✅ Obligatoire !
    }
}
