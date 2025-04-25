package ranim.projetpidev.entites;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashSet;
import java.util.Set;

public class Panier {

    private IntegerProperty id;

    private Set<Course> courses = new HashSet<>();

    private final ObservableList<Course> coursesObservable;

    public Panier() {
        this.id = new SimpleIntegerProperty();
        this.coursesObservable = FXCollections.observableArrayList();
    }

    public Panier(int id) {
        this.id = new SimpleIntegerProperty(id);
        this.coursesObservable = FXCollections.observableArrayList();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public ObservableList<Course> getCourses() {
        return coursesObservable;
    }

    public void setCourses(ObservableList<Course> courses) {
        this.coursesObservable.setAll(courses);
    }

    public void addCourse(Course course) {
        if (!coursesObservable.contains(course)) {
            coursesObservable.add(course);
        }
    }

    public void removeCourse(Course course) {
        coursesObservable.remove(course);
    }
}
