package ranim.projetpidev.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import ranim.projetpidev.entites.Question;
import ranim.projetpidev.services.QuestionService;
import java.sql.SQLException;

public class QuestionDetailsController {

    @FXML
    private Label questionTextLabel;

    @FXML
    private Label optionALabel;

    @FXML
    private Label optionBLabel;

    @FXML
    private Label optionCLabel;

    @FXML
    private Label correctAnswerLabel;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    private Question question;

    public void setQuestion(Question question) {
        this.question = question;
        displayQuestionDetails();
    }

    private void displayQuestionDetails() {
        // Remplir les labels avec les détails de la question
        questionTextLabel.setText(question.getQuestionText());
        optionALabel.setText("A: " + question.getOptionA());
        optionBLabel.setText("B: " + question.getOptionB());
        optionCLabel.setText("C: " + question.getOptionC());
        correctAnswerLabel.setText("Réponse correcte: " + question.getCorrectAnswer());
    }

    @FXML
    private void handleModifier() {
        // Rediriger vers un formulaire de modification ou effectuer une action de modification ici
        System.out.println("Modifier la question");
    }

    @FXML
    private void handleSupprimer() {
        // Logique pour supprimer la question
        QuestionService service = new QuestionService();
        try {
            service.delete(question.getId());
            System.out.println("Question supprimée avec succès.");
        } catch (SQLException e) {
            e.printStackTrace();
            // Vous pouvez également afficher une alerte à l'utilisateur en cas d'échec
            System.out.println("Erreur lors de la suppression de la question");
        }
    }
}
