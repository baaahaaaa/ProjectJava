package ranim.projetpidev.controllers;

import ranim.projetpidev.entites.Course;
import ranim.projetpidev.entites.Ressource;
import ranim.projetpidev.services.CourseService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class RessourceDialogController {

    private static final String RESOURCES_BASE_DIR = "src/main/resources/ranim/projetpidev/resources/";

    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private ComboBox<String> courseComboBox;
    @FXML private ComboBox<String> formatComboBox;
    @FXML private TextField filePathField;
    @FXML private DatePicker creationDatePicker;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private Ressource ressource;
    private Stage dialogStage;
    private boolean okClicked = false;
    private final CourseService courseService = new CourseService();

    @FXML
    private void initialize() {
        creationDatePicker.setValue(LocalDate.now());
        formatComboBox.getItems().addAll("PDF", "Image");

        titleField.textProperty().addListener((obs, oldVal, newVal) -> validateInput());
        descriptionField.textProperty().addListener((obs, oldVal, newVal) -> validateInput());
        courseComboBox.valueProperty().addListener((obs, oldVal, newVal) -> validateInput());
        formatComboBox.valueProperty().addListener((obs, oldVal, newVal) -> validateInput());
        filePathField.textProperty().addListener((obs, oldVal, newVal) -> validateInput());
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCourses(ObservableList<Course> courses) {
        ObservableList<String> courseNames = FXCollections.observableArrayList();
        for (Course course : courses) {
            courseNames.add(course.getTitle());
        }
        courseComboBox.setItems(courseNames);
    }

    public void setResource(Ressource ressource) {
        this.ressource = ressource;
        populateFields();
    }

    private void populateFields() {
        if (ressource != null) {
            titleField.setText(ressource.getTitle());
            descriptionField.setText(ressource.getDescription());
            if (ressource.getCourse() != null) {
                courseComboBox.setValue(ressource.getCourse().getTitle());
            }
            formatComboBox.setValue(ressource.getFormat());
            filePathField.setText(ressource.getFilePath());
            creationDatePicker.setValue(
                    ressource.getCreationDate() != null
                            ? ressource.getCreationDate().toLocalDate()
                            : LocalDate.now()
            );
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public Ressource getResource() {
        updateResourceFromForm();
        return ressource;
    }

    private void updateResourceFromForm() {
        if (ressource == null)
            ressource = new Ressource();

        ressource.setTitle(titleField.getText());
        ressource.setDescription(descriptionField.getText());
        ressource.setFormat(formatComboBox.getValue());
        
        // Le chemin est déjà relatif car nous l'avons stocké ainsi dans handleBrowse
        ressource.setFilePath(filePathField.getText());

        if (creationDatePicker.getValue() != null)
            ressource.setCreationDate(Date.valueOf(creationDatePicker.getValue()));

        String courseName = courseComboBox.getValue();
        if (courseName != null) {
            courseService.rechercher().stream()
                    .filter(course -> course.getTitle().equals(courseName))
                    .findFirst()
                    .ifPresent(ressource::setCourse);
        }
    }

    @FXML
    private void handleBrowse() {
        if (formatComboBox.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Format requis", "Veuillez sélectionner un format.");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sélectionner un fichier");

        if ("PDF".equals(formatComboBox.getValue())) {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
        } else {
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg", "*.png", "*.gif", "*.bmp"));
        }

        File selectedFile = fileChooser.showOpenDialog(dialogStage);
        if (selectedFile != null) {
            try {
                // Créer le répertoire de destination s'il n'existe pas
                File destDir = new File(RESOURCES_BASE_DIR);
                if (!destDir.exists()) {
                    destDir.mkdirs();
                }

                // Générer un nom de fichier unique
                String fileName = generateUniqueFileName(selectedFile.getName());
                File destFile = new File(destDir, fileName);

                // Copier le fichier
                java.nio.file.Files.copy(
                    selectedFile.toPath(),
                    destFile.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING
                );

                // Stocker le chemin relatif dans le champ
                filePathField.setText(fileName);
            } catch (Exception e) {
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de copier le fichier : " + e.getMessage());
            }
        }
    }

    private String generateUniqueFileName(String originalName) {
        String baseName = originalName.substring(0, originalName.lastIndexOf('.'));
        String extension = originalName.substring(originalName.lastIndexOf('.'));
        String fileName = baseName + extension;
        int counter = 1;

        File destDir = new File(RESOURCES_BASE_DIR);
        while (new File(destDir, fileName).exists()) {
            fileName = baseName + "_" + counter + extension;
            counter++;
        }

        return fileName;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            updateResourceFromForm();
            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        StringBuilder errors = new StringBuilder();

        if (titleField.getText().isBlank()) errors.append("Le titre est requis.\n");
        if (descriptionField.getText().isBlank()) errors.append("La description est requise.\n");
        if (courseComboBox.getValue() == null) errors.append("Le cours est requis.\n");
        if (formatComboBox.getValue() == null) errors.append("Le format est requis.\n");
        if (filePathField.getText().isBlank()) errors.append("Le chemin du fichier est requis.\n");

        if (errors.length() > 0) {
            showAlert(Alert.AlertType.ERROR, "Champs invalides", errors.toString());
            return false;
        }

        return true;
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.initOwner(dialogStage);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void validateInput() {
        boolean valid = !titleField.getText().isBlank()
                && !descriptionField.getText().isBlank()
                && courseComboBox.getValue() != null
                && formatComboBox.getValue() != null
                && !filePathField.getText().isBlank();

        saveButton.setDisable(!valid);
    }
    public Stage getDialogStage() {
        return dialogStage;
    }

}
