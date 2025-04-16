package ranim.projetpidev.entites;

import javafx.beans.property.*;

import java.sql.Date;

public class Course {
    private final IntegerProperty id;
    private final StringProperty title;
    private final StringProperty description;
    private final StringProperty domain;
    private final StringProperty type;
    private final DoubleProperty price;
    private final ObjectProperty<Date> creationDate;

    public Course() {
        this.id = new SimpleIntegerProperty();
        this.title = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.domain = new SimpleStringProperty();
        this.type = new SimpleStringProperty();
        this.price = new SimpleDoubleProperty(0.0); // Default to 0.0 instead of null
        this.creationDate = new SimpleObjectProperty<>();
    }

    public Course(int id, String title, String description, String domain, String type, Double price, Date creationDate) {
        this.id = new SimpleIntegerProperty(id);
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.domain = new SimpleStringProperty(domain);
        this.type = new SimpleStringProperty(type);
        this.price = new SimpleDoubleProperty(price != null ? price : 0.0); // Handle null price
        this.creationDate = new SimpleObjectProperty<>(creationDate);
    }

    public Course(String title, String description, String domain, String type, Double price, Date creationDate) {
        this.id = new SimpleIntegerProperty(); // Initialize id property
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.domain = new SimpleStringProperty(domain);
        this.type = new SimpleStringProperty(type);
        this.price = new SimpleDoubleProperty(price != null ? price : 0.0);
        this.creationDate = new SimpleObjectProperty<>(creationDate);
    }

    // Property getters
    public IntegerProperty idProperty() {
        return id;
    }

    public StringProperty titleProperty() {
        return title;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public StringProperty domainProperty() {
        return domain;
    }

    public StringProperty typeProperty() {
        return type;
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public ObjectProperty<Date> creationDateProperty() {
        return creationDate;
    }

    // Regular getters
    public int getId() {
        return id.get();
    }

    public String getTitle() {
        return title.get();
    }

    public String getDescription() {
        return description.get();
    }

    public String getDomain() {
        return domain.get();
    }

    public String getType() {
        return type.get();
    }

    public double getPrice() {
        return price.get();
    }

    public Date getCreationDate() {
        return creationDate.get();
    }

    // Setters
    public void setId(int id) {
        this.id.set(id);
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public void setDomain(String domain) {
        this.domain.set(domain);
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public void setPrice(Double price) {
        this.price.set(price != null ? price : 0.0);
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate.set(creationDate);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id.get() +
                ", title=" + title.get() +
                ", description=" + description.get() +
                ", domain=" + domain.get() +
                ", type=" + type.get() +
                ", price=" + price.get() +
                ", creationDate=" + creationDate.get() +
                '}';
    }
} 