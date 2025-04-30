package ranim.projetpidev.entites;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Admin extends User {
    public Admin() {
        super();
    }
    public Admin(int id, String firstName, String lastName, String email, LocalDate entryDate,
                 String password, String type, boolean is_active, String activation_code, LocalDateTime expiration_date) {
        super(id, firstName, lastName, email, entryDate, password, type,is_active,activation_code,expiration_date);
    }
}
