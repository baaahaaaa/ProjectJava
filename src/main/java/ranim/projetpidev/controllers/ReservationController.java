package ranim.projetpidev.controllers;

import java.text.SimpleDateFormat;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;
import ranim.projetpidev.entites.Event;
import ranim.projetpidev.entites.Reservation;
import ranim.projetpidev.services.ReservationService;

public class ReservationController {

    @FXML private Label eventTitleLabel;
    @FXML private Label eventDateLabel;
    @FXML private Label eventLocationLabel;
    @FXML private Label eventPriceLabel;
    
    @FXML private TextField nameField;
    @FXML private TextField phoneNumberField;
    @FXML private Spinner<Integer> nbPlacesSpinner;
    @FXML private Label totalPriceLabel;
    @FXML private TextArea specialRequestField;
    @FXML private Text statusText;
    
    private Event event;
    private ReservationService reservationService;
    private double totalPrice = 0.0;
    
    @FXML
    public void initialize() {
        reservationService = new ReservationService();
        
        // Initialize spinner for number of places
        SpinnerValueFactory<Integer> valueFactory = 
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1);
        nbPlacesSpinner.setValueFactory(valueFactory);
        
        // Make spinner editable
        TextFormatter<Integer> formatter = new TextFormatter<>(
                new IntegerStringConverter(), 
                1, 
                change -> {
                    String newText = change.getControlNewText();
                    if (newText.matches("([1-9][0-9]*)?")) {
                        return change;
                    }
                    return null;
                });
        
        nbPlacesSpinner.getEditor().setTextFormatter(formatter);
        
        // Add listener to automatically calculate price when number of places changes
        nbPlacesSpinner.valueProperty().addListener((obs, oldValue, newValue) -> {
            calculateTotalPrice();
        });
        
        totalPriceLabel.setText("0.00 dt");
    }
    
    /**
     * Set the event to be reserved
     * @param event The event
     */
    public void setEvent(Event event) {
        this.event = event;
        
        // Display event details
        eventTitleLabel.setText(event.getTitle());
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        eventDateLabel.setText(dateFormat.format(event.getDateEvent()));
        
        eventLocationLabel.setText(event.getLocation());
        eventPriceLabel.setText(String.format("%.2f dt", event.getPrice()));
        
        // Calculate initial price
        calculateTotalPrice();
    }
    
    @FXML
    public void handleCalculatePrice() {
        calculateTotalPrice();
    }
    
    private void calculateTotalPrice() {
        if (event != null) {
            int nbPlaces = nbPlacesSpinner.getValue();
            totalPrice = reservationService.calculateTotalPrice(event.getId(), nbPlaces);
            totalPriceLabel.setText(String.format("%.2f dt", totalPrice));
        }
    }
    
    @FXML
    public void handleReservation() {
        // Validate inputs
        if (validateInputs()) {
            // Create reservation
            Reservation reservation = new Reservation(
                    event.getId(),
                    nbPlacesSpinner.getValue(),
                    totalPrice,
                    phoneNumberField.getText(),
                    nameField.getText(),
                    specialRequestField.getText()
            );
            
            // Save reservation
            try {
                reservationService.add(reservation);
                showStatus("Réservation effectuée avec succès!", true);
                
                // Wait a moment before closing
                Thread.sleep(1500);
                handleCancel();
            } catch (Exception e) {
                showStatus("Erreur lors de la réservation: " + e.getMessage(), false);
            }
        }
    }
    
    @FXML
    public void handleCancel() {
        // Close the form
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
    
    private boolean validateInputs() {
        // Reset status
        statusText.setText("");
        
        // Validate name
        if (nameField.getText().trim().isEmpty()) {
            showStatus("Veuillez entrer votre nom", false);
            nameField.requestFocus();
            return false;
        }
        
        // Validate phone number
        if (phoneNumberField.getText().trim().isEmpty()) {
            showStatus("Veuillez entrer votre numéro de téléphone", false);
            phoneNumberField.requestFocus();
            return false;
        }
        
        // Validate number of places
        if (nbPlacesSpinner.getValue() < 1) {
            showStatus("Le nombre de places doit être au moins 1", false);
            nbPlacesSpinner.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void showStatus(String message, boolean success) {
        statusText.setText(message);
        statusText.setFill(success ? Color.GREEN : Color.RED);
    }
} 