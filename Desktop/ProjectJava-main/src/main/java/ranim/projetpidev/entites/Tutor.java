package ranim.projetpidev.entites;

public class Tutor extends User {
    private String domain;

    public Tutor() {
        super();
    }

    public Tutor(int id, String firstName, String lastName, String email, java.time.LocalDate entryDate,
                 String password, String type, String domain) {
        super(id, firstName, lastName, email, entryDate, password, type);
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }
}
