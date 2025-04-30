package ranim.projetpidev.entites;

import java.time.LocalDate;

public class Promocode {

    private int id;
    private String code;
    private double discountValue;
    private LocalDate expiryDate;
    private boolean active;
    private int idStudent; // ID de l'étudiant

    public Promocode() {}

    public Promocode(int id, String code, double discountValue, LocalDate expiryDate,boolean active ,int idStudent) {
        this.id = id;
        this.code = code;
        this.discountValue = discountValue;
        this.expiryDate = expiryDate;
        this.idStudent = idStudent;
        this.active = active;
    }

    // Getter et setter pour chaque attribut
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getIdStudent() {
        return idStudent;
    }

    public void setIdStudent(int idStudent) {
        this.idStudent = idStudent;
    }
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }
}
