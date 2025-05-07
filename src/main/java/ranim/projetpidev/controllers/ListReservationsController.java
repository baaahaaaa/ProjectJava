package ranim.projetpidev.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import ranim.projetpidev.entites.Reservation;
import ranim.projetpidev.services.ReservationService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ListReservationsController implements Initializable {

    @FXML private TableView<Reservation> reservationsTable;
   /* @FXML private TableColumn<Reservation, Integer> idColumn;
    @FXML private TableColumn<Reservation, Integer> eventIdColumn;*/
    @FXML private TableColumn<Reservation, Integer> nbPlacesColumn;
    @FXML private TableColumn<Reservation, Double> totalPriceColumn;
    @FXML private TableColumn<Reservation, String> phoneNumberColumn;
    @FXML private TableColumn<Reservation, String> nameColumn;
    @FXML private TableColumn<Reservation, String> specialRequestColumn;
    @FXML private TableColumn<Reservation, Void> actionColumn;

    private ReservationService reservationService;
    private ObservableList<Reservation> reservationsList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        reservationService = new ReservationService();
        loadReservations();
        setupActionColumn();
    }

    private void loadReservations() {
        List<Reservation> reservations = reservationService.getAll();
        reservationsList = FXCollections.observableArrayList(reservations);

        // Set the cell value factory for each column
       /* idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        eventIdColumn.setCellValueFactory(new PropertyValueFactory<>("eventId"));*/
        nbPlacesColumn.setCellValueFactory(new PropertyValueFactory<>("nbPlaces"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        specialRequestColumn.setCellValueFactory(new PropertyValueFactory<>("specialRequest"));

        // Set the data for the table
        reservationsTable.setItems(reservationsList);
    }

    private void setupActionColumn() {
        Callback<TableColumn<Reservation, Void>, TableCell<Reservation, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Reservation, Void> call(final TableColumn<Reservation, Void> param) {
                return new TableCell<>() {
                    private final Button modifyBtn = new Button("Modifier");
                    private final Button deleteBtn = new Button("Supprimer");
                    private final HBox pane = new HBox(5, modifyBtn, deleteBtn);

                    {
                        // Style buttons
                        modifyBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                        deleteBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");
                        pane.setPadding(new Insets(5));

                        // Set button actions
                        deleteBtn.setOnAction(event -> {
                            Reservation reservation = getTableView().getItems().get(getIndex());
                            handleDeleteReservation(reservation);
                        });

                        modifyBtn.setOnAction(event -> {
                            Reservation reservation = getTableView().getItems().get(getIndex());
                            handleModifyReservation(reservation);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(pane);
                        }
                    }
                };
            }
        };

        actionColumn.setCellFactory(cellFactory);
    }

    private void handleDeleteReservation(Reservation reservation) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmation de suppression");
        confirmDialog.setHeaderText("Êtes-vous sûr de vouloir supprimer cette réservation?");
        confirmDialog.setContentText("Cette action ne peut pas être annulée.");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Delete the reservation
            reservationService.delete(reservation);
            // Refresh the table
            loadReservations();
            showAlert("Succès", "Réservation supprimée avec succès.");
        }
    }

    private void handleModifyReservation(Reservation reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/editReservation.fxml"));
            Parent root = loader.load();

            // Pass the reservation to the edit controller
            EditReservationController controller = loader.getController();
            controller.setReservation(reservation);
            controller.setListReservationsController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier la réservation");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la vue de modification.");
        }
    }

    @FXML
    public void handleRefresh() {
        loadReservations();
    }

    @FXML
    public void handleBack() {
        // Go back to previous screen
        // Close the current stage or go back to the previous scene
        try {
            // Close the current window
            Stage currentStage = (Stage) reservationsTable.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du retour à la vue précédente.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


}
