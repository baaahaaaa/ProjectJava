package ranim.projetpidev.controllers.internship;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import ranim.projetpidev.controllers.candidat.AddCandidatureController;
import ranim.projetpidev.controllers.feedback.FeedbackController;
import ranim.projetpidev.entites.Internship;
import ranim.projetpidev.entites.Feedback;
import ranim.projetpidev.services.InternshipService;
import ranim.projetpidev.services.FeedbackService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class FrontInternshipdetailsController {

    @FXML private Label titleLabel;
    @FXML private Label companyLabel;
    @FXML private Label locationLabel;
    @FXML private TextArea descriptionLabel;
    @FXML private Label durationLabel;
    @FXML private Label requirementsLabel;
    @FXML private Label otherRequirementsLabel;
    @FXML private Label emailLabel;
    @FXML private Label recompensationTypeLabel;
    @FXML private Label recompensationAmountLabel;
    @FXML private Label currencyLabel;
    @FXML private Label latitudeLabel;
    @FXML private Label longitudeLabel;
    @FXML private Button applyBtn;
    @FXML private Button resultBtn;
    @FXML private Button feedbackBtn;
    @FXML private Button backBtn;
    @FXML private ListView<Feedback> feedbackListView;

    private InternshipService internshipService = new InternshipService();
    private FeedbackService feedbackService = new FeedbackService();
    private Internship currentInternship;

    /**
     * Permet de recevoir l'instance d'Internship et de charger les informations.
     */
    @FXML
    public void setInternship(Internship internship) {
        this.currentInternship = internship;
        loadInternshipDetails(internship);
        loadFeedbacks(internship.getId());
    }

    private void loadInternshipDetails(Internship internship) {
        try {
            Internship i = internshipService.getById(internship.getId());
            titleLabel.setText(i.getTitle());
            companyLabel.setText(i.getCompanyName());
            locationLabel.setText(i.getLocation());
            descriptionLabel.setText(i.getDescription());
            durationLabel.setText(i.getDuration());
            requirementsLabel.setText(i.getRequirements());
            otherRequirementsLabel.setText(i.getOtherRequirements());
            emailLabel.setText(i.getEmail());
            recompensationTypeLabel.setText(i.getRecompensationType());
            recompensationAmountLabel.setText(String.valueOf(i.getRecompensationAmount()));
            currencyLabel.setText(i.getCurrency());
            latitudeLabel.setText(String.valueOf(i.getLatitude()));
            longitudeLabel.setText(String.valueOf(i.getLongitude()));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadFeedbacks(int internshipId) {
        try {
            List<Feedback> allFeedbacks = feedbackService.getAll();
            List<Feedback> internshipFeedbacks = allFeedbacks.stream()
                    .filter(f -> f.getInternshipId() == internshipId)
                    .collect(Collectors.toList());
            feedbackListView.setItems(FXCollections.observableArrayList(internshipFeedbacks));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/internship/Frontlisteinternships.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Internship List");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void applyNow(ActionEvent event) throws SQLException {
        // Check if the internship exists in the database before proceeding
        if (currentInternship == null || !internshipService.exists(currentInternship.getId())) {
            showError("Invalid Internship", "The internship you are applying for does not exist.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/candidat/Frontaddcandidature.fxml"));
            Parent root = loader.load();
            AddCandidatureController controller = loader.getController();
            controller.setInternshipId(currentInternship.getId());
            Stage stage = (Stage) applyBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Postuler au Stage");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showError(String invalidInternship, String s) {
    }

    @FXML
    public void showResult(ActionEvent event) {
        boolean isAccepted = false; // Remplacez par votre logique réelle
        String fxmlFile = isAccepted ?
                "/ranim/projetpidev/candidat/Frontcandidatureaccepté.fxml" :
                "/ranim/projetpidev/candidat/Frontcandidaturerefusé.fxml";
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) resultBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Résultat de la Candidature");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToFeedback() {
        try {
            // Créez le FXMLLoader pour charger la scène de feedback
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/feedback/Frontpageajoutfeedback.fxml"));
            Parent root = loader.load();

            // Récupérer le contrôleur du Feedback
            FeedbackController feedbackController = loader.getController();

            // Passer l'objet Internship au contrôleur Feedback
            feedbackController.setInternship(currentInternship);  // currentInternship est l'objet Internship actuel

            // Charger la scène de feedback
            Stage stage = (Stage) feedbackBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Leave Feedback");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
