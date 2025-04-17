package ranim.projetpidev.entites;

import java.util.Date;

public class Event {
    private int id;
    private String title;
    private String description;
    private String type;
    private String location;
    private Date dateEvent;
    private double price;

    // Constructeur sans paramètres
    public Event() {}

    // Constructeur avec tous les paramètres
    public Event(String title, String description, String type, String location, Date dateEvent, double price) {
        this.title = title;
        this.description = description;
        this.type = type;
        this.location = location;
        this.dateEvent = dateEvent;
        this.price = price;
    }

    // Constructeur avec l'ID et tous les autres paramètres
    public Event(int id, String title, String description, String type, String location, Date dateEvent, double price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.location = location;
        this.dateEvent = dateEvent;
        this.price = price;
    }

    // Getters/Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Date getDateEvent() { return dateEvent; }
    public void setDateEvent(Date dateEvent) { this.dateEvent = dateEvent; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
