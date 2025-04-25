package ranim.projetpidev.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import ranim.projetpidev.entites.Course;
import ranim.projetpidev.entites.Ressource;
import ranim.projetpidev.services.RessourceService;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import java.io.File;
import java.util.List;

public class UserCourseResourceController {
    @FXML
    private Text courseTitle;
    @FXML
    private Text courseDescription;
    @FXML
    private Label courseDomain;
    @FXML
    private Label courseType;
    @FXML
    private Label coursePrice;
    @FXML
    private VBox resourcesContainer;

    private final RessourceService ressourceService = new RessourceService();
    private Course currentCourse;

    public void setCourse(Course course) {
        this.currentCourse = course;
        displayCourseInfo();
        loadResources();
    }

    private void displayCourseInfo() {
        courseTitle.setText(currentCourse.getTitle());
        courseDescription.setText(currentCourse.getDescription());
        courseDomain.setText(currentCourse.getDomain());
        courseType.setText(currentCourse.getType());
        coursePrice.setText(String.format("%.2f DT", currentCourse.getPrice()));
    }

    private void loadResources() {
        List<Ressource> resources = ressourceService.getRessourcesByCourseId(currentCourse.getId());
        resourcesContainer.getChildren().clear();

        for (Ressource resource : resources) {
            VBox resourceCard = createResourceCard(resource);
            resourcesContainer.getChildren().add(resourceCard);
        }
    }

    private VBox createResourceCard(Ressource resource) {
        VBox card = new VBox(10);
        card.getStyleClass().add("resource-card");
        card.setPadding(new Insets(15));

        // Titre et description
        Text title = new Text(resource.getTitle());
        title.getStyleClass().add("resource-title");

        Text description = new Text(resource.getDescription());
        description.getStyleClass().add("resource-description");
        description.setWrappingWidth(400);

        // Format et date
        HBox details = new HBox(10);
        Label format = new Label("Format: " + resource.getFormat());
        Label date = new Label("Date: " + resource.getCreationDate().toString());
        details.getChildren().addAll(format, date);

        // Aperçu
        Node preview = createPreview(resource);

        // Bouton pour ouvrir/télécharger la ressource
        Button openButton = new Button("Ouvrir la ressource");
        openButton.getStyleClass().add("open-resource-btn");
        openButton.setOnAction(e -> openResource(resource));

        card.getChildren().addAll(title, description, details, preview, openButton);
        return card;
    }

    private Node createPreview(Ressource resource) {
        if ("Image".equalsIgnoreCase(resource.getFormat())) {
            try {
                File file = new File(resource.getFilePath());
                if (file.exists()) {
                    ImageView imageView = new ImageView(new Image(file.toURI().toString()));
                    imageView.setFitWidth(200);
                    imageView.setFitHeight(150);
                    imageView.setPreserveRatio(true);
                    return imageView;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // Pour les autres formats ou si l'image ne peut pas être chargée
        Label formatLabel = new Label(resource.getFormat().toUpperCase());
        formatLabel.getStyleClass().add("format-label");
        return formatLabel;
    }

    private void openResource(Ressource resource) {
        try {
            File file = new File(resource.getFilePath());
            if (file.exists()) {
                java.awt.Desktop.getDesktop().open(file);
            } else {
                showError("Erreur", "Le fichier n'existe pas");
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur", "Impossible d'ouvrir la ressource");
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 