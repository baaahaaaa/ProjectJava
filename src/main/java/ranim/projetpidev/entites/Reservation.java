package ranim.projetpidev.entites;

public class Reservation {
    private int id;
    private int eventId;
    private int nbPlaces;
    private double totalPrice;
    private String phoneNumber;
    private String name;
    private String specialRequest;

    // Constructeur sans paramètres
    public Reservation() {}

    // Constructeur avec tous les paramètres
    public Reservation(int eventId, int nbPlaces, double totalPrice, String phoneNumber, String name, String specialRequest) {
        this.eventId = eventId;
        this.nbPlaces = nbPlaces;
        this.totalPrice = totalPrice;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.specialRequest = specialRequest;
    }

    // Constructeur avec l'ID et tous les autres paramètres
    public Reservation(int id, int eventId, int nbPlaces, double totalPrice, String phoneNumber, String name, String specialRequest) {
        this.id = id;
        this.eventId = eventId;
        this.nbPlaces = nbPlaces;
        this.totalPrice = totalPrice;
        this.phoneNumber = phoneNumber;
        this.name = name;
        this.specialRequest = specialRequest;
    }

    // Getters/Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getEventId() { return eventId; }
    public void setEventId(int eventId) { this.eventId = eventId; }

    public int getNbPlaces() { return nbPlaces; }
    public void setNbPlaces(int nbPlaces) { this.nbPlaces = nbPlaces; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSpecialRequest() { return specialRequest; }
    public void setSpecialRequest(String specialRequest) { this.specialRequest = specialRequest; }
}
