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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javafx.application.Platform;
import org.json.JSONObject;

public class UserCourseResourceController {
    private static final String RESOURCES_BASE_DIR = "src/main/resources/ranim/projetpidev/resources/";
    
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

        // Boutons
        HBox buttonsContainer = new HBox(10);
        Button openButton = new Button("Ouvrir la ressource");
        openButton.getStyleClass().add("open-resource-btn");
        openButton.setOnAction(e -> openResource(resource));

        // Ajouter le bouton de résumé uniquement pour les PDF
        if ("PDF".equalsIgnoreCase(resource.getFormat())) {
            Button summarizeButton = new Button("Résumer le contenu");
            summarizeButton.getStyleClass().add("summarize-btn");
            summarizeButton.setOnAction(e -> summarizePDF(resource));
            buttonsContainer.getChildren().addAll(openButton, summarizeButton);
        } else {
            buttonsContainer.getChildren().add(openButton);
        }

        card.getChildren().addAll(title, description, details, preview, buttonsContainer);
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
            // Construire le chemin complet du fichier
            String filePath = resource.getFilePath();
            File file;
            
            // Si le chemin est relatif, ajouter le répertoire de base
            if (!filePath.startsWith("/") && !filePath.contains(":")) {
                file = new File(RESOURCES_BASE_DIR + filePath);
            } else {
                file = new File(filePath);
            }

            if (!file.exists()) {
                showError("Erreur", "Le fichier n'existe pas à l'emplacement : " + file.getAbsolutePath());
                return;
            }

            if ("PDF".equalsIgnoreCase(resource.getFormat())) {
                openPDF(file);
            } else if ("Image".equalsIgnoreCase(resource.getFormat())) {
                openImage(file);
            } else {
                if (java.awt.Desktop.isDesktopSupported()) {
                    java.awt.Desktop.getDesktop().open(file);
                } else {
                    showError("Erreur", "Impossible d'ouvrir le fichier. Vérifiez que vous avez un programme par défaut pour ce type de fichier.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur", "Impossible d'ouvrir la ressource: " + e.getMessage());
        }
    }

    private void openPDF(File pdfFile) {
        try {
            // Essayer d'ouvrir avec le visualiseur PDF par défaut
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().open(pdfFile);
            } else {
                // Si Desktop n'est pas supporté, essayer d'ouvrir avec le navigateur
                String os = System.getProperty("os.name").toLowerCase();
                if (os.contains("win")) {
                    // Pour Windows
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + pdfFile.getAbsolutePath());
                } else if (os.contains("mac")) {
                    // Pour Mac
                    Runtime.getRuntime().exec("open " + pdfFile.getAbsolutePath());
                } else {
                    // Pour Linux
                    Runtime.getRuntime().exec("xdg-open " + pdfFile.getAbsolutePath());
                }
            }
        } catch (Exception e) {
            showError("Erreur", "Impossible d'ouvrir le PDF. Assurez-vous d'avoir un visualiseur PDF installé.");
        }
    }

    private void openImage(File imageFile) {
        try {
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop.getDesktop().open(imageFile);
            } else {
                showError("Erreur", "Impossible d'ouvrir l'image. Vérifiez que vous avez un visualiseur d'images installé.");
            }
        } catch (Exception e) {
            showError("Erreur", "Impossible d'ouvrir l'image: " + e.getMessage());
        }
    }

    private void summarizePDF(Ressource resource) {
        try {
            // Construire le chemin complet du fichier
            String filePath = resource.getFilePath();
            File file;
            
            if (!filePath.startsWith("/") && !filePath.contains(":")) {
                file = new File(RESOURCES_BASE_DIR + filePath);
            } else {
                file = new File(filePath);
            }

            if (!file.exists()) {
                showError("Erreur", "Le fichier n'existe pas à l'emplacement : " + file.getAbsolutePath());
                return;
            }

            // Afficher une boîte de dialogue de chargement simple
            Alert loadingAlert = new Alert(Alert.AlertType.INFORMATION);
            loadingAlert.setTitle("Traitement en cours");
            loadingAlert.setHeaderText("Génération du résumé...");
            loadingAlert.setContentText("Veuillez patienter pendant le traitement du PDF.");
            loadingAlert.getDialogPane().getButtonTypes().clear();
            loadingAlert.show();

            // Exécuter le script Python dans un thread séparé
            new Thread(() -> {
                try {
                    ProcessBuilder processBuilder = new ProcessBuilder(
                        "python",
                        "src/main/python/pdf_summarizer.py",
                        file.getAbsolutePath()
                    );
                    processBuilder.redirectErrorStream(true);
                    Process process = processBuilder.start();

                    // Lire la sortie du script
                    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    StringBuilder output = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        output.append(line);
                    }

                    int exitCode = process.waitFor();
                    Platform.runLater(() -> {
                        loadingAlert.close();
                        if (exitCode == 0) {
                            try {
                                // Parser la réponse JSON
                                JSONObject jsonResponse = new JSONObject(output.toString());
                                if (jsonResponse.has("summary")) {
                                    String summary = jsonResponse.getString("summary");
                                    showSummaryDialog(summary);
                                } else if (jsonResponse.has("error")) {
                                    showError("Erreur", jsonResponse.getString("error"));
                                }
                            } catch (Exception e) {
                                showError("Erreur", "Erreur lors du traitement de la réponse : " + e.getMessage());
                            }
                        } else {
                            showError("Erreur", "Erreur lors de l'exécution du script Python");
                        }
                    });
                } catch (Exception e) {
                    Platform.runLater(() -> {
                        loadingAlert.close();
                        showError("Erreur", "Erreur lors du traitement du PDF: " + e.getMessage());
                    });
                }
            }).start();

        } catch (Exception e) {
            showError("Erreur", "Impossible de traiter le PDF: " + e.getMessage());
        }
    }

    private void showSummaryDialog(String summary) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Résumé du document");
        dialog.setHeaderText("Voici le résumé du contenu :");

        TextArea summaryArea = new TextArea(summary);
        summaryArea.setEditable(false);
        summaryArea.setWrapText(true);
        summaryArea.setPrefRowCount(15);
        summaryArea.setPrefColumnCount(50);

        dialog.getDialogPane().setContent(summaryArea);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
} 