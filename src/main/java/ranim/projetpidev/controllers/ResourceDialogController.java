package ranim.projetpidev.controllers;

import ranim.projetpidev.entites.Course;
import ranim.projetpidev.entites.Resource;
import ranim.projetpidev.services.CourseService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class ResourceDialogController {
    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private ComboBox<String> courseComboBox;
    @FXML private ComboBox<String> formatComboBox;
    @FXML private TextField filePathField;
    @FXML private DatePicker creationDatePicker;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;
    @FXML private Button browseButton;

    private Resource resource;
    private Stage dialogStage;
    private boolean okClicked = false;
    private final CourseService courseService = new CourseService();

    @FXML
    private void initialize() {
        // Initialize the date picker with today's date
        creationDatePicker.setValue(LocalDate.now());

        // Initialize format combo box
        formatComboBox.getItems().addAll("PDF", "Image");

        // Add input validation listeners
        titleField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
        });

        descriptionField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
        });

        courseComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
        });

        formatComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
        });

        filePathField.textProperty().addListener((observable, oldValue, newValue) -> {
            validateInput();
        });
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCourses(ObservableList<Course> courses) {
        // Convert courses to names
        ObservableList<String> courseNames = FXCollections.observableArrayList();
        for (Course course : courses) {
            courseNames.add(course.getTitle());
        }
        courseComboBox.setItems(courseNames);
    }

    public void setResource(Resource resource) {
        this.resource = resource;

        if (resource != null) {
            titleField.setText(resource.getTitle());
            descriptionField.setText(resource.getDescription());

            // Check if the course is null before accessing it
            if (resource.getCourse() != null) {
                courseComboBox.setValue(resource.getCourse().getTitle());
            } else {
                courseComboBox.setValue("No course assigned"); // Or set to any default value
            }

            formatComboBox.setValue(resource.getFormat());
            filePathField.setText(resource.getFilePath());

            // Check if the creationDate is null
            if (resource.getCreationDate() != null) {
                creationDatePicker.setValue(resource.getCreationDate().toLocalDate());
            } else {
                creationDatePicker.setValue(LocalDate.now()); // Set to today's date if null
            }
        } else {
            // Handle the case where the resource is null (when adding a new resource)
            creationDatePicker.setValue(LocalDate.now()); // Set to today's date
        }
    }


    public boolean isOkClicked() {
        return okClicked;
    }

    public Resource getResource() {
        if (resource == null) {
            resource = new Resource();
        }

        resource.setTitle(titleField.getText());
        resource.setDescription(descriptionField.getText());

        // Find the course by name
        String courseName = courseComboBox.getValue();
        if (courseName != null) {
            List<Course> courses = courseService.rechercher();
            for (Course course : courses) {
                if (course.getTitle().equals(courseName)) {
                    resource.setCourse(course);
                    break;
                }
            }
        }

        resource.setFormat(formatComboBox.getValue());
        resource.setFilePath(filePathField.getText());
        resource.setCreationDate(Date.valueOf(creationDatePicker.getValue()));

        return resource;
    }

    @FXML
    private void handleBrowse() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Resource File");

        // Set file filters based on selected format
        String selectedFormat = formatComboBox.getValue();
        if (selectedFormat != null) {
            if (selectedFormat.equals("PDF")) {
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("PDF Files", "*.pdf")
                );
            } else if (selectedFormat.equals("Image")) {
                fileChooser.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
                );
            }
        }

        File selectedFile = fileChooser.showOpenDialog(dialogStage);
        if (selectedFile != null) {
            filePathField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            if (resource == null) {
                resource = new Resource();
            }

            resource.setTitle(titleField.getText());
            resource.setDescription(descriptionField.getText());

            // Find the course by name
            String courseName = courseComboBox.getValue();
            if (courseName != null) {
                List<Course> courses = courseService.rechercher();
                for (Course course : courses) {
                    if (course.getTitle().equals(courseName)) {
                        resource.setCourse(course);
                        break;
                    }
                }
            }

            resource.setFormat(formatComboBox.getValue());
            resource.setFilePath(filePathField.getText());
            resource.setCreationDate(Date.valueOf(creationDatePicker.getValue()));

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (titleField.getText() == null || titleField.getText().trim().isEmpty()) {
            errorMessage += "Title is required!\n";
        }

        if (descriptionField.getText() == null || descriptionField.getText().trim().isEmpty()) {
            errorMessage += "Description is required!\n";
        }

        if (courseComboBox.getValue() == null) {
            errorMessage += "Course is required!\n";
        }

        if (formatComboBox.getValue() == null) {
            errorMessage += "Format is required!\n";
        }

        if (filePathField.getText() == null || filePathField.getText().trim().isEmpty()) {
            errorMessage += "File path is required!\n";
        } else {
            File file = new File(filePathField.getText());
            if (!file.exists()) {
                errorMessage += "File does not exist!\n";
            } else {
                String format = formatComboBox.getValue();
                if (format != null) {
                    if (format.equals("PDF") && !filePathField.getText().toLowerCase().endsWith(".pdf")) {
                        errorMessage += "Selected file must be a PDF!\n";
                    } else if (format.equals("Image") && !isImageFile(filePathField.getText())) {
                        errorMessage += "Selected file must be an image!\n";
                    }
                }
            }
        }

        if (creationDatePicker.getValue() == null) {
            errorMessage += "Creation date is required!\n";
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }

    private void validateInput() {
        String errorMessage = "";

        if (titleField.getText() == null || titleField.getText().trim().isEmpty()) {
            errorMessage += "Title is required!\n";
        }

        if (descriptionField.getText() == null || descriptionField.getText().trim().isEmpty()) {
            errorMessage += "Description is required!\n";
        }

        if (courseComboBox.getValue() == null) {
            errorMessage += "Course is required!\n";
        }

        if (formatComboBox.getValue() == null) {
            errorMessage += "Format is required!\n";
        }

        if (filePathField.getText() == null || filePathField.getText().trim().isEmpty()) {
            errorMessage += "File path is required!\n";
        } else {
            File file = new File(filePathField.getText());
            if (!file.exists()) {
                errorMessage += "File does not exist!\n";
            } else {
                String format = formatComboBox.getValue();
                if (format != null) {
                    if (format.equals("PDF") && !filePathField.getText().toLowerCase().endsWith(".pdf")) {
                        errorMessage += "Selected file must be a PDF!\n";
                    } else if (format.equals("Image") && !isImageFile(filePathField.getText())) {
                        errorMessage += "Selected file must be an image!\n";
                    }
                }
            }
        }

        saveButton.setDisable(!errorMessage.isEmpty());
    }

    private boolean isImageFile(String filePath) {
        String lowerCasePath = filePath.toLowerCase();
        return lowerCasePath.endsWith(".jpg") ||
                lowerCasePath.endsWith(".jpeg") ||
                lowerCasePath.endsWith(".png") ||
                lowerCasePath.endsWith(".gif") ||
                lowerCasePath.endsWith(".bmp");
    }
} 