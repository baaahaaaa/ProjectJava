package ranim.projetpidev.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ranim.projetpidev.entites.Question;
import ranim.projetpidev.services.QuestionService;
import java.sql.SQLException;

public class EditQuestionController {

    @FXML private TextField questionField;
    @FXML private TextField optionAField;
    @FXML private TextField optionBField;
    @FXML private TextField optionCField;
    @FXML private ComboBox<String> correctAnswerComboBox;

    private Question question;
    private final QuestionService questionService = new QuestionService();
    private Runnable onUpdateCallback;

    public void setQuestion(Question question) {
        this.question = question;
        questionField.setText(question.getQuestionText());
        optionAField.setText(question.getOptionA());
        optionBField.setText(question.getOptionB());
        optionCField.setText(question.getOptionC());
        correctAnswerComboBox.setItems(FXCollections.observableArrayList(
                question.getOptionA(), question.getOptionB(), question.getOptionC()
        ));
        correctAnswerComboBox.setValue(question.getCorrectAnswer());
    }

    public void setOnUpdateCallback(Runnable callback) {
        this.onUpdateCallback = callback;
    }

    @FXML
    public void handleUpdate() {
        // Vérification des champs
        if (questionField.getText().isEmpty() || optionAField.getText().isEmpty() ||
                optionBField.getText().isEmpty() || optionCField.getText().isEmpty() ||
                correctAnswerComboBox.getValue() == null) {
            showAlert("Erreur", "Tous les champs doivent être remplis.");
            return;
        }

        // Mise à jour de la question
        question.setQuestionText(questionField.getText());
        question.setOptionA(optionAField.getText());
        question.setOptionB(optionBField.getText());
        question.setOptionC(optionCField.getText());
        question.setCorrectAnswer(correctAnswerComboBox.getValue());

        try {
            // Tentative de mise à jour dans la base de données
            questionService.update(question);
            showAlert("Succès", "La question a été modifiée avec succès.");

            // Appel de la callback si définie
            if (onUpdateCallback != null) {
                onUpdateCallback.run();
            }

            // Fermeture de la fenêtre
            Stage stage = (Stage) questionField.getScene().getWindow();
            stage.close();
        } catch (SQLException e) {
            // Gérer l'exception SQLException ici
            showAlert("Erreur", "Une erreur est survenue lors de la mise à jour de la question : " + e.getMessage());
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
