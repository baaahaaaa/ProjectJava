package ranim.projetpidev.services;

import ranim.projetpidev.entites.Panier;
import ranim.projetpidev.entites.Course;
import ranim.projetpidev.tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PanierService {
    private Connection connection;

    public PanierService() {
        connection = MyDataBase.getInstance().getCnx();
    }

    public int getPanierIdByUserId(int userId) {
        String query = "SELECT id FROM panier WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int creerPanierPourUser(int userId) {
        String query = "INSERT INTO panier (user_id) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void ajouterCourseAuPanier(int panierId, int courseId) {
        String query = "INSERT INTO panier_course (panier_id, course_id, is_paid) VALUES (?, ?, 1)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, panierId);
            statement.setInt(2, courseId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void supprimerCourseDuPanier(int panierId, int courseId) {
        String query = "DELETE FROM panier_course WHERE panier_id = ? AND course_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, panierId);
            statement.setInt(2, courseId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void validerPanier(int panierId) {
        String query = "UPDATE panier_course SET is_paid = TRUE WHERE panier_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, panierId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la validation du panier", e);
        }
    }

    public List<Course> getCoursesDuPanier(int panierId) {
        List<Course> courses = new ArrayList<>();
        String query = "SELECT c.*, pc.is_paid FROM course c " +
                      "JOIN panier_course pc ON c.id = pc.course_id " +
                      "WHERE pc.panier_id = ? AND pc.is_paid = FALSE";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, panierId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Course course = new Course();
                course.setId(rs.getInt("id"));
                course.setTitle(rs.getString("title"));
                course.setDescription(rs.getString("description"));
                course.setDomain(rs.getString("domain"));
                course.setType(rs.getString("type"));
                course.setPrice(rs.getDouble("price"));
                course.setCreationDate(rs.getDate("creation_date"));
                courses.add(course);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des cours du panier", e);
        }
        return courses;
    }

    public boolean isCoursePaid(int userId, int courseId) {
        String query = "SELECT pc.is_paid FROM panier_course pc " +
                      "JOIN panier p ON pc.panier_id = p.id " +
                      "WHERE p.user_id = ? AND pc.course_id = ? AND pc.is_paid = TRUE";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.setInt(2, courseId);
            ResultSet rs = statement.executeQuery();
            return rs.next(); // Retourne true si le cours est trouvé comme payé
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la vérification du statut de paiement", e);
        }
    }



} 