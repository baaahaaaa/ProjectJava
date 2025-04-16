package ranim.projetpidev.controllers;

import ranim.projetpidev.entites.Course;
import ranim.projetpidev.entites.Resource;
import ranim.projetpidev.services.CourseService;
import ranim.projetpidev.services.ResourceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

public class ResourceController {
    @FXML
    private TableView<Resource> resourceTable;
    @FXML
    private TableColumn<Resource, String> titleColumn;
    @FXML
    private TableColumn<Resource, String> descriptionColumn;
    @FXML
    private TableColumn<Resource, String> formatColumn;
    @FXML
    private TableColumn<Resource, Date> creationDateColumn;
    @FXML
    private TableColumn<Resource, String> previewColumn;
    @FXML
    private TableColumn<Resource, Void> actionsColumn;

    private final ResourceService resourceService = new ResourceService();
    private final CourseService courseService = new CourseService();
    private final ObservableList<Resource> resourceList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadResources();
    }

    private void setupTableColumns() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        formatColumn.setCellValueFactory(new PropertyValueFactory<>("format"));
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        
        // Set up preview column
        previewColumn.setCellFactory(column -> new TableCell<Resource, String>() {
            private final ImageView imageView = new ImageView();
            private final Label label = new Label();

            {
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                imageView.setPreserveRatio(true);
            }

            @Override
            protected void updateItem(String filePath, boolean empty) {
                super.updateItem(filePath, empty);
                if (empty || filePath == null) {
                    setGraphic(null);
                } else {
                    Resource resource = getTableView().getItems().get(getIndex());
                    if (resource.getFormat().equalsIgnoreCase("Image")) {
                        try {
                            File file = new File(filePath);
                            if (file.exists()) {
                                Image image = new Image(file.toURI().toString());
                                imageView.setImage(image);
                                setGraphic(imageView);
                            } else {
                                label.setText("Image not found");
                                setGraphic(label);
                            }
                        } catch (Exception e) {
                            label.setText("Error loading image");
                            setGraphic(label);
                        }
                    } else if (resource.getFormat().equalsIgnoreCase("PDF")) {
                        try {
                            // Try to load the PDF icon from resources
                            Image pdfIcon = new Image(getClass().getResourceAsStream("/images/pdf-icon.png"));
                            imageView.setImage(pdfIcon);
                            setGraphic(imageView);
                        } catch (Exception e) {
                            // If PDF icon is not found, show a text label
                            label.setText("PDF");
                            setGraphic(label);
                        }
                    } else {
                        label.setText("Unknown format");
                        setGraphic(label);
                    }
                }
            }
        });

        actionsColumn.setCellFactory(createActionsCellFactory());
    }

    private Callback<TableColumn<Resource, Void>, TableCell<Resource, Void>> createActionsCellFactory() {
        return param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            
            {
                editButton.setOnAction(event -> {
                    Resource resource = getTableView().getItems().get(getIndex());
                    showResourceDialog(resource);
                });
                
                deleteButton.setOnAction(event -> {
                    Resource resource = getTableView().getItems().get(getIndex());
                    handleDelete(resource);
                });
            }
            
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, editButton, deleteButton));
                }
            }
        };
    }

    private void loadResources() {
        List<Resource> resources = resourceService.rechercher();
        resourceList.setAll(resources);
        resourceTable.setItems(resourceList);
    }

    @FXML
    private void handleAddResource() {
        showResourceDialog(null);
    }

    private void showResourceDialog(Resource resource) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/esprit/views/ResourceDialog.fxml"));
            VBox dialogPane = loader.load();
            
           ranim.projetpidev.controllers.ResourceDialogController controller = loader.getController();
            
            // Convert List to ObservableList
            List<Course> courses = courseService.rechercher();
            ObservableList<Course> observableCourses = FXCollections.observableArrayList(courses);
            controller.setCourses(observableCourses);
            
            if (resource != null) {
                controller.setResource(resource);
            }
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle(resource == null ? "Add Resource" : "Edit Resource");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(resourceTable.getScene().getWindow());
            dialogStage.setScene(new Scene(dialogPane));
            
            controller.setDialogStage(dialogStage);
            
            dialogStage.showAndWait();
            
            if (controller.isOkClicked()) {
                Resource updatedResource = controller.getResource();
                if (resource == null) {
                    resourceService.ajouter(updatedResource);
                } else {
                    resourceService.modifier(updatedResource);
                }
                loadResources();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load the dialog");
            alert.setContentText("There was an error loading the resource dialog.");
            alert.showAndWait();
        }
    }

    private void handleDelete(Resource resource) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Resource");
        alert.setContentText("Are you sure you want to delete this resource?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                resourceService.supprimer(resource);
                loadResources();
            }
        });
    }
} 