package ranim.projetpidev.controllers.internship;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ranim.projetpidev.entites.Internship;
import ranim.projetpidev.services.InternshipService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;

public class BackaddinternshipController {

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
    @FXML private ComboBox<String> currencyComboBox;
    @FXML private ComboBox<String> durationUnitComboBox;
    @FXML private Button addBtn;
    @FXML private Button cancelBtn;

    private InternshipService internshipService = new InternshipService();

    @FXML public void initialize() {
        requirementsComboBox.getItems().addAll("Business","IT","Marketing","Other");
        recompensationTypeComboBox.getItems().addAll("Recompensated","Free");
        currencyComboBox.getItems().addAll("USD","Euro","Dinar");
        durationUnitComboBox.getItems().addAll("month","week","year","day");

        // Durée en entier
        durationField.setTextFormatter(new TextFormatter<>(c ->
                c.getControlNewText().matches("\\d*") ? c : null
        ));

        // Montant décimal
        recompensationAmountField.setTextFormatter(new TextFormatter<>(c ->
                c.getControlNewText().matches("\\d*(\\.\\d*)?") ? c : null
        ));

        // Validation email
        emailField.focusedProperty().addListener((obs, oldV, newV) -> {
            if (!newV) {
                boolean ok = emailField.getText()
                        .matches("^[\\w.-]+@[\\w-]+\\.[a-zA-Z]{2,}$");
                if (!ok && !emailField.getStyleClass().contains("invalid"))
                    emailField.getStyleClass().add("invalid");
                else emailField.getStyleClass().removeAll(Collections.singleton("invalid"));
            }
        });

        // Activer/désactiver montant & devise
        recompensationTypeComboBox.valueProperty().addListener((obs,o,n) -> {
            boolean paid = "Recompensated".equals(n);
            recompensationAmountField.setDisable(!paid);
            currencyComboBox.setDisable(!paid);
        });
    }

    @FXML
    public void addInternship(ActionEvent event) {
        try {
            // Retrieve form data
            String title = titleField.getText();
            String description = descriptionField.getText();
            String companyName = companyNameField.getText();
            String location = locationField.getText();
            String duration = durationField.getText();
            String unit = durationUnitComboBox.getValue();
            String requirements = requirementsComboBox.getValue();
            String otherRequirements = otherRequirementsField.getText();
            String email = emailField.getText();
            String recompensationType = recompensationTypeComboBox.getValue();
            double recompensationAmount = recompensationAmountField.getText().isEmpty() ? 0 : Double.parseDouble(recompensationAmountField.getText());
            String currency = currencyComboBox.getValue();

            // Validate duration based on unit
            if (Integer.parseInt(duration) < 1) {
                if (unit.equals("year") && Integer.parseInt(duration) < 1) {
                    showError("Invalid Duration", "Duration for year should be at least 1.");
                    return;
                } else if (unit.equals("week") && Integer.parseInt(duration) < 4) {
                    showError("Invalid Duration", "Duration for week should be at least 4.");
                    return;
                } else if (unit.equals("month") && Integer.parseInt(duration) < 1) {
                    showError("Invalid Duration", "Duration for month should be at least 1.");
                    return;
                } else if (unit.equals("day") && Integer.parseInt(duration) < 30) {
                    showError("Invalid Duration", "Duration for day should be at least 30.");
                    return;
                }
            }

            // Create Internship object
            Internship internship = new Internship(
                    0, 23, title, description, companyName, location, duration, requirements, otherRequirements, email,
                    recompensationType, recompensationAmount, currency, 0, 0
            );

            // Add the internship to the database
            internshipService.add(internship);

            // Redirect to internship list page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/internship/Backlisteinternship.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) addBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Internship List");
            stage.show();

        } catch (SQLException e) {
            showError("Database Error", "An error occurred while adding the internship.");
            e.printStackTrace();
        } catch (NumberFormatException | IOException e) {
            showError("Invalid Input", "Please enter a valid number for duration and recompensation amount.");
            e.printStackTrace();
        }
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void cancelAdd(ActionEvent event) {
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
