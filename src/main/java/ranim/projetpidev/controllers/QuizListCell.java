package ranim.projetpidev.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ranim.projetpidev.entites.Quiz;

import java.io.IOException;

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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/QuizDetailsFront.fxml"));
            Parent root = loader.load();

            // Injecter le quiz dans le contrôleur
            QuizDetailsControllerFront controller = loader.getController();
            controller.setQuiz(quiz);

            Stage stage = new Stage();
            stage.setTitle("Détails du Quiz");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
