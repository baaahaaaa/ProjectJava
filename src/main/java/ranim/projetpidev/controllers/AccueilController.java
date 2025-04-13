package ranim.projetpidev.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AccueilController {

    public void goToAgent(ActionEvent event) {
        loadScene(event, "/ranim/projetpidev/AjouterAgent.fxml");
    }

    public void goToStudent(ActionEvent event) {
        loadScene(event, "/ranim/projetpidev/AjoutStudent.fxml");
    }

    public void goToTutor(ActionEvent event) {
        loadScene(event, "/ranim/projetpidev/AjoutTutor.fxml");
    }

    private void loadScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Formulaire d'inscription");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
