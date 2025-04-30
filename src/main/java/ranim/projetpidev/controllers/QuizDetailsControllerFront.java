package ranim.projetpidev.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import ranim.projetpidev.entites.Quiz;
import ranim.projetpidev.services.QuestionService;
import ranim.projetpidev.services.QuizService;

public class QuizDetailsControllerFront {

    @FXML private Label titleLabel;
    @FXML private Label typeLabel;
    @FXML private Label descriptionLabel;
    @FXML private Label nbQuestionsLabel;
    @FXML private Label niveauLabel;

    private Quiz quiz;

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;

        titleLabel.setText("Titre : " + quiz.getTitle());
        typeLabel.setText("Type : " + quiz.getTypes());
        descriptionLabel.setText("Description : " + quiz.getDescription());

        QuestionService qs = new QuestionService();
        int nbQuestions = 0;
        try {
            nbQuestions = qs.countQuestionsByQuizId(quiz.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        nbQuestionsLabel.setText("Nombre de questions : " + nbQuestions);

        String niveau = "Facile";
        if (nbQuestions >= 5 && nbQuestions < 10) niveau = "Intermédiaire";
        else if (nbQuestions >= 10) niveau = "Avancé";
        niveauLabel.setText("Niveau : " + niveau);
    }
}
