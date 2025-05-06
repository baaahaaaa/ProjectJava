package ranim.projetpidev.controllers;

import ranim.projetpidev.entites.Course;
import ranim.projetpidev.services.CourseService;
import ranim.projetpidev.controllers.CourseDialogController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import ranim.projetpidev.services.RessourceService;

import java.io.IOException;
import java.sql.Date;

public class CourseController {

    @FXML
    private TableView<Course> courseTable;
    @FXML
    private TableColumn<Course, String> titleColumn;
    @FXML
    private TableColumn<Course, String> descriptionColumn;
    @FXML
    private TableColumn<Course, String> resourcesColumn;
    @FXML
    private TableColumn<Course, String> domainColumn;
    @FXML
    private TableColumn<Course, String> typeColumn;
    @FXML
    private TableColumn<Course, Double> priceColumn;
    @FXML
    private TableColumn<Course, Date> creationDateColumn;
    @FXML
    private TableColumn<Course, Void> actionsColumn;

    private final CourseService courseService = new CourseService();
    private final ObservableList<Course> courseList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTableColumns();
        loadCourses();
    }

    private void setupTableColumns() {
        titleColumn.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        domainColumn.setCellValueFactory(cellData -> cellData.getValue().domainProperty());
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        priceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        creationDateColumn.setCellValueFactory(cellData -> cellData.getValue().creationDateProperty());

        actionsColumn.setCellFactory(createActionsCellFactory());
    }

    private Callback<TableColumn<Course, Void>, TableCell<Course, Void>> createActionsCellFactory() {
        return param -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final Button viewButton = new Button("View Ressources");
            private final Button addButton = new Button("Add Ressource");

            {
                editButton.setOnAction(event -> handleEditCourse(getTableView().getItems().get(getIndex())));
                deleteButton.setOnAction(event -> handleDeleteCourse(getTableView().getItems().get(getIndex())));
                viewButton.setOnAction(event -> handleViewRessources(getTableView().getItems().get(getIndex())));
                addButton.setOnAction(event -> handleAddRessourceToCourse(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(5, editButton, deleteButton, viewButton, addButton));
                }
            }
        };
    }

    private void loadCourses() {
        courseList.setAll(courseService.rechercher());
        courseTable.setItems(courseList);
    }

    @FXML
    private void handleCreateCourse() {
        showCourseDialog(null);
    }

    private void showCourseDialog(Course course) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/CourseDialog.fxml"));
            VBox dialogPane = loader.load();
            CourseDialogController controller = loader.getController();

            // Set up the dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle(course == null ? "Create Course" : "Edit Course");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(courseTable.getScene().getWindow());
            dialogStage.setScene(new Scene(dialogPane));

            controller.setDialogStage(dialogStage);

            // Set the course if editing
            if (course != null) {
                controller.setCourse(course);
            }

            // Show the dialog and wait for response
            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                Course updatedCourse = controller.getCourse();
                if (updatedCourse != null) {
                    if (course == null) {
                        courseService.ajouter(updatedCourse);
                    } else {
                        courseService.modifier(updatedCourse);
                    }
                    loadCourses();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load the dialog");
            alert.setContentText("There was an error loading the course dialog.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleDeleteCourse(Course course) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Course");
        alert.setHeaderText("Are you sure you want to delete this course?");
        alert.setContentText("This action cannot be undone.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                courseService.supprimer(course);
                loadCourses();
            }
        });
    }

    @FXML
    private void handleEditCourse(Course course) {
        showCourseDialog(course);
    }

    @FXML
    private void handleViewRessources() {
        Course selectedCourse = courseTable.getSelectionModel().getSelectedItem();
        if (selectedCourse == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Aucun cours sélectionné");
            alert.setHeaderText(null);
            alert.setContentText("Veuillez sélectionner un cours pour voir ses ressources.");
            alert.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/RessourceView.fxml"));
            VBox root = loader.load();

            // On passe le cours sélectionné au contrôleur des ressources
            RessourceController controller = loader.getController();
            controller.setCourseContext(selectedCourse); // à créer côté RessourceController

            Stage stage = new Stage();
            stage.setTitle("Ressources du cours : " + selectedCourse.getTitle());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(courseTable.getScene().getWindow());
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible d'ouvrir les ressources");
            alert.setContentText("Une erreur est survenue lors du chargement de la vue des ressources.");
            alert.showAndWait();
        }
    }

    private void handleViewRessources(Course course) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/RessourceView.fxml"));
            VBox root = loader.load();

            RessourceController controller = loader.getController();
            controller.setCourseContext(course);

            Stage stage = new Stage();
            stage.setTitle("Ressources du cours : " + course.getTitle());
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(courseTable.getScene().getWindow());

            // Ajout d'un gestionnaire d'événements pour le bouton de fermeture
            stage.setOnCloseRequest(event -> {
                loadCourses(); // Rafraîchir la liste des cours
            });

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Impossible d'ouvrir les ressources");
            alert.setContentText("Une erreur est survenue lors du chargement de la vue des ressources.");
            alert.showAndWait();
        }
    }

    private void handleAddRessourceToCourse(Course course) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/RessourceDialog.fxml"));
            VBox dialogPane = loader.load();

            RessourceDialogController controller = loader.getController();
            ObservableList<Course> list = FXCollections.observableArrayList(course);
            controller.setCourses(list);

            Stage dialogStage = new Stage();
            controller.setDialogStage(dialogStage);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(courseTable.getScene().getWindow());
            dialogStage.setTitle("Ajouter une ressource à : " + course.getTitle());
            dialogStage.setScene(new Scene(dialogPane));
            dialogStage.showAndWait();

            if (controller.isOkClicked()) {
                RessourceService ressourceService = new RessourceService();
                ressourceService.ajouter(controller.getResource());
            }

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Erreur lors de l'ouverture du dialogue de ressource.");
            alert.showAndWait();
        }
    }
}
