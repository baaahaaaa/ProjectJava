package ranim.projetpidev.entites;

import javafx.beans.property.*;

public class Internship {

    private IntegerProperty id;
    private IntegerProperty agentId;
    private StringProperty title;
    private StringProperty description;
    private StringProperty companyName;
    private StringProperty location;
    private StringProperty duration;   // Colonne "duration"
    private StringProperty requirements;
    private StringProperty otherRequirements;
    private StringProperty email;
    private StringProperty recompensationType; // Colonne "recompensation_type"
    private DoubleProperty recompensationAmount; // Colonne "recompensation_amount"
    private StringProperty currency;
    private DoubleProperty latitude;
    private DoubleProperty longitude;

    public Internship() {
        this.id = new SimpleIntegerProperty();
        this.agentId = new SimpleIntegerProperty();
        this.title = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.companyName = new SimpleStringProperty();
        this.location = new SimpleStringProperty();
        this.duration = new SimpleStringProperty();
        this.requirements = new SimpleStringProperty();
        this.otherRequirements = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.recompensationType = new SimpleStringProperty();
        this.recompensationAmount = new SimpleDoubleProperty();
        this.currency = new SimpleStringProperty();
        this.latitude = new SimpleDoubleProperty();
        this.longitude = new SimpleDoubleProperty();
    }

    // Constructeur avec tous les paramètres
    public Internship(int id, int agentId, String title, String description, String companyName, String location,
                      String duration, String requirements, String otherRequirements, String email,
                      String recompensationType, double recompensationAmount, String currency, double latitude, double longitude) {
        this.id = new SimpleIntegerProperty(id);
        this.agentId = new SimpleIntegerProperty(agentId);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.companyName = new SimpleStringProperty(companyName);
        this.location = new SimpleStringProperty(location);
        this.duration = new SimpleStringProperty(duration);
        this.requirements = new SimpleStringProperty(requirements);
        this.otherRequirements = new SimpleStringProperty(otherRequirements);
        this.email = new SimpleStringProperty(email);
        this.recompensationType = new SimpleStringProperty(recompensationType);
        this.recompensationAmount = new SimpleDoubleProperty(recompensationAmount);
        this.currency = new SimpleStringProperty(currency);
        this.latitude = new SimpleDoubleProperty(latitude);
        this.longitude = new SimpleDoubleProperty(longitude);
    }

    // Getters et setters avec Properties
    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty agentIdProperty() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId.set(agentId);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty companyNameProperty() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName.set(companyName);
    }

    public StringProperty locationProperty() {
        return location;
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public StringProperty durationProperty() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration.set(duration);
    }

    public StringProperty requirementsProperty() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements.set(requirements);
    }

    public StringProperty otherRequirementsProperty() {
        return otherRequirements;
    }

    public void setOtherRequirements(String otherRequirements) {
        this.otherRequirements.set(otherRequirements);
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public StringProperty recompensationTypeProperty() {
        return recompensationType;
    }

    public void setRecompensationType(String recompensationType) {
        this.recompensationType.set(recompensationType);
    }

    public DoubleProperty recompensationAmountProperty() {
        return recompensationAmount;
    }

    public void setRecompensationAmount(double recompensationAmount) {
        this.recompensationAmount.set(recompensationAmount);
    }

    public StringProperty currencyProperty() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency.set(currency);
    }

    public DoubleProperty latitudeProperty() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude.set(latitude);
    }

    public DoubleProperty longitudeProperty() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude.set(longitude);
    }

    // Getters traditionnels
    public int getId() {
        return id.get();
    }

    public int getAgentId() {
        return agentId.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getDescription() {
        return description.get();
    }

    public String getCompanyName() {
        return companyName.get();
    }

    public String getLocation() {
        return location.get();
    }

    public String getDuration() {
        return duration.get();
    }

    public String getRequirements() {
        return requirements.get();
    }

    public String getOtherRequirements() {
        return otherRequirements.get();
    }

    public String getEmail() {
        return email.get();
    }

    public String getRecompensationType() {
        return recompensationType.get();
    }

    public double getRecompensationAmount() {
        return recompensationAmount.get();
    }

    public String getCurrency() {
        return currency.get();
    }

    public double getLatitude() {
        return latitude.get();
    }

    public double getLongitude() {
        return longitude.get();
    }
}
