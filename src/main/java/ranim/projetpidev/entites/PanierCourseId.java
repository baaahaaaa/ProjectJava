package ranim.projetpidev.entites;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class PanierCourseId {
    private final IntegerProperty panierId;
    private final IntegerProperty courseId;

    public PanierCourseId() {
        this.panierId = new SimpleIntegerProperty();
        this.courseId = new SimpleIntegerProperty();
    }

    public PanierCourseId(int panierId, int courseId) {
        this.panierId = new SimpleIntegerProperty(panierId);
        this.courseId = new SimpleIntegerProperty(courseId);
    }

    public IntegerProperty panierIdProperty() {
        return panierId;
    }

    public int getPanierId() {
        return panierId.get();
    }

    public void setPanierId(int panierId) {
        this.panierId.set(panierId);
    }

    public IntegerProperty courseIdProperty() {
        return courseId;
    }

    public int getCourseId() {
        return courseId.get();
    }

    public void setCourseId(int courseId) {
        this.courseId.set(courseId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PanierCourseId that = (PanierCourseId) o;
        return panierId.get() == that.panierId.get() && courseId.get() == that.courseId.get();
    }

    @Override
    public int hashCode() {
        return 31 * panierId.get() + courseId.get();
    }
} 