package ranim.projetpidev.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ranim.projetpidev.entites.Quiz;
import ranim.projetpidev.services.QuizService;

public class EditQuizController {

    @FXML
    private TextField titreField;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private Button btnEnregistrer;

    private Quiz quizToEdit;

    private final QuizService quizService = new QuizService();

    @FXML
    public void initialize() {
        typeComboBox.setItems(FXCollections.observableArrayList(
                "Aéronautique", "Biotechnologie", "Chimie", "Civil Engineering", "Data Science",
                "Informatique", "Mathematics", "Mécanique", "Nanotechnologie", "Programmation",
                "Python", "Java", "Télécommunications", "Français"
        ));
    }

    public void setQuiz(Quiz quiz) {
        this.quizToEdit = quiz;
        titreField.setText(quiz.getTitle());
        descriptionArea.setText(quiz.getDescription());
        typeComboBox.setValue(quiz.getTypes());
    }

    @FXML
    public void handleUpdateQuiz() {
        if (titreField.getText().isEmpty() || descriptionArea.getText().isEmpty() || typeComboBox.getValue() == null) {
            showAlert("Erreur", "Veuillez remplir tous les champs.");
            return;
        }

        quizToEdit.setTitle(titreField.getText());
        quizToEdit.setDescription(descriptionArea.getText());
        quizToEdit.setTypes(typeComboBox.getValue());

        quizService.update(quizToEdit);

        if (listQuizController != null) {
            listQuizController.refreshTable(); // ✅ rafraîchir la table
        }

        showAlert("Succès", "Quiz mis à jour avec succès !");
        ((Stage) btnEnregistrer.getScene().getWindow()).close();
    }


    private void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private ListQuizController listQuizController;

    public void setListQuizController(ListQuizController controller) {
        this.listQuizController = controller;
    }


    public void setQuizToEdit(Quiz quiz) {
        this.quizToEdit = quiz;
        titreField.setText(quiz.getTitle());
        descriptionArea.setText(quiz.getDescription());
        typeComboBox.setValue(quiz.getTypes());
    }


}
