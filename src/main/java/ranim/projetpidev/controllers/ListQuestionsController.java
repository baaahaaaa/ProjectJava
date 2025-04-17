package ranim.projetpidev.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import ranim.projetpidev.entites.Question;
import ranim.projetpidev.services.QuestionService;

import java.io.IOException;
import java.util.List;

public class ListQuestionsController {

    @FXML private TableView<Question> questionTable;
    @FXML private TableColumn<Question, String> colQuestion;
    @FXML private TableColumn<Question, Void> colAfficher;
    @FXML private TableColumn<Question, Void> colModifier;
    @FXML private TableColumn<Question, Void> colSupprimer;

    private int quizId;

    public void setQuizId(int quizId) {
        this.quizId = quizId;
        loadQuestionsFromDatabase();
    }

    public void refreshQuestions() {
        setQuizId(this.quizId);
    }

    private void loadQuestionsFromDatabase() {
        QuestionService service = new QuestionService();
        List<Question> questions = service.getQuestionsByQuizId(quizId);
        ObservableList<Question> observableQuestions = FXCollections.observableArrayList(questions);
        questionTable.setItems(observableQuestions);

        colQuestion.setCellValueFactory(new PropertyValueFactory<>("questionText"));

        colAfficher.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Afficher");
            {
                btn.setOnAction(event -> {
                    Question q = getTableView().getItems().get(getIndex());
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Détails de la question");
                    alert.setHeaderText(null);
                    alert.setContentText("Q: " + q.getQuestionText() +
                            "\nA: " + q.getOptionA() +
                            "\nB: " + q.getOptionB() +
                            "\nC: " + q.getOptionC() +
                            "\nRéponse correcte: " + q.getCorrectAnswer());
                    alert.showAndWait();
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        colModifier.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Modifier");
            {
                btn.setOnAction(event -> {
                    Question selected = getTableView().getItems().get(getIndex());
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
                });
            }

            private void refreshQuestions() {
                loadQuestionsFromDatabase();
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        colSupprimer.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Supprimer");
            {
                btn.setOnAction(event -> {
                    Question q = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Suppression");
                    confirm.setHeaderText("Confirmer la suppression ?");
                    confirm.setContentText("Supprimer la question : " + q.getQuestionText());

                    confirm.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            QuestionService qs = new QuestionService();
                            qs.delete(q);
                            getTableView().getItems().remove(q);
                        }
                    });
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });
    }

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

}
