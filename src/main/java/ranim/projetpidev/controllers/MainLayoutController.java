package ranim.projetpidev.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainLayoutController {
    @FXML
    private StackPane contentArea;

    @FXML
    private void handleCoursesButton() {
        try {
            VBox courseView = FXMLLoader.load(getClass().getResource("/com/esprit/views/CourseView.fxml"));
            contentArea.getChildren().setAll(courseView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleResourcesButton() {
        try {
            VBox resourceView = FXMLLoader.load(getClass().getResource("/com/esprit/views/ResourceView.fxml"));
            contentArea.getChildren().setAll(resourceView);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 