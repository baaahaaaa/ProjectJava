package ranim.projetpidev.controllers.internship;

import java.io.IOException;
import java.sql.SQLException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.stage.Stage;
import ranim.projetpidev.entites.Internship;
import ranim.projetpidev.services.InternshipService;

public class BackinternshipdetailsController {
    @FXML private Label titleLabel;
    @FXML private Label companyLabel;
    @FXML private Label locationLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label requirementsLabel;
    @FXML private Label recompensationLabel;
    @FXML private Button backBtn;
    @FXML private Button modifyBtn;
    @FXML private Button applicationsListBtn;

    private InternshipService internshipService = new InternshipService();

    public void setInternshipId(int id) {
        try {
            Internship internship = internshipService.getById(id);
            titleLabel.setText(internship.getTitle());
            companyLabel.setText(internship.getCompanyName());
            locationLabel.setText(internship.getLocation());
            descriptionLabel.setText(internship.getDescription());
            requirementsLabel.setText(internship.getRequirements() != null ? internship.getRequirements() : "N/A");
            if("recompensated".equalsIgnoreCase(internship.getRecompensationType())) {
                recompensationLabel.setText(internship.getRecompensationAmount() + " " + internship.getCurrency());
            } else {
                recompensationLabel.setText("Free Internship");
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }
    }

    // Navigate back to the internship list
    @FXML
    public void backToList(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/internship/Backlisteinternship.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Internship List");
            stage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    // Navigate to the modify internship page
    @FXML
    public void modifyInternship(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/internship/Backupdateinternship.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) modifyBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Modify Internship");
            stage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    // Navigate to the applications list page
    @FXML
    public void applicationsList(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/candidat/Backlistecandidatures.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) applicationsListBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Applications List");
            stage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
