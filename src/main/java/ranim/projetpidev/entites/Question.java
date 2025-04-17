package ranim.projetpidev.entites;

public class Question {
    private int id;
    private int quizId;
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String correctAnswer;

    // La réponse choisie par l'utilisateur (non stockée en base, juste pour vérif)
    private String userAnswer;

    // Constructeur sans paramètres
    public Question() {}

    // Constructeur avec tous les paramètres
    public Question(int quizId, String questionText, String optionA, String optionB, String optionC, String correctAnswer) {
        this.quizId = quizId;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.correctAnswer = correctAnswer;
    }

    // Constructeur avec l'ID et tous les autres paramètres
    public Question(int id, int quizId, String questionText, String optionA, String optionB, String optionC, String correctAnswer) {
        this.id = id;
        this.quizId = quizId;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.correctAnswer = correctAnswer;
    }

    // Constructeur simplifié avec uniquement la question
    public Question(String questionText) {
        this.questionText = questionText;
        this.optionA = "";
        this.optionB = "";
        this.optionC = "";
        this.correctAnswer = "";
    }

    // Getters/Setters
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

    public boolean isCorrect() {
        return correctAnswer != null && correctAnswer.equals(userAnswer);
    }
}
