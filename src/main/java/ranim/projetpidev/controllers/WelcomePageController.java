package ranim.projetpidev.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ranim.projetpidev.entites.User;

import javax.swing.text.Document;
import java.io.FileOutputStream;

public class WelcomePageController {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField entryDateField;

    @FXML
    private Button updateButton;

    @FXML
    private Button exportPdfButton;

    @FXML
    private TextField passwordField;

    private User user;

    // Cette méthode sera appelée pour passer l'utilisateur à la page de bienvenue
    public void initData(User user) {
        this.user = user;

        // Remplir les champs avec les données de l'utilisateur
        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        emailField.setText(user.getEmail());
        entryDateField.setText(user.getEntryDate().toString());
        passwordField.setText(user.getPassword());
    }

    // Méthode pour gérer l'action du bouton Mettre à jour
    @FXML
    private void handleUpdate() {
        try {
            // Charger le fichier FXML pour la page de mise à jour
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/EditUser.fxml"));
            Parent root = loader.load();

            // Passer l'utilisateur à la nouvelle page
            EditUserController controller = loader.getController();
            controller.initData(user);

            // Créer une nouvelle scène et afficher la page de mise à jour
            Stage stage = new Stage();
            stage.setTitle("Page de Mise à Jour");
            stage.setScene(new Scene(root, 400, 400));
            stage.show();

            // Fermer la fenêtre actuelle (optionnel)
            Stage currentStage = (Stage) firstNameField.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur est survenue lors du passage à la page de mise à jour.");
        }
    }
    // Méthode pour gérer l'action du bouton Exporter en PDF
    @FXML
    private void handleExportPDF() {
        showAlert("Exportation PDF", "La fonctionnalité d'exportation en PDF sera bientôt disponible !");
    }

    // Fonction pour afficher une alerte avec un message
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
