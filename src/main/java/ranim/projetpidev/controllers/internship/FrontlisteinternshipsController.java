package ranim.projetpidev.controllers.internship;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import ranim.projetpidev.controllers.candidat.AddCandidatureController;
import ranim.projetpidev.controllers.internship.FrontInternshipdetailsController;
import ranim.projetpidev.entites.Candidat;
import ranim.projetpidev.entites.Internship;
import ranim.projetpidev.services.CandidatService;
import ranim.projetpidev.services.InternshipService;
import ranim.projetpidev.services.PDFService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FrontlisteinternshipsController {

    @FXML private TextField searchField;
    @FXML private TilePane cardsContainer;
    @FXML private Button previousButton;
    @FXML private Button nextButton;

    private final InternshipService internshipService = new InternshipService();
    private final CandidatService candidatService     = new CandidatService();
    private final PDFService pdfService               = new PDFService();

    @FXML
    public void initialize() {
        loadInternshipCards();
    }

    private void loadInternshipCards() {
        cardsContainer.getChildren().clear();
        try {
            List<Internship> internships = internshipService.getAll();
            for (Internship internship : internships) {
                cardsContainer.getChildren().add(createInternshipCard(internship));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private Node createInternshipCard(Internship internship) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/ranim/projetpidev/internship/InternshipCard.fxml")
            );
            AnchorPane card = loader.load();
            InternshipCardController ctrl = loader.getController();
            ctrl.setInternship(internship);
            return card;
        } catch (IOException e) {
            e.printStackTrace();
            // Fallback minimaliste si échec du FXML
            AnchorPane fallback = new AnchorPane();
            fallback.getStyleClass().add("card");
            return fallback;
        }
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String query = searchField.getText().toLowerCase();
        cardsContainer.getChildren().clear();
        try {
            for (Internship internship : internshipService.searchByTerm(query)) {
                cardsContainer.getChildren().add(createInternshipCard(internship));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handlePagination(ActionEvent event) {
        loadInternshipCards();
    }

    private void navigateToInternshipDetails(Internship internship) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/ranim/projetpidev/internship/FrontInternshipdetails.fxml")
            );
            Parent view = loader.load();
            FrontInternshipdetailsController ctrl = loader.getController();
            ctrl.setInternship(internship);

            BorderPane dashboardRoot = (BorderPane) cardsContainer.getScene().getRoot();
            dashboardRoot.setCenter(view);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void applyToInternship(Internship internship) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/ranim/projetpidev/candidat/Frontaddcandidature.fxml")
            );
            Parent view = loader.load();
            AddCandidatureController ctrl = loader.getController();
            ctrl.setInternshipId(internship.getId());

            BorderPane dashboardRoot = (BorderPane) cardsContainer.getScene().getRoot();
            dashboardRoot.setCenter(view);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void navigateToResult(Internship internship) {
        try {
            Candidat candidat = candidatService.getById(internship.getId());
            String fxml = (candidat != null && candidat.isResult())
                    ? "/ranim/projetpidev/candidat/Frontcandidatureaccepté.fxml"
                    : "/ranim/projetpidev/candidat/Frontcandidaturerefusé.fxml";

            Parent view = FXMLLoader.load(getClass().getResource(fxml));
            BorderPane dashboardRoot = (BorderPane) cardsContainer.getScene().getRoot();
            dashboardRoot.setCenter(view);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
