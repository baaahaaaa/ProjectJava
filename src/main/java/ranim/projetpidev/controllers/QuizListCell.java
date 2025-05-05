package ranim.projetpidev.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import ranim.projetpidev.entites.Quiz;

public class QuizListCell {

    @FXML private Label titleLabel;
    @FXML private Label typeLabel;
    @FXML private Label descriptionLabel;

    private Quiz quiz;

    public void setData(Quiz quiz) {
        this.quiz = quiz;
        titleLabel.setText(quiz.getTitle());
        typeLabel.setText("Type : " + quiz.getTypes());
        descriptionLabel.setText(quiz.getDescription());
    }

    @FXML
    private void handleDetails() {
        System.out.println("Détails du quiz : " + quiz.getTitle());
        // Rediriger vers page détails ou ouvrir modale
    }

    @FXML
    private void handleStart() {
        System.out.println("Commencer quiz : " + quiz.getTitle());
        // Rediriger vers interface QuizExecutionController
    }

    @FXML
    private void handleResult() {
        System.out.println("Résultats du quiz : " + quiz.getTitle());
        // Rediriger vers la page de résultats
    }
}
