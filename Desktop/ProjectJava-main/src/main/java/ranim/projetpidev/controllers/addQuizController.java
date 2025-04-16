package ranim.projetpidev.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import ranim.projetpidev.services.QuizService;
import ranim.projetpidev.entites.Quiz;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;

public class addQuizController {

    @FXML
    private TextArea desc;

    @FXML
    private TextField titre;

    @FXML
    private ComboBox<String> type;

    // Méthode pour initialiser le ComboBox avec des éléments par défaut
    @FXML
    public void initialize() {
        type.setItems(FXCollections.observableArrayList(
                "Aéronautique", "Biotechnologie", "Chimie", "Civil Engineering", "Data Science",
                "Informatique", "Mathematics", "Mécanique", "Nanotechnologie", "Programmation",
                "Python", "Java", "Télécommunications", "Français"
        ));
    }

    // Méthode pour gérer l'ajout du quiz
    @FXML
    public void handleAddQuiz() {
        String title = titre.getText();
        String description = desc.getText();
        String quizType = type.getValue();

        if (title.isEmpty() || description.isEmpty() || quizType == null) {
            showAlert("Erreur", "Tous les champs sont obligatoires !");
            return;
        }

        Quiz newQuiz = new Quiz(title, description, quizType);
        QuizService quizService = new QuizService();
        quizService.add(newQuiz);

        int quizId = quizService.getLastInsertedId();

        showAlert("Succès", "Quiz ajouté avec succès !");
        titre.clear();
        desc.clear();
        type.setValue(null);
    }

    // Méthode pour ouvrir la liste des quiz
    @FXML
    public void handleVoirListeQuiz() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/listQuiz.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Liste des Quiz");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible d'ouvrir la liste des quiz.");
        }
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}