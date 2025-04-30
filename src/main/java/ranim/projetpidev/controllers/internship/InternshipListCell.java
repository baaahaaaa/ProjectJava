package ranim.projetpidev.controllers.internship;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ranim.projetpidev.entites.Internship;

import java.io.IOException;

public class InternshipListCell extends ListCell<Internship> {

    private VBox content;
    private Text title;
    private Text company;
    private Text location;
    private HBox buttonBox;
    @FXML
    private Button detailsButton;
    @FXML
    private Button applyButton;
    @FXML
    private Button resultButton;

    public InternshipListCell() {
        super();
        // Création des composants textuels
        title = new Text();
        company = new Text();
        location = new Text();
        // Création des boutons d'action
        detailsButton = new Button("Details");
        applyButton = new Button("Apply");
        resultButton = new Button("Result");

        // Navigation pour "Details"
        detailsButton.setOnAction(e -> {
            Internship item = getItem();
            if (item != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/internship/frontinternshipdetails.fxml"));
                    Parent root = loader.load();
                    FrontInternshipdetailsController controller = loader.getController();
                    controller.setInternship(item);
                    Stage stage = (Stage) detailsButton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Détails du Stage");
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        // Navigation pour "Apply"
        applyButton.setOnAction(e -> {
            Internship item = getItem();
            if (item != null) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/internship/frontaddcandidature.fxml"));
                    Parent root = loader.load();
                    Stage stage = (Stage) applyButton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Postuler au Stage");
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        // Navigation pour "Result" (condition simulée ici)
        resultButton.setOnAction(e -> {
            Internship item = getItem();
            if (item != null) {
                // Condition simulée pour déterminer le résultat ; à remplacer par votre logique réelle.
                boolean isAccepted = false;
                String fxmlFile = isAccepted ?
                        "/ranim/projetpidev/internship/frontcandidatureaccepté.fxml" :
                        "/ranim/projetpidev/internship/frontcandidaturerefusé.fxml";
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                    Parent root = loader.load();
                    Stage stage = (Stage) resultButton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.setTitle("Résultat de la Candidature");
                    stage.show();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        // Regroupement des boutons dans un HBox
        buttonBox = new HBox(10, detailsButton, applyButton, resultButton);
        // Assemblage de la card dans un VBox et application du style "card"
        content = new VBox(10, title, company, location, buttonBox);
        content.getStyleClass().add("card");
        content.setPadding(new Insets(10));
    }

    @Override
    protected void updateItem(Internship internship, boolean empty) {
        super.updateItem(internship, empty);
        if (empty || internship == null) {
            setGraphic(null);
        } else {
            title.setText(internship.getTitle());
            company.setText("Entreprise : " + internship.getCompanyName());
            location.setText("Localisation : " + internship.getLocation());
            setGraphic(content);
        }
    }
}
