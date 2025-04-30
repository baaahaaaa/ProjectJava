package ranim.projetpidev.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ranim.projetpidev.entites.*;
import ranim.projetpidev.services.UserService;

import java.sql.SQLException;
import java.time.LocalDate;

public class EditUserController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private DatePicker entryDatePicker;
    @FXML private PasswordField passwordField;
    @FXML private ChoiceBox<String> typeChoiceBox;

    @FXML private TextField companyField;   // Agent
    @FXML private TextField locationField;  // Agent
    @FXML private TextField domainField;    // Tutor

    private User user;
    private final UserService userService = new UserService();

    public void initData(User user) {
        this.user = user;

        firstNameField.setText(user.getFirstName());
        lastNameField.setText(user.getLastName());
        emailField.setText(user.getEmail());
        passwordField.setText(user.getPassword());
        entryDatePicker.setValue(user.getEntryDate());
        typeChoiceBox.getItems().setAll("student", "agent", "tutor", "admin");
        typeChoiceBox.setValue(user.getType());

        if (user instanceof Agent agent) {
            companyField.setText(agent.getCompanyName());
            locationField.setText(agent.getLocation());
        } else if (user instanceof Tutor tutor) {
            domainField.setText(tutor.getDomain());
        }
    }

    @FXML
    private void handleSave() {
        try {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String email = emailField.getText();
            LocalDate entryDate = entryDatePicker.getValue();
            String password = passwordField.getText();
            String type = typeChoiceBox.getValue();

            User updatedUser;

            switch (type) {
                case "agent" -> updatedUser = new Agent(user.getId(), firstName, lastName, email, entryDate, password, type, user.getIs_active(), user.isActivation_code(),
                        companyField.getText(), locationField.getText());
                case "tutor" -> updatedUser = new Tutor(user.getId(), firstName, lastName, email, entryDate, password, type, user.getIs_active(), user.isActivation_code(), domainField.getText());
                case "student" -> updatedUser = new Student(user.getId(), firstName, lastName, email, entryDate, password, type, user.getIs_active(), user.isActivation_code());
                case "admin" -> updatedUser = new Admin(user.getId(), firstName, lastName, email, entryDate, password, type,user.getIs_active(),user.isActivation_code());
                default -> throw new IllegalArgumentException("Type inconnu");
            }

            userService.update(updatedUser);
            showAlert("✅ Succès", "L'utilisateur a été mis à jour avec succès.");
            closeWindow();

        } catch (Exception e) {
            showAlert("❌ Erreur", "Échec de la mise à jour : " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) firstNameField.getScene().getWindow();
        stage.close();
    }

    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
