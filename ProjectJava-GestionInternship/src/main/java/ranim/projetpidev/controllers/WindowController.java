package ranim.projetpidev.controllers;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class WindowController {

    @FXML
    private Button maximizeButton; // Assuming you have a button to trigger maximize

    // Method to maximize the window when button is clicked
    @FXML
    public void maximizeWindow() {
        Stage stage = (Stage) maximizeButton.getScene().getWindow();
        stage.setFullScreen(true);  // This will make the window full-screen
    }

    // Optionally, add functionality to switch between normal and full-screen
    @FXML
    public void toggleFullScreen() {
        Stage stage = (Stage) maximizeButton.getScene().getWindow();
        stage.setFullScreen(!stage.isFullScreen());  // Toggle full-screen mode
    }
}
