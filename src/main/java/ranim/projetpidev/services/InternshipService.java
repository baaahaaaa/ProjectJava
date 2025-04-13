package ranim.projetpidev.services;

import ranim.projetpidev.entites.Internship;
import ranim.projetpidev.services.IServices;
import ranim.projetpidev.tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InternshipService implements IServices<Internship> {

    @Override
    public void add(Internship internship) throws SQLException {
        String query = "INSERT INTO internship (agent_id, title, description, company_name, location, duration, requirements, other_requirements, email, recompensation_type, recompensation_amount, currency, latitude, longitude) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setInt(1, internship.getAgentId());
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
            pstmt.executeUpdate();
            System.out.println("Offre de stage créée avec succès !");
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM internship WHERE id = ?";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setInt(1, id);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Offre de stage supprimée avec succès !");
            } else {
                System.out.println("Aucune offre supprimée (ID introuvable).");
            }
        }
    }

    // Méthode update : on met à jour le titre en utilisant la valeur du nouvel objet Internship.
    @Override
    public void update(Internship internship) throws SQLException {
        String query = "UPDATE internship SET title = ? WHERE id = ?";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setString(1, internship.getTitle());
            pstmt.setInt(2, internship.getId());
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Offre de stage mise à jour avec succès !");
            } else {
                System.out.println("Aucune offre mise à jour (ID introuvable).");
            }
        }
    }

    @Override
    public List<Internship> getAll() throws SQLException {
        List<Internship> internships = new ArrayList<>();
        String query = "SELECT * FROM internship";
        try (Statement stmt = MyDataBase.getInstance().getCnx().createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Internship internship = new Internship();
                internship.setId(rs.getInt("id"));
                internship.setAgentId(rs.getInt("agent_id"));
                internship.setTitle(rs.getString("title"));
                internship.setDescription(rs.getString("description"));
                internship.setCompanyName(rs.getString("company_name"));
                internship.setLocation(rs.getString("location"));
                internship.setDuration(rs.getString("duration"));
                internship.setRequirements(rs.getString("requirements"));
                internship.setOtherRequirements(rs.getString("other_requirements"));
                internship.setEmail(rs.getString("email"));
                internship.setRecompensationType(rs.getString("recompensation_type"));
                internship.setRecompensationAmount(rs.getDouble("recompensation_amount"));
                internship.setCurrency(rs.getString("currency"));
                internship.setLatitude(rs.getDouble("latitude"));
                internship.setLongitude(rs.getDouble("longitude"));
                internships.add(internship);
            }
        }
        return internships;
    }

    @Override
    public Internship getById(int id) {
        Internship internship = null;
        String query = "SELECT * FROM internship WHERE id = ?";
        try (PreparedStatement pstmt = MyDataBase.getInstance().getCnx().prepareStatement(query)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                internship = new Internship();
                internship.setId(rs.getInt("id"));
                internship.setAgentId(rs.getInt("agent_id"));
                internship.setTitle(rs.getString("title"));
                internship.setDescription(rs.getString("description"));
                internship.setCompanyName(rs.getString("company_name"));
                internship.setLocation(rs.getString("location"));
                internship.setDuration(rs.getString("duration"));
                internship.setRequirements(rs.getString("requirements"));
                internship.setOtherRequirements(rs.getString("other_requirements"));
                internship.setEmail(rs.getString("email"));
                internship.setRecompensationType(rs.getString("recompensation_type"));
                internship.setRecompensationAmount(rs.getDouble("recompensation_amount"));
                internship.setCurrency(rs.getString("currency"));
                internship.setLatitude(rs.getDouble("latitude"));
                internship.setLongitude(rs.getDouble("longitude"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return internship;
    }

    // Nouvelle méthode : recherche simple par terme dans plusieurs colonnes
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
                internship.setId(rs.getInt("id"));
                internship.setAgentId(rs.getInt("agent_id"));
                internship.setTitle(rs.getString("title"));
                internship.setDescription(rs.getString("description"));
                internship.setCompanyName(rs.getString("company_name"));
                internship.setLocation(rs.getString("location"));
                internship.setDuration(rs.getString("duration"));
                internship.setRequirements(rs.getString("requirements"));
                internship.setOtherRequirements(rs.getString("other_requirements"));
                internship.setEmail(rs.getString("email"));
                internship.setRecompensationType(rs.getString("recompensation_type"));
                internship.setRecompensationAmount(rs.getDouble("recompensation_amount"));
                internship.setCurrency(rs.getString("currency"));
                internship.setLatitude(rs.getDouble("latitude"));
                internship.setLongitude(rs.getDouble("longitude"));
                internships.add(internship);
            }
        }
        return internships;
    }

    // Nouvelle méthode : recherche avancée par critères
    public List<Internship> findByAdvancedCriteria(String requirements, String location, String recompensationType, Double minAmount) throws SQLException {
        List<Internship> internships = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM internship WHERE 1=1");
        List<Object> params = new ArrayList<>();

        // Filtrer par requirements
        if (requirements != null && !requirements.trim().isEmpty()) {
            if (requirements.equalsIgnoreCase("other")) {
                query.append(" AND requirements = ?");
                params.add("other");
            } else {
                query.append(" AND requirements LIKE ?");
                params.add("%" + requirements + "%");
            }
        }

        // Filtrer par location (recherche exacte)
        if (location != null && !location.trim().isEmpty()) {
            query.append(" AND location = ?");
            params.add(location);
        }

        // Filtrer par type de rémunération et montant minimal si applicable
        if (recompensationType != null && !recompensationType.trim().isEmpty()) {
            query.append(" AND recompensation_type = ?");
            params.add(recompensationType);
            if (recompensationType.equalsIgnoreCase("recompensated") && minAmount != null) {
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
                internship.setId(rs.getInt("id"));
                internship.setAgentId(rs.getInt("agent_id"));
                internship.setTitle(rs.getString("title"));
                internship.setDescription(rs.getString("description"));
                internship.setCompanyName(rs.getString("company_name"));
                internship.setLocation(rs.getString("location"));
                internship.setDuration(rs.getString("duration"));
                internship.setRequirements(rs.getString("requirements"));
                internship.setOtherRequirements(rs.getString("other_requirements"));
                internship.setEmail(rs.getString("email"));
                internship.setRecompensationType(rs.getString("recompensation_type"));
                internship.setRecompensationAmount(rs.getDouble("recompensation_amount"));
                internship.setCurrency(rs.getString("currency"));
                internship.setLatitude(rs.getDouble("latitude"));
                internship.setLongitude(rs.getDouble("longitude"));
                internships.add(internship);
            }
        }
        return internships;
    }
}
