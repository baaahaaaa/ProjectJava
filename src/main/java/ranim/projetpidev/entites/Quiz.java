package ranim.projetpidev.entites;

import java.util.ArrayList;
import java.util.List;

public class Quiz {
    private int id;
    private String title;
    private String description;
    private String types;

    // Mets à jour tes constructeurs :
    public Quiz(String title, String description, String types) {
        this.title = title;
        this.description = description;
        this.types = types;
    }

    public Quiz(int id, String title, String description, String type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.types = type;
    }

    // Relation avec les questions (non obligatoire si tu charges à part)
    private List<Question> questions = new ArrayList<>();

    public Quiz() {}

    public Quiz(String title, String description) {
        this.title = title;
        this.description = description;
    }


    public Quiz(int id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }

    // Getters et Setters
    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }


    public String getTypes() {
        return types;
    }

    public void setTypes(String type) {
        this.types = type;
    }

    public void addQuestion(Question question) {
        question.setQuizId(this.id); // Assure la liaison
        this.questions.add(question);
    }

    @Override
    public String toString() {
        return title;
    }
}