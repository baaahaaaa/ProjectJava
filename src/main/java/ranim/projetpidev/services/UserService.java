package ranim.projetpidev.services;

import ranim.projetpidev.entites.*;
import ranim.projetpidev.tools.MyDataBase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IServices<User> {

    @Override
    public void add(User user) throws SQLException {
        String sql = "INSERT INTO user (first_name, last_name, email, entry_date, password, type, company_name, location, domain) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection cnx = MyDataBase.getInstance().getCnx();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setDate(4, Date.valueOf(user.getEntryDate()));
            ps.setString(5, user.getPassword());
            ps.setString(6, user.getType());
            ps.setString(7, user instanceof Agent agent ? agent.getCompanyName() : null);
            ps.setString(8, user instanceof Agent agent ? agent.getLocation() : null);
            ps.setString(9, user instanceof Tutor tutor ? tutor.getDomain() : null);

            ps.executeUpdate();
            System.out.println("✅ Utilisateur ajouté avec succès.");
        }
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE user SET first_name = ?, last_name = ?, email = ?, entry_date = ?, password = ?, type = ?, " +
                "company_name = ?, location = ?, domain = ? WHERE id = ?";

        // ✅ Toujours récupérer la connexion dans le bloc try pour garantir qu'elle est ouverte au bon moment
        try (Connection cnx = MyDataBase.getInstance().getCnx();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setDate(4, java.sql.Date.valueOf(user.getEntryDate()));
            ps.setString(5, user.getPassword());
            ps.setString(6, user.getType());

            ps.setString(7, user instanceof Agent agent ? agent.getCompanyName() : null);
            ps.setString(8, user instanceof Agent agent ? agent.getLocation() : null);
            ps.setString(9, user instanceof Tutor tutor ? tutor.getDomain() : null);

            ps.setInt(10, user.getId());

            ps.executeUpdate();
            System.out.println("✅ Utilisateur mis à jour avec succès.");

        } catch (SQLException e) {
            System.err.println("❌ Erreur SQL : " + e.getMessage());
            throw new RuntimeException("Erreur de mise à jour", e); // ou log + alerte
        }
    }


    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM user WHERE id = ?";

        try (Connection cnx = MyDataBase.getInstance().getCnx();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("✅ Utilisateur supprimé avec succès.");
        }
    }

    @Override
    public List<User> getAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";

        try (Connection cnx = MyDataBase.getInstance().getCnx();
             Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String type = rs.getString("type");
                if (type == null) continue;

                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                LocalDate entryDate = rs.getDate("entry_date").toLocalDate();
                String password = rs.getString("password");

                User user;
                switch (type.toLowerCase()) {
                    case "agent" -> {
                        String companyName = rs.getString("company_name");
                        String location = rs.getString("location");
                        user = new Agent(id, firstName, lastName, email, entryDate, password, type, companyName, location);
                    }
                    case "tutor" -> {
                        String domain = rs.getString("domain");
                        user = new Tutor(id, firstName, lastName, email, entryDate, password, type, domain);
                    }
                    case "student" -> {
                        user = new Student(id, firstName, lastName, email, entryDate, password, type);
                    }
                    case "admin" -> {
                        user = new Admin(id, firstName, lastName, email, entryDate, password, type);
                    }
                    default -> {
                        continue;
                    }
                }
                users.add(user);
            }
        }
        return users;
    }
}