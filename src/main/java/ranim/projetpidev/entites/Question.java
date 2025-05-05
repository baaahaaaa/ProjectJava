package ranim.projetpidev.entites;

import java.util.ArrayList;
import java.util.List;

public class Question {

    private int id;
    private int quizId;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String correctAnswer;

    // Réponse choisie par l’utilisateur (non stockée en base)
    private String userAnswer;

    // === CONSTRUCTEURS ===
    public Question() {}

    public Question(int quizId, String questionText, String optionA, String optionB, String optionC, String correctAnswer) {
        this.quizId = quizId;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.correctAnswer = correctAnswer;
    }

    public Question(int id, int quizId, String questionText, String optionA, String optionB, String optionC, String correctAnswer) {
        this.id = id;
        this.quizId = quizId;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.correctAnswer = correctAnswer;
    }

    public Question(String questionText) {
        this.questionText = questionText;
        this.optionA = "";
        this.optionB = "";
        this.optionC = "";
        this.correctAnswer = "";
    }

    // === GETTERS & SETTERS ===
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getQuizId() { return quizId; }
    public void setQuizId(int quizId) { this.quizId = quizId; }

    public String getQuestionText() { return questionText; }
    public void setQuestionText(String questionText) { this.questionText = questionText; }

    public String getOptionA() { return optionA; }
    public void setOptionA(String optionA) { this.optionA = optionA; }

    public String getOptionB() { return optionB; }
    public void setOptionB(String optionB) { this.optionB = optionB; }

    public String getOptionC() { return optionC; }
    public void setOptionC(String optionC) { this.optionC = optionC; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    public String getUserAnswer() { return userAnswer; }
    public void setUserAnswer(String userAnswer) { this.userAnswer = userAnswer; }

    // === MÉTHODES UTILES ===

    public boolean isCorrect() {
        return correctAnswer != null && correctAnswer.equals(userAnswer);
    }

    /**
     * Retourne l’intitulé de la question (utilisé dans l’affichage)
     */
    public String getText() {
        return questionText;
    }

    /**
     * Retourne la liste des réponses disponibles
     */
    public List<String> getChoices() {
        List<String> choices = new ArrayList<>();
        if (optionA != null && !optionA.isEmpty()) choices.add(optionA);
        if (optionB != null && !optionB.isEmpty()) choices.add(optionB);
        if (optionC != null && !optionC.isEmpty()) choices.add(optionC);
        return choices;
    }

    @Override
    public String toString() {
        return questionText;
    }
}