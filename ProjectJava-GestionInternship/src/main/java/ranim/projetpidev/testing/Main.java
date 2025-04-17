package ranim.projetpidev.testing;

import ranim.projetpidev.entites.Internship;
import ranim.projetpidev.services.InternshipService;
import ranim.projetpidev.tools.MyDataBase;

import java.sql.SQLException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws SQLException {
        // Connexion à la base de données
        MyDataBase md = MyDataBase.getInstance();

        // Création de l'instance de InternshipService pour interagir avec la base de données
        InternshipService internshipService = new InternshipService();

        // Test de l'ajout d'un internship
        Internship newInternship = new Internship(0, 1, "Java Developer", "Development of backend systems", "TechCorp",
                "New York", "6 months", "Java, Spring", "Bachelor degree", "contact@techcorp.com", "Paid", 5000, "USD", 40.7128, -74.0060);
        internshipService.add(newInternship);
        System.out.println("Internship ajouté avec succès!");

        // Test de la récupération de tous les internships
        List<Internship> internships = internshipService.getAll();
        System.out.println("Liste des internships : ");
        for (Internship internship : internships) {
            System.out.println(internship.getId() + " - " + internship.getTitle() + " - " + internship.getCompanyName());
        }

        // Test de la mise à jour d'un internship
        if (!internships.isEmpty()) {
            Internship internshipToUpdate = internships.get(0);  // Prenons le premier internship
            internshipToUpdate.setTitle("Updated Java Developer");
            internshipService.update(internshipToUpdate);
            System.out.println("Internship mis à jour avec succès!");
        }

        // Test de la suppression d'un internship
        if (!internships.isEmpty()) {
            Internship internshipToDelete = internships.get(0);  // Prenons le premier internship
            internshipService.delete(internshipToDelete.getId());
            System.out.println("Internship supprimé avec succès!");
        }

        // Test de recherche d'internships par un terme (par exemple "Java")
        List<Internship> searchResults = internshipService.searchByTerm("Java");
        System.out.println("Résultats de la recherche par terme 'Java' : ");
        for (Internship internship : searchResults) {
            System.out.println(internship.getId() + " - " + internship.getTitle());
        }

        // Test de la recherche avancée avec des critères (ex: location = New York)
        List<Internship> advancedSearchResults = internshipService.findByAdvancedCriteria("Java", "New York", "Paid", 1000.0);
        System.out.println("Résultats de la recherche avancée : ");
        for (Internship internship : advancedSearchResults) {
            System.out.println(internship.getId() + " - " + internship.getTitle());
        }
    }
}
