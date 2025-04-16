package ranim.projetpidev.entites;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class User  {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate entryDate;
    private String password;
    private String type;

    public User() {}
    public User(int id, String firstName, String lastName, String email, LocalDate entryDate, String password, String type) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.entryDate = entryDate;
        this.password = password;
        this.type = type;

    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public LocalDate getEntryDate() {
        return entryDate;
    }
    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

}
