package ranim.projetpidev.controllers.candidat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import ranim.projetpidev.entites.Candidat;
import ranim.projetpidev.services.CandidatService;

import java.io.IOException;

public class BackdetailscandidatureController {

    @FXML private Label fullNameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private TextArea motivationLabel;
    @FXML private Button backBtn;
    @FXML private Button acceptBtn;
    @FXML private Button rejectBtn;

    private CandidatService candidatService = new CandidatService();

    private Candidat currentCandidat;

    // Method to set candidate details in the view
    public void setCandidateDetails(Candidat candidat) {
        this.currentCandidat = candidat; // Store the current candidate in a member variable
        fullNameLabel.setText(candidat.getFullName());
        emailLabel.setText(candidat.getEmail());
        phoneLabel.setText(candidat.getPhoneNumber());
        motivationLabel.setText(candidat.getApplyingMotivation());
    }

    // Accept the candidate (sets result to true)
    @FXML
    public void acceptCandidate() {
        if (currentCandidat != null) {
            try {
                // Update candidate status to "Accepted"
                currentCandidat.setResult(true);
                candidatService.update(currentCandidat); // Update in the database
                showConfirmation("Candidate Accepted", "The candidate has been accepted.");

                // Load the "Accepted" result page (Frontcandidatureaccepté.fxml)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/Frontcandidatureaccepté.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) backBtn.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Candidate Accepted");
                stage.show();
            } catch (Exception e) {
                showError("Error", "An error occurred while accepting the candidate.");
                e.printStackTrace();
            }
        }
    }

    // Reject the candidate (sets result to false)
    @FXML
    public void rejectCandidate() {
        if (currentCandidat != null) {
            try {
                // Update candidate status to "Rejected"
                currentCandidat.setResult(false);
                candidatService.update(currentCandidat); // Update in the database
                showConfirmation("Candidate Rejected", "The candidate has been rejected.");

                // Load the "Rejected" result page (Frontcandidaturerefusé.fxml)
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/Frontcandidaturerefusé.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) backBtn.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.setTitle("Candidate Rejected");
                stage.show();
            } catch (Exception e) {
                showError("Error", "An error occurred while rejecting the candidate.");
                e.printStackTrace();
            }
        }
    }

    // Method to navigate back to the list view (Backlistecandidatures.fxml)
    @FXML
    public void backToList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/candidat/Backlistecandidatures.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Candidates List");
            stage.show();
        } catch (IOException e) {
            showError("Error", "An error occurred while navigating back to the list.");
            e.printStackTrace();
        }
    }

    // Show confirmation message
    private void showConfirmation(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Show error message
    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
