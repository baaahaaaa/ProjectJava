package ranim.projetpidev.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class FrontDashboardController {

    @FXML private BorderPane rootPane;    // Votre BorderPane principal
    @FXML private AnchorPane contentPane;  // Placeholder center

    @FXML
    public void initialize() {
        goHome();  // vue par défaut
    }

    public void goHome()    { loadView("/ranim/projetpidev/HomeView.fxml"); }
    public void goAbout()   { loadView("/ranim/projetpidev/AboutView.fxml"); }
    public void goCourses() { loadView("/ranim/projetpidev/CourseView.fxml"); }
    public void goTrainers(){ loadView("/ranim/projetpidev/TrainersView.fxml"); }
    public void goEvents()  { loadView("/ranim/projetpidev/listEvents.fxml"); }
    public void goPricing() { loadView("/ranim/projetpidev/internship/Frontlisteinternships.fxml"); }
    public void goContact() { loadView("/ranim/projetpidev/ContactView.fxml"); }
    public void gotTologin(){ loadView("/ranim/projetpidev/Login.fxml"); }
    public void showQuiz()  { loadView("/ranim/projetpidev/quiz/FrontListeQuiz.fxml"); }

    /**
     * Charge la vue FXML spécifiée et l’injecte DANS LE CENTER du rootPane
     * sans jamais toucher à top/left du BorderPane.
     */
    private void loadView(String fxmlPath) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(fxmlPath));
            rootPane.setCenter(view);
            // Si vous voulez que la vue remplisse tout l'espace :
            AnchorPane.setTopAnchor(view,    0.0);
            AnchorPane.setBottomAnchor(view, 0.0);
            AnchorPane.setLeftAnchor(view,   0.0);
            AnchorPane.setRightAnchor(view,  0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
