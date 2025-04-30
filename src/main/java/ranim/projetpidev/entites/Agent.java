package ranim.projetpidev.entites;

import java.time.LocalDateTime;

public class Agent extends User {
    private String companyName;
    private String location;

    public Agent() {
        super();
    }

    public Agent(int id, String firstName, String lastName, String email, java.time.LocalDate entryDate,
                 String password, String type, boolean is_active, String activation_code, LocalDateTime expiration_date, String companyName, String location) {
        super(id, firstName, lastName, email, entryDate, password, type,is_active,activation_code,expiration_date);
        this.companyName = companyName;
        this.location = location;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
