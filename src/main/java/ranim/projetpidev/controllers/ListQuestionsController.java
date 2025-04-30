package ranim.projetpidev.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ranim.projetpidev.entites.Question;
import ranim.projetpidev.services.QuestionService;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListQuestionsController {

    @FXML private TableView<Question> questionTable;
    @FXML private TableColumn<Question, String> colQuestion;
    @FXML private TableColumn<Question, Void> colAfficher;
    @FXML private TableColumn<Question, Void> colModifier;
    @FXML private TableColumn<Question, Void> colSupprimer;

    private int quizId;

    // Méthode pour initialiser l'affichage des questions
    public void setQuizId(int quizId) {
        this.quizId = quizId;
        chargerQuestionsDepuisBase();
    }

    // Méthode pour rafraîchir la liste des questions
    public void refreshQuestions() {
        setQuizId(this.quizId);
    }

    // Méthode pour charger les questions depuis la base de données
    private void chargerQuestionsDepuisBase() {
        try {
            QuestionService service = new QuestionService();
            List<Question> questions = service.getQuestionsByQuizId(quizId);
            ObservableList<Question> observableQuestions = FXCollections.observableArrayList(questions);
            questionTable.setItems(observableQuestions);

            colQuestion.setCellValueFactory(new PropertyValueFactory<>("questionText"));

            // Bouton Afficher (Afficher les détails de la question)
            colAfficher.setCellFactory(param -> new TableCell<>() {
                private final Button btn = new Button("Afficher");
                {
                    btn.setOnAction(event -> {
                        Question q = getTableView().getItems().get(getIndex());
                        afficherDetailsQuestion(q);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : btn);
                }
            });

            // Bouton Modifier (Modifier la question)
            colModifier.setCellFactory(param -> new TableCell<>() {
                private final Button btn = new Button("Modifier");
                {
                    btn.setOnAction(event -> {
                        Question selected = getTableView().getItems().get(getIndex());
                        modifierQuestion(selected);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : btn);
                }
            });

            // Bouton Supprimer (Supprimer la question)
            colSupprimer.setCellFactory(param -> new TableCell<>() {
                private final Button btn = new Button("Supprimer");
                {
                    btn.setOnAction(event -> {
                        Question q = getTableView().getItems().get(getIndex());
                        confirmerSuppression(q);
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    setGraphic(empty ? null : btn);
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
            afficherAlerte("Erreur", "Une erreur est survenue lors du chargement des questions.");
        }
    }

    // Méthode pour afficher les détails d'une question
    private void afficherDetailsQuestion(Question question) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/questionDetails.fxml"));
            Parent root = loader.load();

            // Obtenez le contrôleur de QuestionDetailsController
            QuestionDetailsController controller = loader.getController();
            controller.setQuestion(question);  // Passer la question au contrôleur

            // Afficher la nouvelle fenêtre avec les détails
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails de la question");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour modifier une question
    private void modifierQuestion(Question selected) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/editQuestion.fxml"));
            Parent root = loader.load();

            EditQuestionController controller = loader.getController();
            controller.setQuestion(selected);
            controller.setOnUpdateCallback(this::refreshQuestions);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier la question");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour confirmer la suppression d'une question
    private void confirmerSuppression(Question q) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Suppression");
        confirm.setHeaderText("Confirmer la suppression ?");
        confirm.setContentText("Voulez-vous vraiment supprimer la question : " + q.getQuestionText());

        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    QuestionService qs = new QuestionService();
                    qs.delete(q.getId());  // Utilisation de l'ID pour supprimer
                    questionTable.getItems().remove(q);
                } catch (SQLException e) {
                    e.printStackTrace();
                    afficherAlerte("Erreur", "Une erreur est survenue lors de la suppression.");
                }
            }
        });
    }

    // Méthode pour ajouter une nouvelle question
    @FXML
    private void handleAjouterQuestion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/addQuestions.fxml"));
            Parent root = loader.load();

            addQuestionsController controller = loader.getController();
            controller.setQuizId(quizId);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Ajouter une question");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Méthode pour afficher des alertes d'erreur
    private void afficherAlerte(String titre, String contenu) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(contenu);
        alert.showAndWait();
    }
}
