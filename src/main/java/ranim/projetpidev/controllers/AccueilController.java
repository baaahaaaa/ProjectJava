package ranim.projetpidev.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AccueilController {

    private void loadScene(String fxmlFile, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/" + fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goToAgent(ActionEvent event) {
        loadScene("AjouterAgent.fxml", event);
    }

    public void goToStudent(ActionEvent event) {
        loadScene("AjouterStudent.fxml", event);
    }

    public void goToTutor(ActionEvent event) {
        loadScene("AjouterTutor.fxml", event);
    }
}
