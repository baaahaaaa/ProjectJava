package ranim.projetpidev.controllers;

import ranim.projetpidev.entites.Course;
import ranim.projetpidev.utils.GeminiAPI;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.collections.FXCollections;
import javafx.application.Platform;
import javafx.concurrent.Task;

import java.sql.Date;
import java.time.LocalDate;

public class CourseDialogController {
    @FXML
    private TextField titleField;
    @FXML
    private TextArea descriptionField;
    @FXML
    private ComboBox<String> domainComboBox;
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
    @FXML
    private Label statusLabel;

    private Course course;
    private Stage dialogStage;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
        // Initialiser le ComboBox avec les options de domaines
        domainComboBox.setItems(FXCollections.observableArrayList(
                "IT", "Marketing", "Management", "Design", "Data science", "Deep learning", "Excel", "Soft skills"));

        // Initialize ChoiceBox with type options
        typeChoiceBox.setItems(FXCollections.observableArrayList("Paid", "Free"));

        // Ajouter un écouteur sur le champ titre pour générer la description
        titleField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.trim().isEmpty() && !newValue.equals(oldValue)) {
                generateDescription(newValue);
            }
        });

        // Autres écouteurs pour la validation
        descriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
        });

        domainComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
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

    private void generateDescription(String title) {
        if (title == null || title.trim().isEmpty()) {
            return;
        }

        // Créer une tâche en arrière-plan pour la génération
        Task<String> task = new Task<>() {
            @Override
            protected String call() throws Exception {
                updateMessage("Génération de la description en cours...");
                String result = GeminiAPI.generateCourseDescription(title);
                if (result.startsWith("Erreur")) {
                    throw new Exception(result);
                }
                return result;
            }
        };

        // Gérer le résultat
        task.setOnSucceeded(event -> {
            String description = task.getValue();
            Platform.runLater(() -> {
                descriptionField.setText(description);
                statusLabel.setText("Description générée avec succès");
                statusLabel.setStyle("-fx-text-fill: green;");
            });
        });

        task.setOnFailed(event -> {
            Platform.runLater(() -> {
                statusLabel.setText("Échec de la génération");
                statusLabel.setStyle("-fx-text-fill: red;");
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText("Erreur de génération");
                alert.setContentText("Impossible de générer la description : " + task.getException().getMessage());
                alert.showAndWait();
            });
        });

        // Mettre à jour le label de statut
        statusLabel.setText("Génération en cours...");
        statusLabel.setStyle("-fx-text-fill: blue;");

        // Lancer la tâche
        new Thread(task).start();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCourse(Course course) {
        this.course = course;
        if (course != null) {
            titleField.setText(course.getTitle());
            descriptionField.setText(course.getDescription());
            domainComboBox.setValue(course.getDomain());
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
        course.setDomain(domainComboBox.getValue());
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
            course.setDomain(domainComboBox.getValue());
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

        if (domainComboBox.getValue() == null || domainComboBox.getValue().trim().isEmpty()) {
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

        if (domainComboBox.getValue() == null || domainComboBox.getValue().trim().isEmpty()) {
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
