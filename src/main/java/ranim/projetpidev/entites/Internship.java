package ranim.projetpidev.entites;

public class Internship {
    private int id;
    private int agentId;
    private String title;
    private String description;
    private String companyName;
    private String location;
    private String duration;               // Colonne "duration"
    private String requirements;
    private String otherRequirements;
    private String email;
    private String recompensationType;     // Colonne "recompensation_type"
    private double recompensationAmount;   // Colonne "recompensation_amount"
    private String currency;
    private double latitude;
    private double longitude;

    // Constructeur vide
    public Internship() { }

    // Constructeur complet
    public Internship(int id, int agentId, String title, String description, String companyName,
                      String location, String duration, String requirements, String otherRequirements,
                      String email, String recompensationType, double recompensationAmount,
                      String currency, double latitude, double longitude) {
        this.id = id;
        this.agentId = agentId;
        this.title = title;
        this.description = description;
        this.companyName = companyName;
        this.location = location;
        this.duration = duration;
        this.requirements = requirements;
        this.otherRequirements = otherRequirements;
        this.email = email;
        this.recompensationType = recompensationType;
        this.recompensationAmount = recompensationAmount;
        this.currency = currency;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters et Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getAgentId() { return agentId; }
    public void setAgentId(int agentId) { this.agentId = agentId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getDuration() { return duration; }
    public void setDuration(String duration) { this.duration = duration; }

    public String getRequirements() { return requirements; }
    public void setRequirements(String requirements) { this.requirements = requirements; }

    public String getOtherRequirements() { return otherRequirements; }
    public void setOtherRequirements(String otherRequirements) { this.otherRequirements = otherRequirements; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRecompensationType() { return recompensationType; }
    public void setRecompensationType(String recompensationType) { this.recompensationType = recompensationType; }

    public double getRecompensationAmount() { return recompensationAmount; }
    public void setRecompensationAmount(double recompensationAmount) { this.recompensationAmount = recompensationAmount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    @Override
    public String toString() {
        return "Internship{" +
                "id=" + id +
                ", agentId=" + agentId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", companyName='" + companyName + '\'' +
                ", location='" + location + '\'' +
                ", duration='" + duration + '\'' +
                ", requirements='" + requirements + '\'' +
                ", otherRequirements='" + otherRequirements + '\'' +
                ", email='" + email + '\'' +
                ", recompensationType='" + recompensationType + '\'' +
                ", recompensationAmount=" + recompensationAmount +
                ", currency='" + currency + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
