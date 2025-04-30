package ranim.projetpidev.controllers.internship;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ranim.projetpidev.entites.Internship;
import ranim.projetpidev.services.InternshipService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

public class BacklisteinternshipController {

    @FXML private TableView<Internship> internshipTable;
    @FXML private TableColumn<Internship, String> companyNameCol;
    @FXML private TableColumn<Internship, String> titleCol;
    @FXML private TableColumn<Internship, String> locationCol;
    @FXML private TableColumn<Internship, String> recompensationCol;
    @FXML private TableColumn<Internship, Void> actionsCol;
    @FXML private ImageView logoImage;  // Correcting the image ID to match the FXML

    private InternshipService internshipService = new InternshipService();

    @FXML
    public void initialize() {
        companyNameCol.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
        recompensationCol.setCellValueFactory(new PropertyValueFactory<>("recompensationType"));

        // Add action buttons manually to each row
        actionsCol.setCellFactory(param -> new TableCell<Internship, Void>() {
            private final Button showButton = new Button("Show");
            private final Button modifyButton = new Button("Modify");
            private final Button deleteButton = new Button("Delete");

            {
                // Button actions
                showButton.setOnAction(event -> {
                    Internship internship = getTableView().getItems().get(getIndex());
                    showInternshipDetails(internship.getId());
                });

                modifyButton.setOnAction(event -> {
                    Internship internship = getTableView().getItems().get(getIndex());
                    modifyInternship(internship.getId());
                });

                deleteButton.setOnAction(event -> {
                    Internship internship = getTableView().getItems().get(getIndex());
                    deleteInternship(internship.getId());
                });

                // Assigning CSS classes for button colors
                showButton.getStyleClass().add("button-pink");   // Blue for Show
                modifyButton.getStyleClass().add("button-orange");  // Orange for Modify
                deleteButton.getStyleClass().add("button-blue");    // Red for Delete

                // Adding buttons to the HBox
                HBox hbox = new HBox(10, showButton, modifyButton, deleteButton);
                hbox.setAlignment(Pos.CENTER);
                setGraphic(hbox);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(new HBox(10, showButton, modifyButton, deleteButton));
                }
            }
        });

        // Initialize table with internships
        refreshInternships();
    }

    // Show details of the selected internship
    public void showInternshipDetails(int internshipId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/internship/Backinternshipdetails.fxml"));
            Parent root = loader.load();
            BackinternshipdetailsController controller = loader.getController();
            controller.setInternshipId(internshipId);
            Stage stage = (Stage) internshipTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Internship Details");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Modify the selected internship
    public void modifyInternship(int internshipId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/internship/Backupdateinternship.fxml"));
            Parent root = loader.load();
            BackupdateinternshipController controller = loader.getController();
            controller.setInternshipId(internshipId);
            Stage stage = (Stage) internshipTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Modify Internship");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Delete the selected internship
    public void deleteInternship(int internshipId) {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Internship");
            alert.setHeaderText("Are you sure you want to delete this internship?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                internshipService.delete(internshipId); // Delete internship logic
                refreshInternships(); // Refresh the table after deletion
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Refresh the table data
    public void refreshButtonClicked(ActionEvent event) {
        refreshInternships();
    }

    private void refreshInternships() {
        try {
            internshipTable.getItems().setAll(internshipService.getAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Open the "Add New Internship" form
    @FXML
    public void addNewInternship(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/internship/Backaddinternship.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Add New Internship");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Navigate to Frontlisteinternships.fxml when image is clicked
    @FXML
    public void navigateToFrontListeInternships() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/internship/Frontlisteinternships.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoImage.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Internships List");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
