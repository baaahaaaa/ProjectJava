package ranim.projetpidev.services;

import ranim.projetpidev.entites.Promocode;
import ranim.projetpidev.entites.Student;
import ranim.projetpidev.tools.MyDataBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PromocodeServie implements IServices<Promocode> {
    @Override
    public void add(Promocode promocode) throws SQLException {
        String sql = "INSERT INTO promocode (code, discount_value, expiry_date,active, ID_student) VALUES (?, ?, ?,?, ?)";

        try (Connection cnx = MyDataBase.getInstance().getCnx();
             PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, promocode.getCode());
            ps.setDouble(2, promocode.getDiscountValue());
            ps.setDate(3, Date.valueOf(promocode.getExpiryDate()));
            ps.setBoolean(4, promocode.isActive());
            ps.setInt(5, promocode.getIdStudent());

            ps.executeUpdate();
            System.out.println("✅ Code promo ajouté avec succès.");
        }
    }

    @Override
    public void update(Promocode promocode) throws SQLException {
        String sql = "UPDATE promocode SET code = ?, discount_value = ?, expiry_date = ?,active = ?, ID_student = ? WHERE id = ?";

        try (Connection cnx = MyDataBase.getInstance().getCnx();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setString(1, promocode.getCode());
            ps.setDouble(2, promocode.getDiscountValue());
            ps.setDate(3, Date.valueOf(promocode.getExpiryDate()));
            ps.setBoolean(4, promocode.isActive());

            ps.setInt(5, promocode.getIdStudent());
            ps.setInt(6, promocode.getId());

            ps.executeUpdate();
            System.out.println("✅ Code promo mis à jour avec succès.");
        }
    }

    @Override
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM promocode WHERE id = ?";

        try (Connection cnx = MyDataBase.getInstance().getCnx();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("✅ Code promo supprimé avec succès.");
        }
    }

    @Override
    public List<Promocode> getAll() throws SQLException {
        List<Promocode> promocodes = new ArrayList<>();
        String sql = "SELECT * FROM promocode";

        try (Connection cnx = MyDataBase.getInstance().getCnx();
             Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Promocode promocode = new Promocode();
                promocode.setId(rs.getInt("id"));
                promocode.setCode(rs.getString("code"));
                promocode.setDiscountValue(rs.getDouble("discount_value"));
                promocode.setExpiryDate(rs.getDate("expiry_date").toLocalDate());
                promocode.setActive(rs.getBoolean("active"));
                promocode.setIdStudent(rs.getInt("ID_student"));
                promocodes.add(promocode);
            }
        }
        return promocodes;
    }


    public Promocode getById(int id) throws SQLException {
        String sql = "SELECT * FROM promocode WHERE id = ?";
        Promocode promocode = null;

        try (Connection cnx = MyDataBase.getInstance().getCnx();
             PreparedStatement ps = cnx.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    promocode = new Promocode();
                    promocode.setId(rs.getInt("id"));
                    promocode.setCode(rs.getString("code"));
                    promocode.setDiscountValue(rs.getDouble("discount_value"));
                    promocode.setExpiryDate(rs.getDate("expiry_date").toLocalDate());
                    promocode.setActive(rs.getBoolean("active"));
                    promocode.setIdStudent(rs.getInt("ID_student"));
                }
            }
        }
        return promocode;
    }
    public List<Promocode> getAllPromocodesForStudent(int studentId) throws SQLException {
        List<Promocode> promocodes = new ArrayList<>();

        // Requête SQL pour obtenir tous les codes promo d'un étudiant spécifique
        String query = "SELECT p.code, p.discount_value, p.expiry_date, p.active " +
                "FROM promocode p " +
                "WHERE p.ID_student = ?";  // Filtre pour récupérer les codes promo pour l'étudiant avec l'ID spécifié

        // Assurez-vous que la connexion est correctement initialisée
        Connection connection = MyDataBase.getInstance().getCnx();
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

}
