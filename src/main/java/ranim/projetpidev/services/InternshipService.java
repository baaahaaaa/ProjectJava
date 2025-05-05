package ranim.projetpidev.services;

import ranim.projetpidev.entites.Internship;
import ranim.projetpidev.tools.MyDataBase;

import java.io.IOException;
import java.nio.file.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InternshipService implements IServiices<Internship> {

    @Override
    public void add(Internship internship) throws SQLException {
        String query = "INSERT INTO internship (agent_id, title, description, company_name, location, duration, requirements, other_requirements, email, recompensation_type, recompensation_amount, currency, latitude, longitude, logo_path) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setInt(1,  internship.getAgentId());
            pstmt.setString(2, internship.getTitle());
            pstmt.setString(3, internship.getDescription());
            pstmt.setString(4, internship.getCompanyName());
            pstmt.setString(5, internship.getLocation());
            pstmt.setString(6, internship.getDuration());
            pstmt.setString(7, internship.getRequirements());
            pstmt.setString(8, internship.getOtherRequirements());
            pstmt.setString(9, internship.getEmail());
            pstmt.setString(10, internship.getRecompensationType());
            pstmt.setDouble(11, internship.getRecompensationAmount());
            pstmt.setString(12, internship.getCurrency());
            pstmt.setDouble(13, internship.getLatitude());
            pstmt.setDouble(14, internship.getLongitude());
            pstmt.setString(15, internship.getLogoPath());      // nouveau
            pstmt.executeUpdate();
            System.out.println("Offre de stage créée avec succès !");
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        // Supprimer d'abord les enregistrements associés
        deleteAssociatedFeedback(id);
        deleteAssociatedCandidates(id);

        String query = "DELETE FROM internship WHERE id = ?";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            System.out.println(rowsDeleted > 0
                    ? "Offre de stage supprimée avec succès !"
                    : "Aucune offre supprimée (ID introuvable).");
        }
    }

    private void deleteAssociatedFeedback(int internshipId) throws SQLException {
        String query = "DELETE FROM feedback WHERE internship_id = ?";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setInt(1, internshipId);
            pstmt.executeUpdate();
            System.out.println("Feedbacks associés supprimés avec succès !");
        }
    }

    private void deleteAssociatedCandidates(int internshipId) throws SQLException {
        String query = "DELETE FROM candidat WHERE internship_id = ?";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setInt(1, internshipId);
            pstmt.executeUpdate();
            System.out.println("Candidats associés supprimés avec succès !");
        }
    }

    @Override
    public void update(Internship internship) throws SQLException {
        String query = "UPDATE internship SET title = ?, company_name = ?, location = ?, description = ?, duration = ?, requirements = ?, other_requirements = ?, email = ?, recompensation_type = ?, recompensation_amount = ?, currency = ?, latitude = ?, longitude = ?, logo_path = ? WHERE id = ?";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setString(1,  internship.getTitle());
            pstmt.setString(2,  internship.getCompanyName());
            pstmt.setString(3,  internship.getLocation());
            pstmt.setString(4,  internship.getDescription());
            pstmt.setString(5,  internship.getDuration());
            pstmt.setString(6,  internship.getRequirements());
            pstmt.setString(7,  internship.getOtherRequirements());
            pstmt.setString(8,  internship.getEmail());
            pstmt.setString(9,  internship.getRecompensationType());
            pstmt.setDouble(10, internship.getRecompensationAmount());
            pstmt.setString(11, internship.getCurrency());
            pstmt.setDouble(12, internship.getLatitude());
            pstmt.setDouble(13, internship.getLongitude());
            pstmt.setString(14, internship.getLogoPath());     // nouveau
            pstmt.setInt(15, internship.getId());
            int rowsUpdated = pstmt.executeUpdate();
            System.out.println(rowsUpdated > 0
                    ? "Offre de stage mise à jour avec succès !"
                    : "Aucune offre mise à jour (ID introuvable).");
        }
    }

    public boolean existsinternship(int internshipId) throws SQLException {
        String query = "SELECT COUNT(*) FROM internship WHERE id = ?";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setInt(1, internshipId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    @Override
    public List<Internship> getAll() throws SQLException {
        List<Internship> internships = new ArrayList<>();
        String query = "SELECT * FROM internship";
        try (Statement stmt = MyDataBase.getInstance().getCnx().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Internship internship = new Internship();
                internship.idProperty().set(rs.getInt("id"));
                internship.agentIdProperty().set(rs.getInt("agent_id"));
                internship.titleProperty().set(rs.getString("title"));
                internship.descriptionProperty().set(rs.getString("description"));
                internship.companyNameProperty().set(rs.getString("company_name"));
                internship.locationProperty().set(rs.getString("location"));
                internship.durationProperty().set(rs.getString("duration"));
                internship.requirementsProperty().set(rs.getString("requirements"));
                internship.otherRequirementsProperty().set(rs.getString("other_requirements"));
                internship.emailProperty().set(rs.getString("email"));
                internship.recompensationTypeProperty().set(rs.getString("recompensation_type"));
                internship.recompensationAmountProperty().set(rs.getDouble("recompensation_amount"));
                internship.currencyProperty().set(rs.getString("currency"));
                internship.latitudeProperty().set(rs.getDouble("latitude"));
                internship.longitudeProperty().set(rs.getDouble("longitude"));
                internship.logoPathProperty().set(rs.getString("logo_path"));  // nouveau
                internships.add(internship);
            }
        }
        return internships;
    }

    @Override
    public Internship getById(int id) throws SQLException {
        Internship internship = null;
        String query = "SELECT * FROM internship WHERE id = ?";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                internship = new Internship();
                internship.idProperty().set(rs.getInt("id"));
                internship.agentIdProperty().set(rs.getInt("agent_id"));
                internship.titleProperty().set(rs.getString("title"));
                internship.descriptionProperty().set(rs.getString("description"));
                internship.companyNameProperty().set(rs.getString("company_name"));
                internship.locationProperty().set(rs.getString("location"));
                internship.durationProperty().set(rs.getString("duration"));
                internship.requirementsProperty().set(rs.getString("requirements"));
                internship.otherRequirementsProperty().set(rs.getString("other_requirements"));
                internship.emailProperty().set(rs.getString("email"));
                internship.recompensationTypeProperty().set(rs.getString("recompensation_type"));
                internship.recompensationAmountProperty().set(rs.getDouble("recompensation_amount"));
                internship.currencyProperty().set(rs.getString("currency"));
                internship.latitudeProperty().set(rs.getDouble("latitude"));
                internship.longitudeProperty().set(rs.getDouble("longitude"));
                internship.logoPathProperty().set(rs.getString("logo_path"));  // nouveau
            }
        }
        return internship;
    }

    public boolean exists(int internshipId) throws SQLException {
        String query = "SELECT COUNT(*) FROM internship WHERE id = ?";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setInt(1, internshipId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public List<Internship> searchByTerm(String term) throws SQLException {
        List<Internship> internships = new ArrayList<>();
        String query = "SELECT * FROM internship WHERE title LIKE ? OR description LIKE ? OR requirements LIKE ? OR location LIKE ?";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            String searchTerm = "%" + term + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);
            pstmt.setString(3, searchTerm);
            pstmt.setString(4, searchTerm);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Internship internship = new Internship();
                internship.idProperty().set(rs.getInt("id"));
                internship.agentIdProperty().set(rs.getInt("agent_id"));
                internship.titleProperty().set(rs.getString("title"));
                internship.descriptionProperty().set(rs.getString("description"));
                internship.companyNameProperty().set(rs.getString("company_name"));
                internship.locationProperty().set(rs.getString("location"));
                internship.durationProperty().set(rs.getString("duration"));
                internship.requirementsProperty().set(rs.getString("requirements"));
                internship.otherRequirementsProperty().set(rs.getString("other_requirements"));
                internship.emailProperty().set(rs.getString("email"));
                internship.recompensationTypeProperty().set(rs.getString("recompensation_type"));
                internship.recompensationAmountProperty().set(rs.getDouble("recompensation_amount"));
                internship.currencyProperty().set(rs.getString("currency"));
                internship.latitudeProperty().set(rs.getDouble("latitude"));
                internship.longitudeProperty().set(rs.getDouble("longitude"));
                internship.logoPathProperty().set(rs.getString("logo_path"));  // nouveau
                internships.add(internship);
            }
        }
        return internships;
    }

    public List<Internship> findByAdvancedCriteria(String requirements, String location, String recompensationType, Double minAmount) throws SQLException {
        List<Internship> internships = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM internship WHERE 1=1");
        List<Object> params = new ArrayList<>();

        if (requirements != null && !requirements.trim().isEmpty()) {
            if ("other".equalsIgnoreCase(requirements)) {
                query.append(" AND requirements = ?");
                params.add("other");
            } else {
                query.append(" AND requirements LIKE ?");
                params.add("%" + requirements + "%");
            }
        }
        if (location != null && !location.trim().isEmpty()) {
            query.append(" AND location = ?");
            params.add(location);
        }
        if (recompensationType != null && !recompensationType.trim().isEmpty()) {
            query.append(" AND recompensation_type = ?");
            params.add(recompensationType);
            if ("recompensated".equalsIgnoreCase(recompensationType) && minAmount != null) {
                query.append(" AND recompensation_amount >= ?");
                params.add(minAmount);
            }
        }

        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query.toString())) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Internship internship = new Internship();
                internship.idProperty().set(rs.getInt("id"));
                internship.agentIdProperty().set(rs.getInt("agent_id"));
                internship.titleProperty().set(rs.getString("title"));
                internship.descriptionProperty().set(rs.getString("description"));
                internship.companyNameProperty().set(rs.getString("company_name"));
                internship.locationProperty().set(rs.getString("location"));
                internship.durationProperty().set(rs.getString("duration"));
                internship.requirementsProperty().set(rs.getString("requirements"));
                internship.otherRequirementsProperty().set(rs.getString("other_requirements"));
                internship.emailProperty().set(rs.getString("email"));
                internship.recompensationTypeProperty().set(rs.getString("recompensation_type"));
                internship.recompensationAmountProperty().set(rs.getDouble("recompensation_amount"));
                internship.currencyProperty().set(rs.getString("currency"));
                internship.latitudeProperty().set(rs.getDouble("latitude"));
                internship.longitudeProperty().set(rs.getDouble("longitude"));
                internship.logoPathProperty().set(rs.getString("logo_path"));  // nouveau
                internships.add(internship);
            }
        }
        return internships;
    }
}
