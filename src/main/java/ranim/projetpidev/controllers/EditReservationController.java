package ranim.projetpidev.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ranim.projetpidev.entites.Reservation;
import ranim.projetpidev.services.ReservationService;

public class EditReservationController {

    @FXML private TextField nbPlacesField;
    @FXML private TextField totalPriceField;
    @FXML private TextField phoneNumberField;
    @FXML private TextField nameField;
    @FXML private TextField specialRequestField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private Reservation reservation;
    private ReservationService reservationService;
    private ListReservationsController listReservationsController;

    public void initialize() {
        reservationService = new ReservationService();

        // Set up button actions
        saveButton.setOnAction(event -> handleSave());
        cancelButton.setOnAction(event -> handleCancel());

        // Add listener to automatically calculate price when number of places changes
        nbPlacesField.textProperty().addListener((observable, oldValue, newValue) -> calculateTotalPrice());
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        populateFields();
    }

    public void setListReservationsController(ListReservationsController controller) {
        this.listReservationsController = controller;
    }

    private void populateFields() {
        if (reservation != null) {
            nbPlacesField.setText(String.valueOf(reservation.getNbPlaces()));
            totalPriceField.setText(String.valueOf(reservation.getTotalPrice()));
            phoneNumberField.setText(reservation.getPhoneNumber());
            nameField.setText(reservation.getName());
            specialRequestField.setText(reservation.getSpecialRequest());
        }
    }

    @FXML
    private void handleSave() {
        try {
            // Validate fields
            if (nbPlacesField.getText().isEmpty() || phoneNumberField.getText().isEmpty()
                    || nameField.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs obligatoires.");
                return;
            }

            int nbPlaces = Integer.parseInt(nbPlacesField.getText());
            double totalPrice = Double.parseDouble(totalPriceField.getText());

            // Update reservation object
            reservation.setNbPlaces(nbPlaces);
            reservation.setTotalPrice(totalPrice);
            reservation.setPhoneNumber(phoneNumberField.getText());
            reservation.setName(nameField.getText());
            reservation.setSpecialRequest(specialRequestField.getText());

            // Save to database
            reservationService.update(reservation);

            // Refresh the table in the list view
            if (listReservationsController != null) {
                listReservationsController.handleRefresh();
            }

            // Show success message
            showAlert(Alert.AlertType.INFORMATION, "Succès", "La réservation a été mise à jour avec succès.");

            // Close the window
            closeWindow();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez saisir des valeurs numériques valides pour le nombre de places et le prix total.");
        }
    }

    @FXML
    private void handleCancel() {
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void calculateTotalPrice() {
        if (nbPlacesField.getText().isEmpty()) return;

        try {
            int nbPlaces = Integer.parseInt(nbPlacesField.getText());
            double totalPrice = reservationService.calculateTotalPrice(reservation.getEventId(), nbPlaces);
            totalPriceField.setText(String.format("%.2f", totalPrice));
        } catch (NumberFormatException e) {
            totalPriceField.setText("0.00");
        }
    }
}
