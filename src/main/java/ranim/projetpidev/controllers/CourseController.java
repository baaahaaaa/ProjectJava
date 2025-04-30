package ranim.projetpidev.controllers;
import ranim.projetpidev.entites.Course;

import ranim.projetpidev.services.CourseService;
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

import java.io.IOException;
import java.sql.Date;
import java.util.List;

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
        return new Callback<TableColumn<Course, Void>, TableCell<Course, Void>>() {
            @Override
            public TableCell<Course, Void> call(final TableColumn<Course, Void> param) {
                return new TableCell<Course, Void>() {
                    private final Button editButton = new Button("Edit");
                    private final Button deleteButton = new Button("Delete");
                    private final Button resourceButton = new Button("View Resources");

                    {
                        editButton.setOnAction(event -> {
                            Course course = getTableView().getItems().get(getIndex());
                            handleEditCourse(course);
                        });

                        deleteButton.setOnAction(event -> {
                            Course course = getTableView().getItems().get(getIndex());
                            handleDeleteCourse(course);
                        });

                        resourceButton.setOnAction(event -> {
                            int courseId = getTableView().getItems().get(getIndex()).getId();
                            System.out.println(":::::::::::course IDD   "+courseId);
                            //push to fxml and display course with id : courseId
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(new HBox(5, editButton, deleteButton,resourceButton));
                        }
                    }
                };
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
} 