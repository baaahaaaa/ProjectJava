package ranim.projetpidev.services;

import ranim.projetpidev.entites.*;
import ranim.projetpidev.tools.MyDataBase;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IServices<User> {
    public UserService() {
        Connection cnx = MyDataBase.getInstance().getCnx();
    }
    @Override
    public void add(User user) {
        String sql = "INSERT INTO user (first_name, last_name, email, entry_date, password, type, company_name, location, domain) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // ✅ Connexion initialisée via MyDataBase
        Connection cnx = MyDataBase.getInstance().getCnx();

        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setDate(4, Date.valueOf(user.getEntryDate()));
            ps.setString(5, user.getPassword());
            ps.setString(6, user.getType());

            // Champs conditionnels (null si non concerné)
            ps.setString(7, user instanceof Agent agent ? agent.getCompanyName() : null);
            ps.setString(8, user instanceof Agent agent ? agent.getLocation() : null);
            ps.setString(9, user instanceof Tutor tutor ? tutor.getDomain() : null);

            ps.executeUpdate();
            System.out.println("✅ Utilisateur ajouté avec succès.");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }




    @Override
    public void update(int id, String first_name) throws SQLException {
        String sql = "UPDATE user SET first_name = '" + first_name + "' WHERE id = " + id;
        Connection cnx = MyDataBase.getInstance().getCnx();
        Statement stmt = cnx.createStatement();
        stmt.executeUpdate(sql);
        System.out.println("User updated successfully");
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM user WHERE id = " + id;
        Connection cnx = MyDataBase.getInstance().getCnx();
        Statement stmt = cnx.createStatement();
        stmt.executeUpdate(sql);
        System.out.println("User deleted successfully");
    }

    public List<User> getAll() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";
        Connection cnx=MyDataBase.getInstance().getCnx();
        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String type = rs.getString("type").toLowerCase();
                int id = rs.getInt("id");
                String firstName = rs.getString("first_name");
                String lastName = rs.getString("last_name");
                String email = rs.getString("email");
                LocalDate entryDate = rs.getDate("entry_date").toLocalDate();
                String password = rs.getString("password");

                User user;

                switch (type) {
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
                        System.out.println("Type inconnu : " + type);
                        continue;
                    }
                }

                users.add(user);
            }
        }

        return users;
    }
}
