package ranim.projetpidev.entites;

public class Student extends User {

    public Student() {
        super();
    }

    public Student(int id, String firstName, String lastName, String email, java.time.LocalDate entryDate,
                   String password, String type) {
        super(id, firstName, lastName, email, entryDate, password, type);
    }

    // Si tu veux ajouter des champs spécifiques à Student, tu peux les ajouter ici
}
