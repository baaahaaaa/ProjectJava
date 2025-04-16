package ranim.projetpidev.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ranim.projetpidev.entites.Quiz;

import java.io.IOException;

public class QuizDetailsController {

    @FXML private Label quizTitleLabel;
    @FXML private Label quizTypeLabel;
    @FXML private Label descriptionLabel;
    @FXML private Button btnVoirQuestions;
    @FXML private Button btnAjouterQuestion;


    private Quiz quiz;

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        quizTitleLabel.setText("Titre : " + quiz.getTitle());
        quizTypeLabel.setText("Type : " + quiz.getTypes());
        descriptionLabel.setText("Description : " + quiz.getDescription());
    }

    @FXML
    private void handleAddQuestion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/addQuestions.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            addQuestionsController controller = loader.getController();
            controller.setQuizId(quiz.getId());

            stage.setTitle("Ajouter une question");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    private void handleShowQuestions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/listAddedQuestions.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            ListQuestionsController controller = loader.getController();
            controller.setQuizId(quiz.getId());

            stage.setTitle("Questions du quiz");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }




    }



    @FXML
    private void handleQuitter() {
        // Ferme la fenêtre actuelle
        Stage stage = (Stage) btnVoirQuestions.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleRetour() {
        // Tu peux ici ouvrir une autre interface ou revenir à une précédente.
        // Exemple : ouvrir la liste des quiz
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/listQuiz.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Liste des Quiz");
            stage.show();

            // Fermer la fenêtre actuelle
            Stage currentStage = (Stage) btnVoirQuestions.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
