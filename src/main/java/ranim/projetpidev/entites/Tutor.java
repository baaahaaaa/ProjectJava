package ranim.projetpidev.entites;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Tutor extends User {
    private String domain;

    public Tutor() {
        super();
    }

    public Tutor(int id, String firstName, String lastName, String email, java.time.LocalDate entryDate,
                 String password, String type, boolean is_active, String activation_code, LocalDateTime expiration_date, String domain) {
        super(id, firstName, lastName, email, entryDate, password, type,is_active,activation_code,expiration_date);
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
