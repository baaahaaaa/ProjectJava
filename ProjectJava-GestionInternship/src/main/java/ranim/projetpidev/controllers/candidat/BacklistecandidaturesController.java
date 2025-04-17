package ranim.projetpidev.controllers.candidat;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ranim.projetpidev.entites.Candidat;
import ranim.projetpidev.services.CandidatService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class BacklistecandidaturesController {

    @FXML private TableView<Candidat> candidatTable;
    @FXML private TableColumn<Candidat, String> fullNameCol;
    @FXML private TableColumn<Candidat, String> emailCol;
    @FXML private TableColumn<Candidat, String> phoneCol;
    @FXML private TableColumn<Candidat, String> motivationCol;
    @FXML private TableColumn<Candidat, Boolean> resultCol;
    @FXML private TableColumn<Candidat, Void> actionsCol;
    @FXML private Button backBtn; // Add the back button in the controller

    private CandidatService candidatService = new CandidatService();

    @FXML
    public void initialize() {
        // Setup the columns for the table
        fullNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        motivationCol.setCellValueFactory(new PropertyValueFactory<>("applyingMotivation"));
        resultCol.setCellValueFactory(new PropertyValueFactory<>("result"));

        // Load existing candidates into the table
        loadCandidates();

        // Set the cell factory for the Actions column (show button)
        actionsCol.setCellFactory(param -> {
            TableCell<Candidat, Void> cell = new TableCell<Candidat, Void>() {
                private final Button showButton = new Button("Show");

                {
                    showButton.setOnAction(event -> {
                        Candidat candidat = getTableView().getItems().get(getIndex());
                        showCandidateDetails(candidat.getId());
                    });
                    HBox hbox = new HBox(10, showButton);
                    hbox.setAlignment(Pos.CENTER);
                    setGraphic(hbox);
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(new HBox(10, showButton));
                    }
                }
            };
            return cell;
        });
    }

    // Method to load candidates into the table
    private void loadCandidates() {
        try {
            List<Candidat> candidats = candidatService.getAll();  // Fetch all candidates
            candidatTable.getItems().setAll(candidats);  // Populate the table
        } catch (SQLException e) {
            showError("Error", "An error occurred while fetching the candidates.");
            e.printStackTrace();
        }
    }

    // Display error message
    private void showError(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Show candidate details in Backdetailscandidature
    private void showCandidateDetails(int candidatId) {
        try {
            // Fetch candidate details
            Candidat candidat = candidatService.getById(candidatId);

            // Load the details page for the candidate
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/candidat/Backdetailscandidature.fxml"));
            Parent root = loader.load();

            // Pass the candidate data to the next controller if needed
            BackdetailscandidatureController controller = loader.getController();
            controller.setCandidateDetails(candidat);

            // Show the details page
            Stage stage = (Stage) candidatTable.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Candidate Details");
            stage.show();
        } catch (IOException e) {
            showError("Error", "An error occurred while loading candidate details.");
            e.printStackTrace();
        }
    }

    // Back button functionality to navigate back to the previous page
    @FXML
    public void backToList() {
        try {
            // Load the Backlistecandidatures page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/internship/Backlisteinternship.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Candidates List");
            stage.show();
        } catch (IOException e) {
            showError("Error", "An error occurred while navigating back to the list.");
            e.printStackTrace();
        }
    }
}
