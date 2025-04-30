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

        // Essayer de récupérer l'utilisateur en fonction de l'email et du mot de passe
        User loggedInUser = userService.login(email, password);

        if (loggedInUser != null) {
            // Vérifier si l'utilisateur a activé son compte
            if (!loggedInUser.getIs_active()) {
                // Si le compte n'est pas activé, informer l'utilisateur et l'empêcher de se connecter
                errorLabel.setText("Veuillez activer votre compte via l'email envoyé !");
                return;  // Empêche la suite de l'exécution
            }

            // Si l'utilisateur est activé, afficher le message de bienvenue
            errorLabel.setText("Bienvenue, " + loggedInUser.getFirstName() + " !");

            try {
                FXMLLoader loader;
                Parent root;

                // Vérification du type d'utilisateur (admin ou autre)
                if ("admin".equalsIgnoreCase(loggedInUser.getType())) {
                    // Rediriger vers le tableau de bord admin
                    loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/BackDashboard.fxml"));
                } else {
                    // Rediriger vers le tableau de bord général
                    loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/FrontDashboard.fxml"));
                }

                // Charger et afficher la nouvelle scène
                root = loader.load();
                Stage stage = (Stage) emailField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Dashboard");
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();  // Gérer les erreurs d'affichage de la scène
            }
        } else {
            // Si l'utilisateur n'est pas trouvé ou le mot de passe est incorrect
            errorLabel.setText("Email ou mot de passe incorrect !");
        }
    }

    public void goToAcceuil(ActionEvent event) {
        loadScene(event, "/ranim/projetpidev/Accueil.fxml");
    }
    public void goToForgetPassword(ActionEvent event) {
        loadScene(event, "/ranim/projetpidev/ForgetPassword.fxml");
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

    @FXML
    private void handleForgotPassword(ActionEvent event) {
        try {
            // Charger le fichier FXML correctement
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/ForgotPassword.fxml"));
            Parent root = loader.load();

            // Créer la scène et l'afficher dans une nouvelle fenêtre
            Stage stage = new Stage();
            stage.setTitle("Réinitialisation du mot de passe");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            // Gérer l'exception d'entrée/sortie (par exemple, fichier non trouvé)
            e.printStackTrace();
        }
    }

}
