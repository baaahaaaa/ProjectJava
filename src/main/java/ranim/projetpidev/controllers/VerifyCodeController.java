package ranim.projetpidev.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Duration;

public class VerifyCodeController {

    @FXML private TextField codeField;
    @FXML private Label messageLabel;

    @FXML
    public void handleVerify() {
        String enteredCode = codeField.getText().trim();
        if (enteredCode.equals(ForgotPasswordController.getGeneratedCode())) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/ranim/projetpidev/ResetPassword.fxml"));
                Stage stage = (Stage) codeField.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            messageLabel.setText("Invalid code. Try again.");
        }
    }
    public void showErrorAnimation(Node node) {
        node.getStyleClass().add("shake-animation");
        node.getStyleClass().add("invalid-field");

        // Retirer les classes après l'animation
        new Timeline(new KeyFrame(Duration.seconds(0.5), e -> {
            node.getStyleClass().remove("shake-animation");
        })).play();
    }
}