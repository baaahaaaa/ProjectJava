package ranim.projetpidev.controllers;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.util.Duration;
import ranim.projetpidev.services.UserService;

public class ResetPasswordController {

    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label messageLabel;

    @FXML
    public void handleResetPassword() {
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (!newPassword.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match.");
            return;
        }

        boolean success = UserService.updatePassword(ForgotPasswordController.getUserEmail(), newPassword);
        if (success) {
            messageLabel.setText("Password successfully updated!");
        } else {
            messageLabel.setText("Error updating password.");
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