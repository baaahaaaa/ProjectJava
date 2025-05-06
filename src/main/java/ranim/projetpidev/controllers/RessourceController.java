package ranim.projetpidev.controllers;

import ranim.projetpidev.entites.Course;
import ranim.projetpidev.entites.Ressource;
import ranim.projetpidev.services.CourseService;
import ranim.projetpidev.services.RessourceService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.List;

public class RessourceController {

    @FXML
    private TableView<Ressource> ressourceTable;
    @FXML
    private TableColumn<Ressource, String> titleColumn;
    @FXML
    private TableColumn<Ressource, String> descriptionColumn;
    @FXML
    private TableColumn<Ressource, String> formatColumn;
    @FXML
    private TableColumn<Ressource, Date> creationDateColumn;
    @FXML
    private TableColumn<Ressource, String> previewColumn;
    @FXML
    private TableColumn<Ressource, Void> actionsColumn;

    private final RessourceService ressourceService = new RessourceService();
    private final CourseService courseService = new CourseService();
    private final ObservableList<Ressource> ressourceList = FXCollections.observableArrayList();

    private Course courseContext;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadRessources();
    }

    public void setCourseContext(Course course) {
        this.courseContext = course;
        loadRessources();
    }

    private void setupTableColumns() {
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        formatColumn.setCellValueFactory(new PropertyValueFactory<>("format"));
        creationDateColumn.setCellValueFactory(new PropertyValueFactory<>("creationDate"));

        previewColumn.setCellFactory(column -> new TableCell<>() {
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
                    Ressource ressource = getTableView().getItems().get(getIndex());
                    if ("Image".equalsIgnoreCase(ressource.getFormat())) {
                        File file = new File(filePath);
                        if (file.exists()) {
                            Image image = new Image(file.toURI().toString());
                            imageView.setImage(image);
                            setGraphic(imageView);
                        } else {
                            label.setText("Image not found");
                            setGraphic(label);
                        }
                    } else if ("PDF".equalsIgnoreCase(ressource.getFormat())) {
                        try {
                            Image pdfIcon = new Image(getClass().getResourceAsStream("/ranim/projetpidev/images/logo.jpg"));
                            imageView.setImage(pdfIcon);
                            setGraphic(imageView);
                        } catch (Exception e) {
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

    private Callback<TableColumn<Ressource, Void>, TableCell<Ressource, Void>> createActionsCellFactory() {
        return param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");

            {
                editButton.setOnAction(event -> {
                    Ressource ressource = getTableView().getItems().get(getIndex());
                    showRessourceDialog(ressource);
                });

                deleteButton.setOnAction(event -> {
                    Ressource ressource = getTableView().getItems().get(getIndex());
                    handleDelete(ressource);
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

    private void loadRessources() {
        if (courseContext != null) {
            List<Ressource> ressources = ressourceService.getRessourcesByCourseId(courseContext.getId());
            ressourceList.setAll(ressources);
            ressourceTable.setItems(ressourceList);
        }
    }

    @FXML
    private void handleAddRessource() {
        if (courseContext == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Attention");
            alert.setHeaderText("Aucun cours sélectionné");
            alert.setContentText("Impossible d'ajouter une ressource sans cours associé.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/RessourceDialog.fxml"));
            VBox dialogPane = loader.load();

            RessourceDialogController controller = loader.getController();

            // On passe uniquement le cours courant
            ObservableList<Course> courseList = FXCollections.observableArrayList(courseContext);
            controller.setCourses(courseList);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Ajouter une ressource au cours : " + courseContext.getTitle());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(ressourceTable.getScene().getWindow());
            dialogStage.setScene(new Scene(dialogPane));
            controller.setDialogStage(dialogStage);

            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                Ressource ressource = controller.getResource();
                ressource.setCoursesId(courseContext.getId()); // S'assurer que le cours est bien associé
                ressourceService.ajouter(ressource);
                loadRessources(); // Recharger la liste
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur lors de l'ajout");
            alert.setContentText("Une erreur est survenue lors de l'ajout de la ressource.");
            alert.showAndWait();
        }
    }

    private void showRessourceDialog(Ressource ressource) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/RessourceDialog.fxml"));
            VBox dialogPane = loader.load();

            RessourceDialogController controller = loader.getController();
            List<Course> courses = courseService.rechercher();
            ObservableList<Course> observableCourses = FXCollections.observableArrayList(courses);
            controller.setCourses(observableCourses);

            if (ressource != null) {
                controller.setResource(ressource);
            }

            Stage dialogStage = new Stage();
            dialogStage.setTitle(ressource == null ? "Add Resource" : "Edit Resource");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(ressourceTable.getScene().getWindow());
            dialogStage.setScene(new Scene(dialogPane));
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                Ressource updated = controller.getResource();
                if (ressource == null) {
                    ressourceService.ajouter(updated);
                } else {
                    ressourceService.modifier(updated);
                }
                loadRessources();
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

    private void handleDelete(Ressource ressource) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Delete Resource");
        alert.setContentText("Are you sure you want to delete this resource?");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                ressourceService.supprimer(ressource);
                loadRessources();
            }
        });
    }

}
