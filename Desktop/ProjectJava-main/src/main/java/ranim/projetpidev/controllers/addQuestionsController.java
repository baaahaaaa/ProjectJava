package ranim.projetpidev.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ranim.projetpidev.entites.Question;
import ranim.projetpidev.services.QuestionService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class addQuestionsController {

    @FXML private VBox questionList;
    @FXML private TextField questionField;
    @FXML private TextField optionAField;
    @FXML private TextField optionBField;
    @FXML private TextField optionCField;
    @FXML private ComboBox<String> correctAnswerComboBox;

    private int quizId;
    private List<Question> questions = new ArrayList<>();

    @FXML
    public void handleAddQuestion() {
        String questionText = questionField.getText();
        String optionA = optionAField.getText();
        String optionB = optionBField.getText();
        String optionC = optionCField.getText();
        String correctAnswer = correctAnswerComboBox.getValue();

        if (questionText.isEmpty() || optionA.isEmpty() || optionB.isEmpty() || optionC.isEmpty() || correctAnswer == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        Question newQuestion = new Question(questionText);
        newQuestion.setOptionA(optionA);
        newQuestion.setOptionB(optionB);
        newQuestion.setOptionC(optionC);
        newQuestion.setCorrectAnswer(correctAnswer);
        newQuestion.setQuizId(quizId);

        questions.add(newQuestion);

        Label label = new Label("✔️ Question ajoutée temporairement : " + newQuestion.getQuestionText());
        questionList.getChildren().add(label);

        questionField.clear();
        optionAField.clear();
        optionBField.clear();
        optionCField.clear();
        correctAnswerComboBox.setValue(null);
    }

    @FXML
    public void handleSaveQuestions() {
        if (questions.isEmpty()) {
            showAlert("Erreur", "Aucune question à enregistrer.");
            return;
        }

        QuestionService questionService = new QuestionService();
        for (Question q : questions) {
            questionService.add(q);
        }

        showAlert("Succès", "Les questions ont été enregistrées !");
        questions.clear();
        questionList.getChildren().clear();
    }

    @FXML
    public void handleShowQuestions() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/listAddedQuestions.fxml"));
            Parent root = loader.load();

            ListQuestionsController controller = loader.getController();
            controller.setQuizId(quizId);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des questions du quiz");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d’ouvrir la liste des questions.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }
}
