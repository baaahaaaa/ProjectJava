package ranim.projetpidev.services;

import org.w3c.dom.Document;
import ranim.projetpidev.entites.*;
import ranim.projetpidev.tools.MyDataBase;

import java.io.FileOutputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class UserService implements IServices<User> {


    @Override
    public void add(User user) throws SQLException {
        // Validation des champs
        String validationMessage = validateUser(user);
        if (validationMessage != null) {
            System.err.println(validationMessage);
            return;  // Si les validations échouent, on arrête l'ajout
        }

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
        // Validation des champs
        String validationMessage = validateUser(user);
        if (validationMessage != null) {
            System.err.println(validationMessage);
            return;  // Si les validations échouent, on arrête la mise à jour
        }

        String sql = "UPDATE user SET first_name = ?, last_name = ?, email = ?, entry_date = ?, password = ?, type = ?, " +
                "company_name = ?, location = ?, domain = ? WHERE id = ?";

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
            throw new RuntimeException("Erreur de mise à jour", e);
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
                LocalDate entryDate = null;
                Date sqlDate = rs.getDate("entry_date");
                if (sqlDate != null) {
                    entryDate = sqlDate.toLocalDate();
                }                String password = rs.getString("password");

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

    // Méthode de validation des données
    public String validateUser(User user) {
        // Vérifier que tous les champs sont remplis
        if (user.getFirstName().trim().isEmpty() || user.getLastName().trim().isEmpty() ||
                user.getEmail().trim().isEmpty() || user.getPassword().trim().isEmpty()) {
            return "❌ Tous les champs obligatoires doivent être remplis !";
        }

        // Validation pour firstName (Doit être une chaîne de caractères, lettres uniquement)
        if (!user.getFirstName().matches("^[a-zA-Z]+$")) {
            return "❌ Le prénom ne doit contenir que des lettres !";
        }

        // Validation pour lastName (Doit être une chaîne de caractères, lettres uniquement)
        if (!user.getLastName().matches("^[a-zA-Z]+$")) {
            return "❌ Le nom ne doit contenir que des lettres !";
        }

        // Validation pour email
        if (!user.getEmail().matches("^\\S+@\\S+\\.\\S+$")) {
            return "❌ Email invalide !";
        }

        // Validation pour le mot de passe
        if (user.getPassword().length() < 6) {
            return "❌ Le mot de passe doit contenir au moins 6 caractères !";
        }

        // Validation pour la date d'entrée
        if (user.getEntryDate() == null) {
            return "❌ Date d'entrée invalide !";
        }

        // Validation pour companyName (Nom de l'entreprise) - Lettres uniquement
        if (user instanceof Agent) {
            String companyName = ((Agent) user).getCompanyName();
            if (companyName == null || companyName.trim().isEmpty()) {
                return "❌ Le nom de l'entreprise ne doit pas être vide !";
            }
            if (!companyName.matches("^[a-zA-Z]+$")) {  // Seules des lettres sont autorisées
                return "❌ Le nom de l'entreprise doit contenir uniquement des lettres !";
            }
        }

        // Validation pour locationField (Lieu) - Lettres uniquement
        if (user instanceof Agent) {
            String location = ((Agent) user).getLocation();
            if (location == null || location.trim().isEmpty()) {
                return "❌ Le lieu ne doit pas être vide !";
            }
            if (!location.matches("^[a-zA-Z]+$")) {  // Seules des lettres sont autorisées
                return "❌ Le lieu doit contenir uniquement des lettres !";
            }
        }

        return null; // Pas d'erreur
    }

    public List<Promocode> getAllPromocodesForStudent(int studentId) throws SQLException {
        List<Promocode> promocodes = new ArrayList<>();

        // Requête SQL pour obtenir tous les codes promo d'un étudiant
        String query = "SELECT p.code, p.discount_value, p.expiry_date, p.active " +
                "FROM promocode p " +
                "WHERE p.ID_student = ?"; // Recherche des codes promo pour un étudiant spécifique

        // Assurez-vous que la connexion est correctement initialisée
        Connection connection = MyDataBase.getInstance().getCnx(); // Connexion à la base de données

        if (connection == null) {
            throw new SQLException("La connexion à la base de données est nulle.");
        }

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            // Paramétrer l'ID de l'étudiant dans la requête
            stmt.setInt(1, studentId);

            try (ResultSet rs = stmt.executeQuery()) {
                // Parcours des résultats pour ajouter les codes promo à la liste
                while (rs.next()) {
                    Promocode promocode = new Promocode();
                    promocode.setCode(rs.getString("code"));
                    promocode.setDiscountValue(rs.getDouble("discount_value"));

                    // Vérifier si la date d'expiration existe et la convertir en LocalDate
                    java.sql.Date sqlDate = rs.getDate("expiry_date");
                    if (sqlDate != null) {
                        promocode.setExpiryDate(sqlDate.toLocalDate());
                    } else {
                        promocode.setExpiryDate(null); // Si la date est nulle, on peut la laisser comme null
                    }

                    promocode.setActive(rs.getBoolean("active"));

                    // Ajouter le code promo à la liste
                    promocodes.add(promocode);
                }
            }
        }

        return promocodes; // Retourner la liste des codes promo pour cet étudiant
    }
    public User login(String email, String password) {
        String req = "SELECT * FROM user WHERE email = ? AND password = ?";
        Connection connection = MyDataBase.getInstance().getCnx(); // Connexion à la base de données

        try {
            PreparedStatement ps = connection.prepareStatement(req);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setFirstName(rs.getString("first_name"));
                u.setLastName(rs.getString("last_name"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setEntryDate(rs.getDate("entry_date").toLocalDate());
                u.setType(rs.getString("type"));
                return u;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Vous pouvez avoir d'autres métho



}
