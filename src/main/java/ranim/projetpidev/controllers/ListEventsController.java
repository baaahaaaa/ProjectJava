package ranim.projetpidev.controllers;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ranim.projetpidev.entites.Event;
import ranim.projetpidev.services.EventService;

public class ListEventsController implements Initializable {

    @FXML private FlowPane eventsContainer;
    
    private EventService eventService;
    private ObservableList<Event> eventsList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        eventService = new EventService();
        loadEvents();
    }

    private void loadEvents() {
        List<Event> events = eventService.getAll();
        eventsList = FXCollections.observableArrayList(events);
        displayEventsAsCards();
    }

    private void displayEventsAsCards() {
        eventsContainer.getChildren().clear();
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (Event event : eventsList) {
            VBox card = new VBox();
            card.getStyleClass().add("event-card");
            card.setPrefWidth(220);
            card.setPrefHeight(280);
            card.setSpacing(10);
            card.setPadding(new Insets(15));
            
            Label titleLabel = new Label(event.getTitle());
            titleLabel.getStyleClass().add("card-title");
            
            Label typeLabel = new Label("Type: " + event.getType());
            Label locationLabel = new Label("Lieu: " + event.getLocation());
            Label dateLabel = new Label("Date: " + dateFormat.format(event.getDateEvent()));
            Label priceLabel = new Label(String.format("Prix: %.2f", event.getPrice()) + " dt");
            
            Label descriptionLabel = new Label(event.getDescription());
            descriptionLabel.setWrapText(true);
            descriptionLabel.getStyleClass().add("card-description");
            VBox.setVgrow(descriptionLabel, Priority.ALWAYS);
            
            HBox buttonsBox = new HBox();
            buttonsBox.setSpacing(10);
            buttonsBox.setAlignment(Pos.CENTER);
            
            Button updateButton = new Button("Modifier");
            updateButton.getStyleClass().add("update-button");
            updateButton.setOnAction(e -> handleUpdateEvent(event));
            
            Button deleteButton = new Button("Supprimer");
            deleteButton.getStyleClass().add("delete-button");
            deleteButton.setOnAction(e -> handleDeleteEvent(event));
            
            Button reserveButton = new Button("Réserver");
            reserveButton.getStyleClass().add("reserve-button");
            reserveButton.setOnAction(e -> handleReserveEvent(event));
            
            buttonsBox.getChildren().addAll(updateButton, deleteButton, reserveButton);
            
            card.getChildren().addAll(
                titleLabel, 
                typeLabel, 
                locationLabel, 
                dateLabel, 
                priceLabel, 
                descriptionLabel, 
                buttonsBox
            );
            
            eventsContainer.getChildren().add(card);
        }
    }

    @FXML
    public void handleRefresh() {
        loadEvents();
    }
    
    @FXML
    public void handleBack() {
        try {
            Stage currentStage = (Stage) eventsContainer.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors du retour à la vue précédente.");
        }
    }
    
    private void handleUpdateEvent(Event event) {
        boolean edited = EditEventController.showEditDialog(event);
        if (edited) {
            handleRefresh();  // Refresh the list if edited
        }
    }
    
    private void handleDeleteEvent(Event event) {
        try {
            eventService.delete(event);
            showAlert("Succès", "L'événement a été supprimé !");
            handleRefresh();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de supprimer l'événement.");
        }
    }
    
    private void handleReserveEvent(Event event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/reservation.fxml"));
            Parent root = loader.load();
            
            ReservationController controller = loader.getController();
            controller.setEvent(event);
            
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Réserver un événement");
            stage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir le formulaire de réservation: " + e.getMessage());
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