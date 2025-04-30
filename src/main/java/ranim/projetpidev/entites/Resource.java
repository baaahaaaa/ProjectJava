package ranim.projetpidev.entites;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.ObjectProperty;

import java.sql.Date;

public class Resource {
    private final StringProperty title;
    private final StringProperty description;
    private final StringProperty format;
    private final ObjectProperty<Date> creationDate;
    private final StringProperty filePath;
    private int id;
    private int coursesId;
    private Course course;

    public Resource() {
        this.title = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
        this.format = new SimpleStringProperty();
        this.creationDate = new SimpleObjectProperty<>();
        this.filePath = new SimpleStringProperty();
    }

    public Resource(String title, String description, String format, Date creationDate, String filePath, Course course) {
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);
        this.format = new SimpleStringProperty(format);
        this.creationDate = new SimpleObjectProperty<>(creationDate);
        this.filePath = new SimpleStringProperty(filePath);
        this.course = course;
        if (course != null) {
            this.coursesId = course.getId();
        }
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCoursesId() {
        return coursesId;
    }

    public void setCoursesId(int coursesId) {
        this.coursesId = coursesId;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public StringProperty titleProperty() {
        return title;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getFormat() {
        return format.get();
    }

    public void setFormat(String format) {
        this.format.set(format);
    }

    public StringProperty formatProperty() {
        return format;
    }

    public Date getCreationDate() {
        return creationDate.get();
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate.set(creationDate);
    }

    public ObjectProperty<Date> creationDateProperty() {
        return creationDate;
    }

    public String getFilePath() {
        return filePath.get();
    }

    public void setFilePath(String filePath) {
        this.filePath.set(filePath);
    }

    public StringProperty filePathProperty() {
        return filePath;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
        if (course != null) {
            this.coursesId = course.getId();
        }
    }
}
