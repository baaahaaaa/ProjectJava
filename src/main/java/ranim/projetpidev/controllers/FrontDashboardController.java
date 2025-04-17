package ranim.projetpidev.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import java.io.IOException;

public class FrontDashboardController {

    @FXML
    private StackPane contentPane;

    public void goHome() {
        loadView("/ranim/projetpidev/HomeView.fxml");
    }

    public void goAbout() {
        loadView("/ranim/projetpidev/AboutView.fxml");
    }

    public void goCourses() {
        loadView("/ranim/projetpidev/CourseView.fxml");
    }

    public void goTrainers() {
        loadView("/ranim/projetpidev/TrainersView.fxml");
    }

    public void goEvents() {
        loadView("/ranim/projetpidev/listEvents.fxml");
    }

    public void goPricing() {
        loadView("/ranim/projetpidev/PricingView.fxml");
    }

    public void goContact() {
        loadView("/ranim/projetpidev/ContactView.fxml");
    }

    public void gotTologin() {
        loadView("/ranim/projetpidev/Login.fxml");  // Par exemple : Get Started = vers Quiz
    }
    public void getStarted() {
        loadView("/ranim/projetpidev/quiz/FrontListeQuiz.fxml");  // ✅ Correction ici
    }

    public void showQuiz() {
        loadView("/ranim/projetpidev/FrontListeQuiz.fxml"); // ✅ chemin exact
    }

    private void loadView(String path) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(path));
            contentPane.getChildren().setAll(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}