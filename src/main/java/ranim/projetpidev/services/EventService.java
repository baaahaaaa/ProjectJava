package ranim.projetpidev.services;

import ranim.projetpidev.entites.Event;
import ranim.projetpidev.entites.Reservation;
import ranim.projetpidev.tools.MyDataBase;
import java.util.List;
import java.util.ArrayList;

import java.sql.*;

public  class EventService implements IServices<Event> {

    private Connection cnx;

    public EventService() {
        cnx = MyDataBase.getInstance().getCnx();
    }

    private boolean isValid(Event event) {
        // Validation de l'événement
        if (event.getTitle() == null || event.getTitle().trim().isEmpty()) {
            System.err.println("❌ Le titre de l'événement est obligatoire.");
            return false;
        }

        if (event.getDescription() == null || event.getDescription().trim().isEmpty()) {
            System.err.println("❌ La description de l'événement est obligatoire.");
            return false;
        }

        if (event.getLocation() == null || event.getLocation().trim().isEmpty()) {
            System.err.println("❌ La localisation de l'événement est obligatoire.");
            return false;
        }

        if (event.getDateEvent() == null) {
            System.err.println("❌ La date de l'événement est obligatoire.");
            return false;
        }

        if (event.getPrice() < 0) {
            System.err.println("❌ Le prix de l'événement doit être supérieur ou égal à 0.");
            return false;
        }

        return true;
    }

    @Override
    public void add(Event event) {
        if (!isValid(event)) {
            System.out.println(" Données invalides. Ajout annulé.");
            return;
        }

        String sql = "INSERT INTO event (title, description, type, location, date_event, price) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, event.getTitle());
            ps.setString(2, event.getDescription());
            ps.setString(3, event.getType());
            ps.setString(4, event.getLocation());
            ps.setDate(5, new Date(event.getDateEvent().getTime()));
            ps.setDouble(6, event.getPrice());
            ps.executeUpdate();
            System.out.println("✅ Événement ajouté !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout de l'événement : " + e.getMessage());
        }
    }

    public void delete(Event event) {
        // First delete all associated reservations for the event
        ReservationService reservationService = new ReservationService();
        List<Reservation> reservations = reservationService.getByEventId(event.getId()); // Get all reservations for the event

        // Delete each reservation
        for (Reservation reservation : reservations) {
            reservationService.delete(reservation);
        }

        // After deleting all reservations, delete the event
        String sql = "DELETE FROM event WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, event.getId());
            ps.executeUpdate();
            System.out.println("🗑️ Événement supprimé avec ses réservations !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression de l'événement : " + e.getMessage());
        }
    }

    @Override
    public void delete(int id) throws SQLException {
    }

    @Override
    public void update(Event event) {
        if (!isValid(event)) {
            System.out.println("⛔ Données invalides. Mise à jour annulée.");
            return;
        }

        String sql = "UPDATE event SET title=?, description=?, type=?, location=?, date_event=?, price=? WHERE id=?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setString(1, event.getTitle());
            ps.setString(2, event.getDescription());
            ps.setString(3, event.getType());
            ps.setString(4, event.getLocation());
            ps.setDate(5, new Date(event.getDateEvent().getTime()));
            ps.setDouble(6, event.getPrice());
            ps.setInt(7, event.getId());
            ps.executeUpdate();
            System.out.println("✏️ Événement mis à jour !");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la mise à jour de l'événement : " + e.getMessage());
        }
    }

    public Event getById(int id) {
        String sql = "SELECT * FROM event WHERE id = ?";
        try (PreparedStatement ps = cnx.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Event(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("type"),
                        rs.getString("location"),
                        rs.getDate("date_event"),
                        rs.getDouble("price")
                );
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur getById : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Event> getAll() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM event";
        try (Statement st = cnx.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Event event = new Event(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("description"),
                        rs.getString("type"),
                        rs.getString("location"),
                        rs.getDate("date_event"),
                        rs.getDouble("price")
                );
                events.add(event);
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur getAll : " + e.getMessage());
        }
        return events;
    }
}
