package ranim.projetpidev.controllers.candidat;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class FrontCandidatureController {

    @FXML private Button backBtn;

    // Navigate back to the candidate list
    @FXML
    public void backToList() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/internship/FrontInternshipdetails.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) backBtn.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Candidates List");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
