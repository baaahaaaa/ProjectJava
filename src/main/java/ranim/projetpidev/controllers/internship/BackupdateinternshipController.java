package ranim.projetpidev.controllers.internship;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ranim.projetpidev.entites.Internship;
import ranim.projetpidev.services.InternshipService;

import java.io.IOException;
import java.sql.SQLException;

public class BackupdateinternshipController {

    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private TextField companyNameField;
    @FXML private TextField locationField;
    @FXML private TextField durationField;
    @FXML private ComboBox<String> requirementsComboBox;
    @FXML private TextArea otherRequirementsField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> recompensationTypeComboBox;
    @FXML private TextField recompensationAmountField;
    @FXML private TextField currencyField;

    @FXML private Button saveBtn;
    @FXML private Button cancelBtn;

    private InternshipService internshipService = new InternshipService();
    private int internshipId;

    @FXML
    public void initialize() {
        requirementsComboBox.getItems().addAll("Business", "IT", "Marketing", "Other");
        recompensationTypeComboBox.getItems().addAll("Recompensated", "Free");
    }

    public void setInternshipId(int id) {
        this.internshipId = id;
        try {
            Internship internship = internshipService.getById(id);
            companyNameField.setText(internship.getCompanyName());
            titleField.setText(internship.getTitle());
            descriptionField.setText(internship.getDescription());
            locationField.setText(internship.getLocation());
            durationField.setText(internship.getDuration());
            requirementsComboBox.setValue(internship.getRequirements());
            otherRequirementsField.setText(internship.getOtherRequirements());
            emailField.setText(internship.getEmail());
            recompensationTypeComboBox.setValue(internship.getRecompensationType());
            recompensationAmountField.setText(String.valueOf(internship.getRecompensationAmount()));
            currencyField.setText(internship.getCurrency());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void saveInternship(ActionEvent event) {
        try {
            Internship internship = new Internship(
                    internshipId,
                    0,
                    titleField.getText(),
                    descriptionField.getText(),
                    companyNameField.getText(),
                    locationField.getText(),
                    durationField.getText(),
                    requirementsComboBox.getValue(),
                    otherRequirementsField.getText(),
                    emailField.getText(),
                    recompensationTypeComboBox.getValue(),
                    Double.parseDouble(recompensationAmountField.getText()),
                    currencyField.getText(),
                    0,
                    0
            );
            internshipService.update(internship);

            // After saving, go back to the list view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/internship/Backlisteinternship.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) saveBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Internship List");
            stage.show();
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cancelUpdate(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/internship/Backlisteinternship.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) cancelBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Internship List");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
