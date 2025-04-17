package ranim.projetpidev.services;

import ranim.projetpidev.entites.Question;
import ranim.projetpidev.tools.MyDataBase;
import java.util.List;
import java.util.ArrayList;


import java.sql.*;

public class QuestionService implements IServices<Question> {

    private Connection cnx;

    public QuestionService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    private boolean isValid(Question q) {
        // Validation de la question et de ses options
        if (q.getQuestionText() == null || q.getQuestionText().trim().length() < 10) {
            System.err.println("❌ La question doit contenir au moins 10 caractères.");
            return false;
        }

        if (q.getOptionA() == null || q.getOptionB() == null || q.getOptionC() == null ||
                q.getOptionA().trim().isEmpty() || q.getOptionB().trim().isEmpty() || q.getOptionC().trim().isEmpty()) {
            System.err.println("❌ Les options A, B et C ne doivent pas être vides.");
            return false;
        }

        if (q.getOptionA().equalsIgnoreCase(q.getOptionB()) ||
                q.getOptionA().equalsIgnoreCase(q.getOptionC()) ||
                q.getOptionB().equalsIgnoreCase(q.getOptionC())) {
            System.err.println("❌ Les options doivent toutes être différentes.");
            return false;
        }

        if (!q.getCorrectAnswer().equalsIgnoreCase("A") &&
                !q.getCorrectAnswer().equalsIgnoreCase("B") &&
                !q.getCorrectAnswer().equalsIgnoreCase("C")) {
            System.err.println("❌ La bonne réponse doit être A, B ou C.");
            return false;
        }

        return true;
    }

    @Override
    public void add(Question question) {
        if (!isValid(question)) {
            System.out.println("⛔ Données invalides. Ajout annulé.");
            return;
        }

        String sql = "INSERT INTO question (quiz_id, question_text, option_a, option_b, option_c, correct_answer) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, question.getQuizId());
            ps.setString(2, question.getQuestionText());
            ps.setString(3, question.getOptionA());
            ps.setString(4, question.getOptionB());
            ps.setString(5, question.getOptionC());
            ps.setString(6, question.getCorrectAnswer().toUpperCase()); // standardisation
            ps.executeUpdate();
            System.out.println("✅ Question ajoutée !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout de la question : " + e.getMessage());
        }
    }

    @Override
    public void delete(Question question) {
        String sql = "DELETE FROM question WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, question.getId());
            ps.executeUpdate();
            System.out.println("🗑️ Question supprimée !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression : " + e.getMessage());
        }
    }

    @Override
    public void update(Question question) {
        if (!isValid(question)) {
            System.out.println("⛔ Données invalides. Mise à jour annulée.");
            return;
        }

        String sql = "UPDATE question SET question_text=?, option_a=?, option_b=?, option_c=?, correct_answer=? WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, question.getQuestionText());
            ps.setString(2, question.getOptionA());
            ps.setString(3, question.getOptionB());
            ps.setString(4, question.getOptionC());
            ps.setString(5, question.getCorrectAnswer().toUpperCase());
            ps.setInt(6, question.getId());
            ps.executeUpdate();
            System.out.println("✏️ Question mise à jour !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la mise à jour : " + e.getMessage());
        }
    }

    @Override
    public Question getById(int id) {
        String sql = "SELECT * FROM question WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Question(
                        rs.getInt("id"),
                        rs.getInt("quiz_id"),
                        rs.getString("question_text"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("correct_answer")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur getById : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Question> getAll() {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM question";
        try (Statement st = cnx.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Question q = new Question(
                        rs.getInt("id"),
                        rs.getInt("quiz_id"),
                        rs.getString("question_text"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("correct_answer")
                );
                questions.add(q);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur getAll : " + e.getMessage());
        }
        return questions;
    }

    public List<Question> getQuestionsByQuizId(int quizId) {
        List<Question> questions = new ArrayList<>();
        String sql = "SELECT * FROM question WHERE quiz_id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, quizId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Question q = new Question(
                        rs.getInt("id"),
                        rs.getInt("quiz_id"),
                        rs.getString("question_text"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("correct_answer")
                );
                questions.add(q);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur récupération des questions : " + e.getMessage());
        }
        return questions;
    }

}
