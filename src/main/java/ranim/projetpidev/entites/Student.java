package ranim.projetpidev.entites;

import java.util.List;
import java.time.LocalDate;

public class Student extends User {

    private List<Promocode> promocodes; // Liste des codes promo associés à l'étudiant

    public Student() {
        super();
    }

    public Student(int id, String firstName, String lastName, String email, LocalDate entryDate,
                   String password, String type) {
        super(id, firstName, lastName, email, entryDate, password, type);
    }

    // Getter et setter pour les codes promo
    public List<Promocode> getPromocodes() {
        return promocodes;
    }

    public void setPromocodes(List<Promocode> promocodes) {
        this.promocodes = promocodes;
    }
}
