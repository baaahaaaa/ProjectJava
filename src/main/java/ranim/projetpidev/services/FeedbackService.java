package ranim.projetpidev.services;

import ranim.projetpidev.entites.Feedback;
import ranim.projetpidev.tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FeedbackService implements IServiices<Feedback> {

    @Override
    public void add(Feedback feedback) throws SQLException {
        String query = "INSERT INTO feedback (candidat_id, internship_id, rating, comment, date_feedback) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setInt(1, feedback.getCandidatId());
            pstmt.setInt(2, feedback.getInternshipId());
            pstmt.setInt(3, feedback.getRating());
            pstmt.setString(4, feedback.getComment());
            pstmt.setDate(5, feedback.getDateFeedback() != null ? new Date(feedback.getDateFeedback().getTime()) : null);
            pstmt.executeUpdate();
            System.out.println("Feedback ajouté avec succès !");
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM feedback WHERE id = ?";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Feedback supprimé avec succès !");
            } else {
                System.out.println("Aucun feedback supprimé (ID introuvable).");
            }
        }
    }

    // La méthode update reçoit l'objet Feedback et met à jour le champ "comment"
    @Override
    public void update(Feedback feedback) throws SQLException {
        String query = "UPDATE feedback SET comment = ? WHERE id = ?";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setString(1, feedback.getComment());
            pstmt.setInt(2, feedback.getId());
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Feedback mis à jour avec succès !");
            } else {
                System.out.println("Aucun feedback mis à jour (ID introuvable).");
            }
        }
    }

    @Override
    public List<Feedback> getAll() throws SQLException {
        List<Feedback> feedbackList = new ArrayList<>();
        String query = "SELECT * FROM feedback";
        try (Statement stmt = MyDataBase.getInstance().getCnx().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Feedback feedback = new Feedback();
                feedback.setId(rs.getInt("id"));
                feedback.setCandidatId(rs.getInt("candidat_id"));
                feedback.setInternshipId(rs.getInt("internship_id"));
                feedback.setRating(rs.getInt("rating"));
                feedback.setComment(rs.getString("comment"));
                feedback.setDateFeedback(rs.getDate("date_feedback"));
                feedbackList.add(feedback);
            }
        }
        return feedbackList;
    }

    @Override
    public Feedback getById(int id) {
        Feedback feedback = null;
        String query = "SELECT * FROM feedback WHERE id = ?";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    feedback = new Feedback();
                    feedback.setId(rs.getInt("id"));
                    feedback.setCandidatId(rs.getInt("candidat_id"));
                    feedback.setInternshipId(rs.getInt("internship_id"));
                    feedback.setRating(rs.getInt("rating"));
                    feedback.setComment(rs.getString("comment"));
                    feedback.setDateFeedback(rs.getDate("date_feedback"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return feedback;
    }
}
