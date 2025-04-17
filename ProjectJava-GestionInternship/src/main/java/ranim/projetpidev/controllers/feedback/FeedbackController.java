package ranim.projetpidev.controllers.feedback;

import com.mysql.cj.MysqlConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import jdk.jfr.Recording;
import jdk.jfr.Event;import ranim.projetpidev.entites.Feedback;
import ranim.projetpidev.entites.Internship;
import ranim.projetpidev.services.FeedbackService;
import ranim.projetpidev.services.InternshipService;
import ranim.projetpidev.services.CandidatService;

import java.sql.SQLException;
import java.io.IOException;

public class FeedbackController {

    @FXML private Slider ratingSlider;
    @FXML private TextArea commentField;

    private FeedbackService feedbackService = new FeedbackService();
    private InternshipService internshipService = new InternshipService();
    private CandidatService candidatService = new CandidatService();

    private Internship currentInternship; // Variable pour stocker l'internship sélectionné

    // Méthode pour définir l'internship
    public void setInternship(Internship internship) {
        this.currentInternship = internship;
    }

    // Soumettre le feedback
    @FXML
    private void submitFeedback() {
        try {
            // Vérifiez si l'internship existe
            if (currentInternship == null || !internshipService.existsinternship(currentInternship.getId())) {
                showError("Erreur", "Le stage n'existe pas !");
                return;
            }

            // Récupérer le candidatId du candidat actuellement connecté ou sélectionné
            int candidatId = getCurrentCandidatId(); // Cette méthode doit retourner l'ID du candidat actuel connecté

            // Vérifier si le candidat existe
            if (!candidatService.exists(candidatId)) {
                showError("Erreur", "Le candidat n'existe pas !");
                return;
            }

            // Créer une instance de feedback
            Feedback feedback = new Feedback();
            feedback.setRating((int) ratingSlider.getValue());  // Récupère la note
            feedback.setComment(commentField.getText());  // Récupère le commentaire

            // Définir la date de feedback
            feedback.setDateFeedback(new java.util.Date());

            // Lancer l'ajout du feedback dans la base de données
            feedback.setInternshipId(currentInternship.getId());  // Ajout de l'internshipId ici
            feedback.setCandidatId(candidatId);  // Ajout du candidatId ici
            feedbackService.add(feedback);

            // Confirmation de soumission
            showConfirmation("Feedback envoyé avec succès", "Votre feedback a été envoyé avec succès !");
        } catch (SQLException e) {
            showError("Erreur", "Une erreur est survenue lors de l'envoi de votre feedback.");
            e.printStackTrace();
        }
    }

    // Méthode pour récupérer l'ID du candidat actuel
    private int getCurrentCandidatId() {
        // Vous pouvez récupérer l'ID du candidat à partir de l'utilisateur connecté.
        // Si vous avez une logique d'authentification, remplacez cette méthode par celle qui retourne l'ID du candidat connecté.
        return 1; // Remplacez ceci par la logique d'authentification pour obtenir le candidatID réel
    }

    // Afficher une fenêtre de confirmation
    private void showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Afficher une fenêtre d'erreur
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Méthode pour la redirection vers FrontInternshipdetails.fxml (Cancel)
    @FXML
    private void goToInternshipDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/internship/FrontInternshipdetails.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ratingSlider.getScene().getWindow();  // Obtient la fenêtre actuelle
            stage.setScene(new Scene(root));
            stage.setTitle("Détails du Stage");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

