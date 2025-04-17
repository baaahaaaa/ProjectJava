package ranim.projetpidev.controllers.internship;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ranim.projetpidev.controllers.candidat.AddCandidatureController;
import ranim.projetpidev.entites.Candidat;
import ranim.projetpidev.entites.Internship;
import ranim.projetpidev.services.CandidatService;
import ranim.projetpidev.services.InternshipService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class FrontlisteinternshipsController {

    @FXML private TextField searchField;
    @FXML private FlowPane cardsContainer;
    @FXML private Button previousButton;
    @FXML private Button nextButton;

    private InternshipService internshipService = new InternshipService();
    private CandidatService candidatService = new CandidatService();

    /**
     * Initialisation de la vue, chargement des cartes.
     */
    @FXML
    public void initialize() {
        loadInternshipCards();
    }

    /**
     * Charge tous les stages et crée une carte pour chacun.
     */
    private void loadInternshipCards() {
        cardsContainer.getChildren().clear();
        try {
            List<Internship> internships = internshipService.getAll();
            for (Internship internship : internships) {
                VBox card = createInternshipCard(internship);
                cardsContainer.getChildren().add(card);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Crée dynamiquement une carte stylisée pour un stage donné.
     * Cette méthode utilise un VBox auquel est appliquée la classe CSS "card" définie dans frontend.css.
     */
    private VBox createInternshipCard(Internship internship) {
        VBox card = new VBox(10);
        card.getStyleClass().add("card");
        card.setPadding(new Insets(10));

        // Titre du stage
        Label titleLabel = new Label(internship.getTitle());
        titleLabel.getStyleClass().add("card-title");

        // Informations de l'entreprise et de la localisation
        Label companyLabel = new Label("Entreprise : " + internship.getCompanyName());
        Label locationLabel = new Label("Localisation : " + internship.getLocation());

        // Boutons d'action regroupés dans un HBox
        HBox buttonBox = new HBox(10);
        Button detailsBtn = new Button("Details");
        detailsBtn.getStyleClass().add("action-button");
        detailsBtn.setOnAction(e -> navigateToInternshipDetails(internship));

        Button applyBtn = new Button("Apply");
        applyBtn.getStyleClass().add("action-button");
        applyBtn.setOnAction(e -> applyToInternship(internship));

        Button resultBtn = new Button("Result");
        resultBtn.getStyleClass().add("action-button");
        resultBtn.setOnAction(e -> navigateToResult(internship));

        buttonBox.getChildren().addAll(detailsBtn, applyBtn, resultBtn);

        card.getChildren().addAll(titleLabel, companyLabel, locationLabel, buttonBox);
        return card;
    }

    /**
     * Recherche dynamique : filtre les cartes en fonction du terme saisi.
     */
    @FXML
    private void handleSearch(ActionEvent event) {
        String searchQuery = searchField.getText().toLowerCase();
        cardsContainer.getChildren().clear();
        try {
            List<Internship> internships = internshipService.searchByTerm(searchQuery);
            for (Internship internship : internships) {
                VBox card = createInternshipCard(internship);
                cardsContainer.getChildren().add(card);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Gestion de la pagination (ici, recharge simplement l’ensemble des cartes).
     */
    @FXML
    private void handlePagination(ActionEvent event) {
        loadInternshipCards();
    }

    /**
     * Redirige vers la vue des détails d'un stage.
     * Charge frontinternshipdetails.fxml, transmet l'objet Internship au contrôleur correspondant et affiche la nouvelle scène.
     */
    @FXML
    private void navigateToInternshipDetails(Internship internship) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/internship/FrontInternshipdetails.fxml"));
            Parent root = loader.load();
            FrontInternshipdetailsController controller = loader.getController();
            controller.setInternship(internship);
            Stage stage = (Stage) cardsContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails du Stage");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Redirige vers le formulaire de candidature.
     * Charge frontaddcandidature.fxml et affiche la nouvelle scène.
     */
    @FXML
    private void applyToInternship(Internship internship) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/candidat/Frontaddcandidature.fxml"));
            Parent root = loader.load();
            AddCandidatureController controller = loader.getController();
            controller.setInternshipId(internship.getId());
            Stage stage = (Stage) cardsContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Postuler au Stage");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void onInternshipSelected(Internship internship) {
        applyToInternship(internship); // Call applyToInternship when an internship is selected
    }
    /**
     * Redirige vers la vue du résultat de la candidature, en fonction du statut du candidat.
     * Charge frontcandidatureaccepté.fxml si la candidature est acceptée, sinon frontcandidaturerefusé.fxml.
     */
    private void navigateToResult(Internship internship) {
        try {
            Candidat candidat = candidatService.getById(internship.getId());
            boolean isAccepted = (candidat != null) && candidat.isResult();

            String fxmlFile = isAccepted ?
                    "/ranim/projetpidev/candidat/Frontcandidatureaccepté.fxml" :
                    "/ranim/projetpidev/candidat/Frontcandidaturerefusé.fxml";

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Stage stage = (Stage) cardsContainer.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Résultat de la Candidature");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
