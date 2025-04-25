package ranim.projetpidev.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import ranim.projetpidev.entites.Course;

import java.io.IOException;

public class PanierItemCellController extends ListCell<Course> {
    @FXML
    private HBox root;
    @FXML
    private Label titleLabel;
    @FXML
    private Label priceLabel;

    public PanierItemCellController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/PanierItemCell.fxml"));
            loader.setController(this);
            root = loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void updateItem(Course course, boolean empty) {
        super.updateItem(course, empty);
        if (empty || course == null) {
            setGraphic(null);
        } else {
            titleLabel.setText(course.getTitle());
            priceLabel.setText(String.format("%.2f DT", course.getPrice()));
            setGraphic(root);
        }
    }

    @FXML
    public void supprimerDuPanier(ActionEvent actionEvent) {
        Course course = getItem();
        if (course != null) {
            getListView().getItems().remove(course);
        }
    }

}