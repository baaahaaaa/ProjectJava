package ranim.projetpidev.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import ranim.projetpidev.services.MailService;

import java.util.Random;

public class ForgotPasswordController {

    @FXML private TextField emailField;
    @FXML private Label messageLabel;

    private static String generatedCode;
    private static String userEmail;

    @FXML
    public void handleSendCode() {
        String email = emailField.getText().trim();
        if (email.isEmpty()) {
            messageLabel.setText("Please enter your email.");
            return;
        }

        generatedCode = generateCode();
        userEmail = email;

        MailService.sendCode(email, generatedCode);
        messageLabel.setText("Code sent!");

        // Charger VerifyCode.fxml
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ranim/projetpidev/VerifyCode.fxml"));
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String generateCode() {
        Random rand = new Random();
        int code = rand.nextInt(999999); // 6 digits
        return String.format("%06d", code);
    }

    public static String getGeneratedCode() {
        return generatedCode;
    }

    public static String getUserEmail() {
        return userEmail;
    }
}
