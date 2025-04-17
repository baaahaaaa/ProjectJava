package ranim.projetpidev.controllers;

import java.io.IOException;
import java.time.LocalDate;
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
import ranim.projetpidev.HelloApplication;
import ranim.projetpidev.entites.Event;
import ranim.projetpidev.services.EventService;

public class EditEventController {

    @FXML private TextField titleField;
    @FXML private TextArea descriptionField;
    @FXML private TextField typeField;
    @FXML private TextField locationField;
    @FXML private DatePicker dateEventPicker;
    @FXML private TextField priceField;

    private Event event;
    private EventService eventService;

    @FXML
    public void initialize() {
        eventService = new EventService();
    }
    
    /**
     * Static method to create and show the edit dialog
     * @param eventToEdit The event to edit
     * @return true if edited successfully, false otherwise
     */
    public static boolean showEditDialog(Event eventToEdit) {
        try {
            // Load the FXML
            FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("editEvent.fxml")
            );
            
            if (loader.getLocation() == null) {
                // Try with different path
                loader = new FXMLLoader(
                    HelloApplication.class.getResource("/ranim/projetpidev/editEvent.fxml")
                );
            }
            
            if (loader.getLocation() == null) {
                throw new IOException("Cannot locate editEvent.fxml");
            }
            
            Parent root = loader.load();
            
            // Get controller and set the event
            EditEventController controller = loader.getController();
            controller.setEvent(eventToEdit);
            
            // Create and configure the stage
            Stage stage = new Stage();
            stage.setTitle("Modifier l'événement");
            stage.setScene(new Scene(root));
            
            // Show the dialog and wait for it to close
            stage.showAndWait();
            
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText("Impossible d'ouvrir la vue de modification: " + e.getMessage());
            alert.showAndWait();
            
            return false;
        }
    }

    public void setEvent(Event event) {
        this.event = event;
        
        // Populate fields with event data
        titleField.setText(event.getTitle());
        descriptionField.setText(event.getDescription());
        typeField.setText(event.getType());
        locationField.setText(event.getLocation());
        
        // Convert java.sql.Date to LocalDate for DatePicker (safer conversion)
        if (event.getDateEvent() instanceof java.sql.Date) {
            // Convert SQL date directly to LocalDate
            java.sql.Date sqlDate = (java.sql.Date) event.getDateEvent();
            dateEventPicker.setValue(sqlDate.toLocalDate());
        } else {
            // Convert util.Date to LocalDate via Calendar
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(event.getDateEvent());
            LocalDate localDate = LocalDate.of(
                cal.get(java.util.Calendar.YEAR),
                cal.get(java.util.Calendar.MONTH) + 1,
                cal.get(java.util.Calendar.DAY_OF_MONTH)
            );
            dateEventPicker.setValue(localDate);
        }
        
        priceField.setText(String.valueOf(event.getPrice()));
    }

    @FXML
    public void handleSaveEvent() {
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

        // Update the event object
        event.setTitle(title);
        event.setDescription(description);
        event.setType(type);
        event.setLocation(location);
        event.setDateEvent(dateEvent);
        event.setPrice(price);

        // Save the updated event
        eventService.update(event);

        showAlert("Succès", "L'événement a été mis à jour !");
        
        // Close the window
        ((Stage) titleField.getScene().getWindow()).close();
    }

    @FXML
    public void handleCancel() {
        ((Stage) titleField.getScene().getWindow()).close();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
