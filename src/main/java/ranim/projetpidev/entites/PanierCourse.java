package ranim.projetpidev.entites;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class PanierCourse {
    private final ObjectProperty<Panier> panier;
    private final ObjectProperty<Course> course;

    public PanierCourse() {
        this.panier = new SimpleObjectProperty<>();
        this.course = new SimpleObjectProperty<>();
    }

    public PanierCourse(Panier panier, Course course) {
        this.panier = new SimpleObjectProperty<>(panier);
        this.course = new SimpleObjectProperty<>(course);
    }

    public ObjectProperty<Panier> panierProperty() {
        return panier;
    }

    public Panier getPanier() {
        return panier.get();
    }

    public void setPanier(Panier panier) {
        this.panier.set(panier);
    }

    public ObjectProperty<Course> courseProperty() {
        return course;
    }

    public Course getCourse() {
        return course.get();
    }

    public void setCourse(Course course) {
        this.course.set(course);
    }
} 