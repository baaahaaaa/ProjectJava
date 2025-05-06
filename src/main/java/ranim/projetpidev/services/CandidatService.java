package ranim.projetpidev.services;

import ranim.projetpidev.entites.Candidat;
import ranim.projetpidev.tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CandidatService implements IServiices<Candidat> {

    @Override
    public void add(Candidat candidat) throws SQLException {
        String query = "INSERT INTO candidat (internship_id, student_id, date_candidature, email, phone_number, full_name, result, cv_filename, cover_letter_filename, cover_letter_text, applying_motivation, interview_date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setInt(1, candidat.getInternshipId());
            pstmt.setInt(2, candidat.getStudentId());
            pstmt.setDate(3, new Date(candidat.getDateCandidature().getTime()));
            pstmt.setString(4, candidat.getEmail());
            pstmt.setString(5, candidat.getPhoneNumber());
            pstmt.setString(6, candidat.getFullName());
            pstmt.setBoolean(7, candidat.isResult());
            pstmt.setString(8, candidat.getCvFilename());
            pstmt.setString(9, candidat.getCoverLetterFilename());
            pstmt.setString(10, candidat.getCoverLetterText());
            pstmt.setString(11, candidat.getApplyingMotivation());
            pstmt.setDate(12, candidat.getInterviewDate() != null ? new Date(candidat.getInterviewDate().getTime()) : null);
            pstmt.executeUpdate();
            System.out.println("Candidat ajouté avec succès !");
        }
    }

    // Suppression basée sur l'objet candidat (extraction de l'ID)
    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM candidat WHERE id = ?";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Candidat supprimé avec succès !");
            } else {
                System.out.println("Aucun candidat supprimé (ID introuvable).");
            }
        }
    }

    // Exemple d'update : ici nous mettons à jour le champ "result" à partir de l'objet candidat
    @Override
    public void update(Candidat candidat) throws SQLException {
        String query = "UPDATE candidat SET result = ? WHERE id = ?";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setBoolean(1, candidat.isResult());
            pstmt.setInt(2, candidat.getId());
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Candidat mis à jour avec succès !");
            } else {
                System.out.println("Aucun candidat mis à jour (ID introuvable).");
            }
        }
    }

    @Override
    public List<Candidat> getAll() throws SQLException {
        List<Candidat> candidats = new ArrayList<>();
        String query = "SELECT * FROM candidat";
        try (Statement stmt = MyDataBase.getInstance().getCnx().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Candidat candidat = new Candidat();
                candidat.setId(rs.getInt("id"));
                candidat.setInternshipId(rs.getInt("internship_id"));
                candidat.setStudentId(rs.getInt("student_id"));
                candidat.setDateCandidature(rs.getDate("date_candidature"));
                candidat.setEmail(rs.getString("email"));
                candidat.setPhoneNumber(rs.getString("phone_number"));
                candidat.setFullName(rs.getString("full_name"));
                candidat.setResult(rs.getBoolean("result"));
                candidat.setCvFilename(rs.getString("cv_filename"));
                candidat.setCoverLetterFilename(rs.getString("cover_letter_filename"));
                candidat.setCoverLetterText(rs.getString("cover_letter_text"));
                candidat.setApplyingMotivation(rs.getString("applying_motivation"));
                candidat.setInterviewDate(rs.getDate("interview_date"));
                candidats.add(candidat);
            }
        }
        return candidats;
    }

    @Override
    public Candidat getById(int id) {
        Candidat candidat = null;
        String query = "SELECT * FROM candidat WHERE id = ?";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                candidat = new Candidat();
                candidat.setId(rs.getInt("id"));
                candidat.setInternshipId(rs.getInt("internship_id"));
                candidat.setStudentId(rs.getInt("student_id"));
                candidat.setDateCandidature(rs.getDate("date_candidature"));
                candidat.setEmail(rs.getString("email"));
                candidat.setPhoneNumber(rs.getString("phone_number"));
                candidat.setFullName(rs.getString("full_name"));
                candidat.setResult(rs.getBoolean("result"));
                candidat.setCvFilename(rs.getString("cv_filename"));
                candidat.setCoverLetterFilename(rs.getString("cover_letter_filename"));
                candidat.setCoverLetterText(rs.getString("cover_letter_text"));
                candidat.setApplyingMotivation(rs.getString("applying_motivation"));
                candidat.setInterviewDate(rs.getDate("interview_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return candidat;
    }

    public boolean exists(int candidatId) {
        String query = "SELECT 1 FROM candidat WHERE id = ? LIMIT 1";  // Vérifie si le candidat existe dans la table 'candidat'
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setInt(1, candidatId);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();  // Si une ligne est retournée, cela signifie que le candidat existe
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;  // Si une erreur se produit ou si le candidat n'existe pas
    }

}
