package ranim.projetpidev.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ranim.projetpidev.entites.Quiz;
import ranim.projetpidev.controllers.QuizExecutionController;

import java.io.IOException;
import javafx.scene.layout.FlowPane;
import javafx.scene.control.TextField;
import java.util.List;
import java.util.ArrayList;
import ranim.projetpidev.services.QuizService;

public class FrontListeQuizController {
    @FXML private FlowPane quizContainer;
    @FXML private TextField searchField;
    @FXML private Button previousButton;
    @FXML private Button nextButton;

    private List<Quiz> allQuizzes;
    private final QuizService quizService = new QuizService();
    private int currentPage = 0;
    private final int quizzesPerPage = 8;

    @FXML
    public void initialize() {
        allQuizzes = quizService.getAll(); // ou quizService.search("")
        updateQuizDisplay();
    }

    private void updateQuizDisplay() {
        quizContainer.getChildren().clear();
        int start = currentPage * quizzesPerPage;
        int end = Math.min(start + quizzesPerPage, allQuizzes.size());

        List<Quiz> pageQuizzes = allQuizzes.subList(start, end);

        for (Quiz quiz : pageQuizzes) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/QuizListCard.fxml"));
                Parent card = loader.load();
                QuizListCell controller = loader.getController();
                controller.setData(quiz);
                quizContainer.getChildren().add(card);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void onFilterClicked() {
        String query = searchField.getText();
        allQuizzes = quizService.search(query);
        currentPage = 0;
        updateQuizDisplay();
    }

    @FXML
    private void handleNextPage() {
        if ((currentPage + 1) * quizzesPerPage < allQuizzes.size()) {
            currentPage++;
            updateQuizDisplay();
        }
    }

    @FXML
    private void handlePreviousPage() {
        if (currentPage > 0) {
            currentPage--;
            updateQuizDisplay();
        }
    }


}
