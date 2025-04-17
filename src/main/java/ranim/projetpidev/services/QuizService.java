package ranim.projetpidev.services;

import ranim.projetpidev.entites.Quiz;
import ranim.projetpidev.tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QuizService implements IServices<Quiz> {

    private static final List<String> TYPES_VALIDES = List.of(
            "Aéronautique", "Biotechnologie", "Chimie", "Civil Engineering", "Data Science",
            "Informatique", "Mathematics", "Mécanique", "Nanotechnologie", "Programmation",
            "Python", "Java", "Télécommunications", "Français"
    );


    private Connection cnx;

    public QuizService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    private boolean isValid(Quiz quiz) {
        if (quiz.getTitle() == null || quiz.getTitle().length() < 3 || quiz.getTitle().length() > 50) {
            System.err.println("❌ Le titre doit contenir au moins 8 caractères.");
            return false;
        }
        if (quiz.getDescription() == null || quiz.getDescription().length() > 255) {
            System.err.println("❌ La description ne doit pas dépasser 255 caractères.");
            return false;
        }
        if (quiz.getTypes() == null || !TYPES_VALIDES.contains(quiz.getTypes().trim())) {

            System.err.println("❌ Type invalide. Choisissez parmi : " + TYPES_VALIDES);
            return false;
        }
        return true;
    }

    @Override
    public void add(Quiz quiz) {
        if (!isValid(quiz)) {
            return; // Annuler si invalide
        }

        String sql = "INSERT INTO quiz (title, description, type) VALUES (?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, quiz.getTitle());
            ps.setString(2, quiz.getDescription());
            ps.setString(3, quiz.getTypes());
            ps.executeUpdate();
            System.out.println("✅ Quiz ajouté avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout du quiz : " + e.getMessage());
        }
    }

    @Override
    public void update(Quiz quiz) {
        if (!isValid(quiz)) {
            return; // Annuler si invalide
        }

        String sql = "UPDATE quiz SET title = ?, description = ?, type = ? WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, quiz.getTitle());
            ps.setString(2, quiz.getDescription());
            ps.setString(3, quiz.getTypes());
            ps.setInt(4, quiz.getId());
            ps.executeUpdate();
            System.out.println("✏️ Quiz mis à jour !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    public void delete(Quiz quiz) {
        String sql = "DELETE FROM quiz WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, quiz.getId());
            ps.executeUpdate();
            System.out.println("🗑️ Quiz supprimé avec succès !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression du quiz : " + e.getMessage());
        }
    }

    @Override
    public Quiz getById(int id) {
        String sql = "SELECT * FROM quiz WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Quiz(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("type")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération du quiz : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Quiz> getAll() {
        List<Quiz> quizzes = new ArrayList<>();
        String sql = "SELECT * FROM quiz";
        try (Statement st = cnx.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Quiz q = new Quiz(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("type")
                );
                quizzes.add(q);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des quiz : " + e.getMessage());
        }
        return quizzes;
    }

    public int getLastInsertedId() {
        String sql = "SELECT MAX(id) FROM quiz";
        try (Statement st = cnx.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur récupération dernier ID : " + e.getMessage());
        }
        return -1;
    }





}
