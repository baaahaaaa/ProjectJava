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
import ranim.projetpidev.entites.Quiz;
import ranim.projetpidev.services.QuizService;

import java.io.IOException;
import java.util.List;

public class ListQuizController {

    @FXML private TableView<Quiz> quizTable;
    @FXML private TableColumn<Quiz, String> colTitle;
    @FXML private TableColumn<Quiz, String> colType;
    @FXML private TableColumn<Quiz, Void> colDetails;
    @FXML private TableColumn<Quiz, Void> colEdit;
    @FXML private TableColumn<Quiz, Void> colDelete;

    private final QuizService quizService = new QuizService();

    @FXML
    public void initialize() {
        loadQuizData();
    }

    private void loadQuizData() {
        List<Quiz> quizList = quizService.getAll();
        ObservableList<Quiz> observableList = FXCollections.observableArrayList(quizList);

        quizTable.setItems(observableList);

        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colType.setCellValueFactory(new PropertyValueFactory<>("types"));

        // Bouton Détails
        colDetails.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Détails");

            {
                btn.setOnAction(event -> {
                    Quiz selectedQuiz = getTableView().getItems().get(getIndex());
                    showQuizDetails(selectedQuiz);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Bouton Modifier
        colEdit.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Modifier");
            {
                btn.setOnAction(event -> {
                    Quiz quiz = getTableView().getItems().get(getIndex());
                    showEditForm(quiz);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : btn);
            }
        });

        // Bouton Supprimer
        colDelete.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("Supprimer");
            {
                btn.setOnAction(event -> {
                    Quiz q = getTableView().getItems().get(getIndex());
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
                    confirm.setTitle("Confirmation de suppression");
                    confirm.setHeaderText("Voulez-vous vraiment supprimer ce quiz ?");
                    confirm.setContentText(q.getTitle());

                    confirm.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            quizService.delete(q.getId()); // Suppression par ID
                            quizTable.getItems().remove(q);
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

    private void showQuizDetails(Quiz quiz) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/quizDetails.fxml"));
            Parent root = loader.load();

            QuizDetailsController controller = loader.getController();
            controller.setQuiz(quiz);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Détails du quiz");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showEditForm(Quiz quiz) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/editQuiz.fxml"));
            Parent root = loader.load();

            EditQuizController controller = loader.getController();
            controller.setQuizToEdit(quiz);
            controller.setListQuizController(this);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Modifier le quiz");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshTable() {
        loadQuizData();
    }
}
