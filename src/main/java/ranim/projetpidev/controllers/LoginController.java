package ranim.projetpidev.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ranim.projetpidev.entites.User;
import ranim.projetpidev.services.UserService;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label errorLabel;

    private UserService userService = new UserService();

    @FXML
    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        User loggedInUser = userService.login(email, password);

        if (loggedInUser != null) {
            errorLabel.setText("Bienvenue, " + loggedInUser.getFirstName() + " !");
            try {
                FXMLLoader loader;
                Parent root;
                if ("admin".equalsIgnoreCase(loggedInUser.getType())) {
                    // Rediriger vers ListUser.fxml
                    loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/BackDashboard.fxml"));
                } else {
                    // Rediriger vers Accueil.fxml (par exemple)
                    loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/FrontDashboard.fxml"));
                }
                root = loader.load();
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Dashboard");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Email ou mot de passe incorrect !");
        }
    }
    public void goToAcceuil(ActionEvent event) {
        loadScene(event, "/ranim/projetpidev/Accueil.fxml");
    }
    private void loadScene(ActionEvent event, String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Are you ? : ");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
