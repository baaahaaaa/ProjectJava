package ranim.projetpidev.controllers.candidat;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ranim.projetpidev.entites.Candidat;
import ranim.projetpidev.services.CandidatService;
import ranim.projetpidev.services.InternshipService;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

public class AddCandidatureController {
    @FXML private ScrollPane formScrollPane;
    @FXML private Slider scrollSlider;
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField phoneField;
    @FXML private TextArea motivationField;
    @FXML private TextField cvField;
    @FXML private DatePicker interviewDatePicker;
    @FXML private Button submitButton;
    @FXML private Button cancelButton;
    @FXML private Button browseCvButton;

    private CandidatService candidatService = new CandidatService();
    private InternshipService internshipService = new InternshipService(); // InternshipService instance to check existence
    private int internshipId; // Variable to store the internship ID
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @FXML
    public void initialize() {
        // Slider <-> ScrollPane
        scrollSlider.valueProperty()
                .bindBidirectional(formScrollPane.vvalueProperty());

        // TextFormatter pour n'accepter que 8 chiffres maximum
        UnaryOperator<TextFormatter.Change> phoneFilter = change -> {
            String text = change.getControlNewText();
            return text.matches("\\d{0,8}") ? change : null;
        };
        phoneField.setTextFormatter(new TextFormatter<>(phoneFilter));
    }

    /** Injecte l'ID du stage depuis le controller parent */
    public void setInternshipId(int internshipId) {
        this.internshipId = internshipId;
    }

    /** Ouvre un FileChooser pour sélectionner le CV */
    @FXML
    public void browseCvFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select CV File");
        chooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("PDF", "*.pdf"),
                new FileChooser.ExtensionFilter("Word", "*.doc", "*.docx")
        );
        File file = chooser.showOpenDialog(browseCvButton.getScene().getWindow());
        if (file != null) {
            cvField.setText(file.getAbsolutePath());
        }
    }

    /** Validation puis enregistrement de la candidature */
    @FXML
    public void submitApplication() {
        // 1) Tous les champs obligatoires
        if (fullNameField.getText().isEmpty()
                || emailField.getText().isEmpty()
                || phoneField.getText().isEmpty()
                || motivationField.getText().isEmpty()
                || cvField.getText().isEmpty()
                || interviewDatePicker.getValue() == null) {
            showError("Form Validation", "Tous les champs sont requis.");
            return;
        }

        // 2) Format email
        if (!EMAIL_PATTERN.matcher(emailField.getText()).matches()) {
            showError("Email invalide", "Veuillez saisir une adresse e‑mail valide.");
            return;
        }

        // 3) Téléphone exactement 8 chiffres
        if (phoneField.getText().length() != 8) {
            showError("Téléphone invalide", "Le numéro de téléphone doit contenir exactement 8 chiffres.");
            return;
        }

        // 4) Vérifier l'existence du stage
        try {
            if (!internshipService.exists(internshipId)) {
                showError("Stage invalide", "Le stage sélectionné n'existe pas.");
                return;
            }
        } catch (SQLException e) {
            showError("Erreur BD", "Impossible de vérifier le stage en base.");
            e.printStackTrace();
            return;
        }

        // Création de l'entité Candidat
        Candidat candidat = new Candidat();
        candidat.setFullName(fullNameField.getText());
        candidat.setEmail(emailField.getText());
        candidat.setPhoneNumber(phoneField.getText());
        candidat.setApplyingMotivation(motivationField.getText());
        candidat.setCvFilename(cvField.getText());
        candidat.setInterviewDate(java.sql.Date.valueOf(interviewDatePicker.getValue()));
        candidat.setInternshipId(internshipId);
        candidat.setStudentId(4); // FIXME: récupérer l'étudiant courant
        candidat.setDateCandidature(new Date());
        candidat.setResult(false);

        // Persistance
        try {
            candidatService.add(candidat);
            showConfirmation("Succès", "Votre candidature a été soumise.");
            clearForm();
        } catch (SQLException e) {
            showError("Erreur BD", "Impossible d’enregistrer la candidature.");
            e.printStackTrace();
        }
    }

    private boolean internshipExists(int internshipId) {
        try {
            return internshipService.exists(internshipId); // Check if internship exists using InternshipService
        } catch (SQLException e) {
            showError("Database Error", "An error occurred while checking the internship.");
            e.printStackTrace();
        }
        return false;
    }

    // Display error message
    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Display confirmation message with "Back" button
    private void showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        // Create "Back" button for navigation
        ButtonType backButton = new ButtonType("Retour");
        alert.getButtonTypes().setAll(backButton);

        alert.showAndWait().ifPresent(response -> {
            if (response == backButton) {
                goBack();
            }
        });
    }

    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/internship/Frontlisteinternships.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) submitButton.getScene().getWindow();  // Getting current stage
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Stages");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to navigate to Internship Details (Cancel action)
    @FXML
    private void goToInternshipDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/internship/FrontInternshipdetails.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) cancelButton.getScene().getWindow();  // Getting current stage
            stage.setScene(new Scene(root));
            stage.setTitle("Détails du Stage");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Optionally clear the form fields after successful submission
    private void clearForm() {
        fullNameField.clear();
        emailField.clear();
        phoneField.clear();
        motivationField.clear();
        cvField.clear();
        interviewDatePicker.setValue(null);
    }
}
