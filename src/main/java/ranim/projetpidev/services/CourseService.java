package ranim.projetpidev.services;

import ranim.projetpidev.entites.Course;
import ranim.projetpidev.entites.Event;
import ranim.projetpidev.entites.Reservation;
import ranim.projetpidev.tools.MyDataBase;
import java.util.List;
import java.util.ArrayList;

import java.sql.*;



public class CourseService implements IService<Course> {

    Connection connection = MyDataBase.getInstance().getCnx();

    @Override
    public void ajouter(Course course) {
        String req = "INSERT INTO course (title, description, domain, type, price, creation_date) VALUES (?,?,?,?,?,?)";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, course.getTitle());
            pst.setString(2, course.getDescription());
            pst.setString(3, course.getDomain());
            pst.setString(4, course.getType());
            pst.setObject(5, course.getPrice());
            pst.setDate(6, course.getCreationDate());
            pst.executeUpdate();
            System.out.println("Course ajoutée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void modifier(Course course) {
        String req = "UPDATE course SET title=?, description=?, domain=?, type=?, price=?, creation_date=? WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setString(1, course.getTitle());
            pst.setString(2, course.getDescription());
            pst.setString(3, course.getDomain());
            pst.setString(4, course.getType());
            pst.setObject(5, course.getPrice());
            pst.setDate(6, course.getCreationDate());
            pst.setInt(7, course.getId());
            pst.executeUpdate();
            System.out.println("Course modifiée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void supprimer(Course course) {
        String req = "DELETE FROM course WHERE id=?";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            pst.setInt(1, course.getId());
            pst.executeUpdate();
            System.out.println("Course supprimée");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public List<Course> rechercher() {
        List<Course> courses = new ArrayList<>();
        String req = "SELECT * FROM course";
        try {
            PreparedStatement pst = connection.prepareStatement(req);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                courses.add(new Course(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getString("domain"),
                    rs.getString("type"),
                    rs.getObject("price", Double.class),
                    rs.getDate("creation_date")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return courses;
    }
} 