package ranim.projetpidev.controllers;

import ranim.projetpidev.entites.Course;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

import java.sql.Date;
import java.time.LocalDate;

public class CourseDialogController {
    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private TextField domainField;
    @FXML
    private ChoiceBox<String> typeChoiceBox;
    @FXML
    private TextField priceField;
    @FXML
    private DatePicker creationDatePicker;
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;

    private Course course;
    private Stage dialogStage;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
        typeChoiceBox.getItems().addAll("free", "paid");
        
        // Add input validation listeners
        titleField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
        });
        
        descriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
        });
        
        domainField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
        });
        
        typeChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
        });
        
        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                priceField.setText(oldValue);
            }
            validateInput();
        });
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCourse(Course course) {
        this.course = course;
        if (course != null) {
            titleField.setText(course.getTitle());
            descriptionField.setText(course.getDescription());
            domainField.setText(course.getDomain());
            typeChoiceBox.setValue(course.getType());
            priceField.setText(String.valueOf(course.getPrice()));
            creationDatePicker.setValue(course.getCreationDate().toLocalDate());
        } else {
            creationDatePicker.setValue(LocalDate.now());
        }
    }

    public Course getCourse() {
        if (course == null) {
            course = new Course();
        }
        course.setTitle(titleField.getText());
        course.setDescription(descriptionField.getText());
        course.setDomain(domainField.getText());
        course.setType(typeChoiceBox.getValue());
        try {
            course.setPrice(Double.parseDouble(priceField.getText()));
        } catch (NumberFormatException e) {
            course.setPrice(0.0);
        }
        course.setCreationDate(Date.valueOf(creationDatePicker.getValue()));
        return course;
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            if (course == null) {
                course = new Course();
            }
            
            course.setTitle(titleField.getText());
            course.setDescription(descriptionField.getText());
            course.setDomain(domainField.getText());
            course.setType(typeChoiceBox.getValue());
            course.setPrice(Double.parseDouble(priceField.getText()));
            course.setCreationDate(Date.valueOf(creationDatePicker.getValue()));
            
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private void validateInput() {
        String errorMessage = "";

        if (titleField.getText() == null || titleField.getText().trim().isEmpty()) {
            errorMessage += "Title is required!\n";
        }

        if (descriptionField.getText() == null || descriptionField.getText().trim().isEmpty()) {
            errorMessage += "Description is required!\n";
        }

        if (domainField.getText() == null || domainField.getText().trim().isEmpty()) {
            errorMessage += "Domain is required!\n";
        }

        if (typeChoiceBox.getValue() == null) {
            errorMessage += "Type is required!\n";
        }

        if (priceField.getText() == null || priceField.getText().trim().isEmpty()) {
            errorMessage += "Price is required!\n";
        } else {
            try {
                double price = Double.parseDouble(priceField.getText());
                if (price < 0) {
                    errorMessage += "Price cannot be negative!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Price must be a valid number!\n";
            }
        }

        saveButton.setDisable(!errorMessage.isEmpty());
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (titleField.getText() == null || titleField.getText().trim().isEmpty()) {
            errorMessage += "Title is required!\n";
        }

        if (descriptionField.getText() == null || descriptionField.getText().trim().isEmpty()) {
            errorMessage += "Description is required!\n";
        }

        if (domainField.getText() == null || domainField.getText().trim().isEmpty()) {
            errorMessage += "Domain is required!\n";
        }

        if (typeChoiceBox.getValue() == null) {
            errorMessage += "Type is required!\n";
        }

        if (priceField.getText() == null || priceField.getText().trim().isEmpty()) {
            errorMessage += "Price is required!\n";
        } else {
            try {
                double price = Double.parseDouble(priceField.getText());
                if (price < 0) {
                    errorMessage += "Price cannot be negative!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Price must be a valid number!\n";
            }
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
} 