package ranim.projetpidev.controllers;

import java.io.IOException;
import java.util.Date;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ranim.projetpidev.entites.Event;
import ranim.projetpidev.services.EventService;

public class AddEventController {

    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private TextField typeField;
    @FXML private TextField locationField;
    @FXML private DatePicker dateEventPicker;
    @FXML private TextField priceField;

    @FXML
    public void handleAddEvent() {
        String title = titleField.getText();
        String description = descriptionField.getText();
        String type = typeField.getText();
        String location = locationField.getText();
        Date dateEvent = java.sql.Date.valueOf(dateEventPicker.getValue());
        double price;

        try {
            price = Double.parseDouble(priceField.getText());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le prix doit être un nombre valide.");
            return;
        }

        if (title.isEmpty() || description.isEmpty() || type.isEmpty() || location.isEmpty() || dateEvent == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        Event newEvent = new Event(title, description, type, location, dateEvent, price);

        EventService eventService = new EventService();
        eventService.add(newEvent);

        showAlert("Succès", "L'événement a été ajouté !");

        titleField.clear();
        descriptionField.clear();
        typeField.clear();
        locationField.clear();
        dateEventPicker.setValue(null);
        priceField.clear();
    }

    @FXML
    public void handleShowEvents() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/listEvents.fxml"));
            Parent root = loader.load();

            ListEventsController controller = loader.getController();


            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des événements");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la liste des événements.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    @FXML
    public void handleShowReservation() {
        try {
            // Load the FXML for the reservation list page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/listReservations.fxml"));
            Parent root = loader.load(); // Load the FXML

            // Create a new stage for the listReservations.fxml
            Stage stage = new Stage();
            stage.setScene(new Scene(root)); // Set the scene with the loaded FXML
            stage.setTitle("Liste des réservations"); // Set the title of the window
            stage.show(); // Show the new window
        } catch (IOException e) {
            e.printStackTrace(); // Print error if the FXML file can't be loaded
            showAlert("Erreur", "Impossible d'ouvrir la liste des réservations.");
        }
    }

}
