package ranim.projetpidev.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import ranim.projetpidev.entites.Question;
import ranim.projetpidev.entites.Quiz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuizExecutionController {

    @FXML
    private Label quizTitleLabel;

    @FXML
    private VBox questionContainer;

    @FXML
    private Button submitButton;

    private Quiz quiz;

    // Map<Question, ToggleGroup> pour suivre les réponses de l'utilisateur
    private final Map<Question, ToggleGroup> userAnswers = new HashMap<>();

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
        quizTitleLabel.setText(quiz.getTitle());
        afficherQuestions();
    }

    private void afficherQuestions() {
        questionContainer.getChildren().clear();

        for (Question question : quiz.getQuestions()) {
            VBox questionBox = new VBox(5);
            Label questionLabel = new Label(question.getText());
            questionLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

            ToggleGroup group = new ToggleGroup();
            userAnswers.put(question, group);

            for (String choice : question.getChoices()) {
                RadioButton rb = new RadioButton(choice);
                rb.setToggleGroup(group);
                questionBox.getChildren().add(rb);
            }

            questionBox.setStyle("-fx-padding: 10; -fx-background-color: #fff; -fx-border-color: #ddd; -fx-border-radius: 5;");
            questionContainer.getChildren().addAll(questionLabel, questionBox);
        }
    }

    @FXML
    private void handleSubmitQuiz() {
        int score = 0;
        int total = quiz.getQuestions().size();

        for (Question question : quiz.getQuestions()) {
            ToggleGroup group = userAnswers.get(question);
            if (group.getSelectedToggle() != null) {
                RadioButton selected = (RadioButton) group.getSelectedToggle();
                String userChoice = selected.getText();
                if (userChoice.equals(question.getCorrectAnswer())) {
                    score++;
                }
            }
        }

        Alert resultAlert = new Alert(Alert.AlertType.INFORMATION);
        resultAlert.setTitle("Résultat du Quiz");
        resultAlert.setHeaderText("Vous avez obtenu : " + score + "/" + total);
        resultAlert.setContentText(score >= total / 2 ? "Bravo !" : "Essayez encore !");
        resultAlert.showAndWait();
    }
}
