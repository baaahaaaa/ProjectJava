package ranim.projetpidev.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ranim.projetpidev.entites.Event;
import ranim.projetpidev.entites.Reservation;
import ranim.projetpidev.tools.MyDataBase;

public class ReservationService implements IServices<Reservation> {

    private Connection cnx;
    private EventService eventService;

    public ReservationService() {
        cnx = MyDataBase.getInstance().getCnx();
        eventService = new EventService();
    }

    @Override
    public void add(Reservation reservation) {
        String sql = "INSERT INTO reservation (event_id, nb_places, total_price, phone_number, name, special_request) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, reservation.getEventId());
            ps.setInt(2, reservation.getNbPlaces());
            ps.setDouble(3, reservation.getTotalPrice());
            ps.setString(4, reservation.getPhoneNumber());
            ps.setString(5, reservation.getName());
            ps.setString(6, reservation.getSpecialRequest());
            ps.executeUpdate();
            System.out.println("✅ Réservation ajoutée !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout de la réservation : " + e.getMessage());
        }
    }

    public void delete(Reservation reservation) {
        String sql = "DELETE FROM reservation WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, reservation.getId());
            ps.executeUpdate();
            System.out.println("🗑️ Réservation supprimée !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression de la réservation : " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) throws SQLException {

    }

    @Override
    public void update(Reservation reservation) {
        String sql = "UPDATE reservation SET event_id=?, nb_places=?, total_price=?, phone_number=?, name=?, special_request=? WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, reservation.getEventId());
            ps.setInt(2, reservation.getNbPlaces());
            ps.setDouble(3, reservation.getTotalPrice());
            ps.setString(4, reservation.getPhoneNumber());
            ps.setString(5, reservation.getName());
            ps.setString(6, reservation.getSpecialRequest());
            ps.setInt(7, reservation.getId());
            ps.executeUpdate();
            System.out.println("✏️ Réservation mise à jour !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la mise à jour de la réservation : " + e.getMessage());
        }
    }

    public Reservation getById(int id) {
        String sql = "SELECT * FROM reservation WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Reservation(
                        rs.getInt("id"),
                        rs.getInt("event_id"),
                        rs.getInt("nb_places"),
                        rs.getDouble("total_price"),
                        rs.getString("phone_number"),
                        rs.getString("name"),
                        rs.getString("special_request")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur getById : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Reservation> getAll() {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation";
        try (Statement st = cnx.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Reservation reservation = new Reservation(
                        rs.getInt("id"),
                        rs.getInt("event_id"),
                        rs.getInt("nb_places"),
                        rs.getDouble("total_price"),
                        rs.getString("phone_number"),
                        rs.getString("name"),
                        rs.getString("special_request")
                );
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur getAll : " + e.getMessage());
        }
        return reservations;
    }

    /**
     * Get all reservations for a specific event
     * @param eventId The event ID
     * @return List of reservations for this event
     */
    public List<Reservation> getByEventId(int eventId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT * FROM reservation WHERE event_id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, eventId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Reservation reservation = new Reservation(
                        rs.getInt("id"),
                        rs.getInt("event_id"),
                        rs.getInt("nb_places"),
                        rs.getDouble("total_price"),
                        rs.getString("phone_number"),
                        rs.getString("name"),
                        rs.getString("special_request")
                );
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur getByEventId : " + e.getMessage());
        }
        return reservations;
    }

    /**
     * Calculate the total price for a reservation based on event price and number of places
     * @param eventId The event ID
     * @param nbPlaces Number of places to reserve
     * @return The calculated total price
     */
    public double calculateTotalPrice(int eventId, int nbPlaces) {
        Event event = eventService.getById(eventId);
        if (event != null) {
            return event.getPrice() * nbPlaces;
        }
        return 0;
    }
} 