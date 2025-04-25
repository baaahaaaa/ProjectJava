package ranim.projetpidev.controllers;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import ranim.projetpidev.entites.Course;
import ranim.projetpidev.entites.Panier;
import ranim.projetpidev.entites.User;
import ranim.projetpidev.services.CourseService;
import ranim.projetpidev.services.PanierService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.text.Text;
import javafx.geometry.Insets;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDate;


import java.awt.*;


public class UserCourseController {
    @FXML
    private FlowPane coursesContainer;
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> domainFilter;
    @FXML
    private Button panierButton;
    @FXML
    private ListView<Course> panierListView;
    @FXML
    private Label totalLabel;

    private final CourseService courseService = new CourseService();
    private final PanierService panierService = new PanierService();
    private ObservableList<Course> allCourses;
    private Panier panier;
    private Stage panierStage;
    private User currentUser;
    private static final User STATIC_USER;


    //paiement
   private static final String STRIPE_SECRET_KEY="sk_test_51Qw9l3JttpF5uKzzIz29llGOkeY3twnnJgQfClAGd9TrLuBkuegCs6weBHGtvhX5CpGeK94goNllrdzQ5rh6cEev00KDFu7qdc";
    private static final String     STRIPE_PUBLIC_KEY="pk_test_51Qw9l3JttpF5uKzzrclxmJGB6WLt3lYtm44b96TNuKfCvmuIyLtcs5JS8ieMPf3JHvG13oLEUaIFaJ1IULHG1vzm00b42hWkzD";


    static {
        STATIC_USER = new User();
        STATIC_USER.setId(6); // Utilisateur existant avec ID 6
    }

    public UserCourseController() {
        this.currentUser = STATIC_USER;
    }

    @FXML
    public void initialize() {
        loadCourses();
        setupSearch();
        setupFilters();
        initializePanier(); // Initialiser le panier après avoir défini l'utilisateur
        Stripe.apiKey=STRIPE_SECRET_KEY;
    }

    private void initializePanier() {
        try {
            // Récupérer le panier existant ou en créer un nouveau
            int panierId = panierService.getPanierIdByUserId(currentUser.getId());
            if (panierId == -1) {
                panierId = panierService.creerPanierPourUser(currentUser.getId());
            }

            panier = new Panier(panierId);
            // Charger les cours existants dans le panier
            List<Course> courses = panierService.getCoursesDuPanier(panierId);
            if (courses != null && !courses.isEmpty()) {
                panier.getCourses().clear(); // Vider le panier avant d'ajouter les cours
                panier.getCourses().addAll(courses);
            }
            
            updatePanierButton();
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur", "Impossible d'initialiser le panier: " + e.getMessage());
        }
    }

    private void updatePanierButton() {
        if (panier != null) {
            int itemCount = panier.getCourses().size();
            panierButton.setText("Panier (" + itemCount + ")");
        }
    }

    private void loadCourses() {
        allCourses = FXCollections.observableArrayList(courseService.rechercher());
        displayCourses(allCourses);
    }

    private void setupSearch() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterCourses();
        });
    }

    private void setupFilters() {
        List<String> domains = allCourses.stream()
                .map(Course::getDomain)
                .distinct()
                .collect(Collectors.toList());
        domainFilter.setItems(FXCollections.observableArrayList(domains));
        
        domainFilter.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            filterCourses();
        });
    }

    private void filterCourses() {
        String searchText = searchField.getText().toLowerCase();
        String selectedDomain = domainFilter.getValue();

        List<Course> filteredCourses = allCourses.stream()
                .filter(course -> 
                    (searchText.isEmpty() || course.getTitle().toLowerCase().contains(searchText)) &&
                    (selectedDomain == null || course.getDomain().equals(selectedDomain)))
                .collect(Collectors.toList());

        displayCourses(filteredCourses);
    }

    private void displayCourses(List<Course> courses) {
        coursesContainer.getChildren().clear();

        for (Course course : courses) {
            VBox courseCard = createCourseCard(course);
            coursesContainer.getChildren().add(courseCard);
        }
    }

    private VBox createCourseCard(Course course) {
        VBox card = new VBox(10);
        card.getStyleClass().add("course-card");
        card.setPadding(new Insets(15));
        card.setPrefWidth(300);

        // Image du cours
        ImageView courseImage = new ImageView();
        courseImage.setFitWidth(270);
        courseImage.setFitHeight(150);
        courseImage.setPreserveRatio(true);

        try {
            Image image = new Image(getClass().getResourceAsStream("/ranim/projetpidev/images/course-default.png"));
            if (image.isError()) {
                Rectangle placeholder = new Rectangle(270, 150);
                placeholder.setFill(Color.LIGHTGRAY);
                card.getChildren().add(placeholder);
            } else {
                courseImage.setImage(image);
                card.getChildren().add(courseImage);
            }
        } catch (Exception e) {
            Rectangle placeholder = new Rectangle(270, 150);
            placeholder.setFill(Color.LIGHTGRAY);
            card.getChildren().add(placeholder);
        }

        Text title = new Text(course.getTitle());
        title.getStyleClass().add("card-title");

        Text description = new Text(course.getDescription());
        description.getStyleClass().add("card-description");
        description.setWrappingWidth(260);

        Label domain = new Label(course.getDomain());
        domain.getStyleClass().add("card-domain");

        Label price = new Label(String.format("%.2f DT", course.getPrice()));
        price.getStyleClass().add("card-price");

        HBox buttonsContainer = new HBox(10);
        buttonsContainer.setAlignment(javafx.geometry.Pos.CENTER);

        Button viewButton = new Button("Voir les ressources");
        viewButton.getStyleClass().add("view-resources-btn");
        viewButton.setOnAction(e -> openCourseResources(course));

        Button addToCartButton = new Button("Ajouter au panier");
        addToCartButton.getStyleClass().add("add-to-cart-btn");
        addToCartButton.setOnAction(e -> ajouterAuPanier(course));

        buttonsContainer.getChildren().addAll(viewButton, addToCartButton);

        card.getChildren().addAll(title, description, domain, price, buttonsContainer);
        return card;
    }

    private void ajouterAuPanier(Course course) {
        if (currentUser == null) {
            showError("Erreur", "Veuillez vous connecter pour ajouter des cours au panier");
            return;
        }

        if (panier == null) {
            initializePanier();
            if (panier == null) {
                showError("Erreur", "Impossible d'initialiser le panier");
                return;
            }
        }

        try {
            // Vérifier si le cours est déjà payé
            if (panierService.isCoursePaid(currentUser.getId(), course.getId())) {
                showError("Erreur", "Ce cours a déjà été acheté");
                return;
            }

            // Vérifier si le cours existe déjà dans le panier
            if (panier.getCourses().stream().anyMatch(c -> c.getId() == course.getId())) {
                showError("Erreur", "Ce cours est déjà dans votre panier");
                return;
            }

            // Ajouter le cours au panier
            panier.addCourse(course);
            panierService.ajouterCourseAuPanier(panier.getId(), course.getId());
            updatePanierButton();
            showSuccess("Succès", "Cours ajouté au panier");
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur", "Impossible d'ajouter le cours au panier: " + e.getMessage());
        }
    }

    @FXML
    private void openPanierPopup() {
        if (currentUser == null) {
            showError("Erreur", "Veuillez vous connecter pour accéder au panier");
            return;
        }

        if (panier == null) {
            initializePanier();
            if (panier == null) {
                showError("Erreur", "Impossible d'initialiser le panier");
                return;
            }
        }

        try {
            if (panierStage == null) {
                // Charger le FXML sans définir de contrôleur
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/PanierPopup.fxml"));
                VBox root = loader.load();

                // Récupérer les références aux composants
                panierListView = (ListView<Course>) root.lookup("#panierListView");
                totalLabel = (Label) root.lookup("#totalLabel");
                Button validerButton = (Button) root.lookup("#validerButton");

                // Configurer le bouton Valider
                validerButton.setOnAction(e -> validerPanier());

                // Configurer le ListView avec une cellule personnalisée
                panierListView.setCellFactory(param -> new PanierItemCellController());

                // Mettre à jour les données
                panierListView.setItems(panier.getCourses());
                updateTotal();

                // Configurer la fenêtre
                panierStage = new Stage();
                panierStage.setTitle("Mon Panier");
                panierStage.setScene(new Scene(root));
                panierStage.setOnCloseRequest(e -> panierStage = null);
            }
            panierStage.show();
            panierStage.toFront();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur", "Impossible d'ouvrir le panier");
        }
    }

    @FXML
    private void supprimerDuPanier() {
        if (panier == null) {
            showError("Erreur", "Panier non initialisé");
            return;
        }

        Course selectedCourse = panierListView.getSelectionModel().getSelectedItem();
        if (selectedCourse != null) {
            try {
                panier.removeCourse(selectedCourse);
                panierService.supprimerCourseDuPanier(panier.getId(), selectedCourse.getId());
                updatePanierButton();
                updateTotal();
                showSuccess("Succès", "Cours supprimé du panier");
            } catch (Exception e) {
                showError("Erreur", "Impossible de supprimer le cours du panier: " + e.getMessage());
            }
        }
    }
//**************Paiement

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }


@FXML
private void handleConfirmPaiement() {

    try {

        PanierService P= new PanierService();
        P.ajouterCourseAuPanier( 13, 6);

        showAlert("Success", "Payment successfully!", Alert.AlertType.INFORMATION);
    } catch (Exception e) {
        showAlert("Error", "Failed to do payment: " + e.getMessage(), Alert.AlertType.ERROR);
    }
}


    @FXML
    public void validerPanier() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Process Payment");
        dialog.setHeaderText("Please enter your payment details");

        // Create TextFields for input
        TextField amountField = new TextField();
        amountField.setPromptText("Montant");

        TextField cardNumberField = new TextField();
        cardNumberField.setPromptText("Numéro de carte");

        TextField expMonthField = new TextField();
        expMonthField.setPromptText("Mois d'expiration");

        TextField expYearField = new TextField();
        expYearField.setPromptText("Année d'expiration");

        TextField cvcField = new TextField();
        cvcField.setPromptText("CVC");

        // Create a GridPane to hold the fields
        GridPane grid = new GridPane();
        grid.add(new Label("Montant:"), 0, 0);
        grid.add(amountField, 1, 0);
        grid.add(new Label("Numéro de carte:"), 0, 1);
        grid.add(cardNumberField, 1, 1);
        grid.add(new Label("Mois d'expiration:"), 0, 2);
        grid.add(expMonthField, 1, 2);
        grid.add(new Label("Année d'expiration:"), 0, 3);
        grid.add(expYearField, 1, 3);
        grid.add(new Label("CVC:"), 0, 4);
        grid.add(cvcField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        // Add buttons to the dialog
        ButtonType confirmButton = new ButtonType("Confirmer", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButton, ButtonType.CANCEL);

        // Handle button clicks
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButton) {
                return ButtonType.OK;
            }
            return null;
        });

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                // Get amount and other details from dialog
                String amountStr = amountField.getText().trim();
                if (amountStr.isEmpty()) {
                    showPaymentError("❌ Veuillez entrer un montant");
                    return;
                }

                long amount = Math.round(Double.parseDouble(amountStr) * 100);
                String cardNumber = cardNumberField.getText().replaceAll("\\s+", "");
                if (cardNumber.isEmpty()) {
                    showPaymentError("❌ Veuillez entrer un numéro de carte");
                    return;
                }
                if (cardNumber.length() != 16 || !cardNumber.matches("\\d{16}")) {
                    showPaymentError("❌ Numéro de carte invalide (doit être de 16 chiffres)");
                    return;
                }

                // Expiry date check
                String expiryMonthStr = expMonthField.getText().trim();
                String expiryYearStr = expYearField.getText().trim();
                if (expiryMonthStr.isEmpty() || expiryYearStr.isEmpty()) {
                    showPaymentError("❌ Veuillez entrer la date d'expiration");
                    return;
                }

                int expiryMonth = Integer.parseInt(expiryMonthStr);
                int expiryYear = Integer.parseInt(expiryYearStr);

                int currentYear = LocalDate.now().getYear();
                if (expiryYear < currentYear || (expiryYear == currentYear && expiryMonth < LocalDate.now().getMonthValue())) {
                    showPaymentError("❌ Carte expirée");
                    return;
                }

                // CVC validation
                String cvc = cvcField.getText().trim();
                if (cvc.isEmpty()) {
                    showPaymentError("❌ Veuillez entrer le CVC");
                    return;
                }
                if (cvc.length() != 3 && cvc.length() != 4) {
                    showPaymentError("❌ CVC invalide");
                    return;
                }

                // Additional validations
                if (cardNumber.equals("4000000000009995")) {
                    showPaymentError("❌ Fonds insuffisants");
                    return;
                }
                if (cardNumber.equals("4000000000000002")) {
                    showPaymentError("❌ Carte refusée");
                    return;
                }

                // Call the payment processing logic
                handleConfirmPaiement();

                // Create payment intent
                PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                        .setAmount(amount)
                        .setCurrency("eur")
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                        .setEnabled(true)
                                        .build()
                        )
                        .build();
                PaymentIntent.create(params);

                // Success notification
                showSystemTrayNotification("Paiement Confirmé montant: " + (amount / 100.0));

                // Clear fields after processing
                amountField.clear();
                cardNumberField.clear();
                expMonthField.clear();
                expYearField.clear();
                cvcField.clear();
            } catch (Exception e) {
                e.printStackTrace();
                showPaymentError("❌ Erreur inattendue");
            }
        }
    }
    private void showPaymentError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.setResizable(true);
            alert.getDialogPane().setMinWidth(400);
            alert.showAndWait();
        });
    }

    public void showSystemTrayNotification(String message) {
        if (SystemTray.isSupported()) {
            SystemTray systemTray = SystemTray.getSystemTray();
            TrayIcon trayIcon = new TrayIcon(Toolkit.getDefaultToolkit().getImage("icon.png"), "Application Notification");

            try {
                systemTray.add(trayIcon);
                trayIcon.displayMessage("Notification", message, TrayIcon.MessageType.INFO);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }
    /*
    private void validerPanier() {
        if (panier == null) {
            showError("Erreur", "Panier non initialisé");
            return;
        }

        if (panier.getCourses().isEmpty()) {
            showError("Erreur", "Votre panier est vide");
            return;
        }

        try {
            // Marquer les cours comme payés dans la base de données
            panierService.validerPanier(panier.getId());
            
            // Vider le panier en mémoire
            panier.getCourses().clear();
            
            // Mettre à jour l'interface
            updatePanierButton();
            updateTotal();
            
            showSuccess("Succès", "Panier validé avec succès");
            
            // Fermer la fenêtre du panier
            if (panierStage != null) {
                panierStage.close();
                panierStage = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur", "Impossible de valider le panier: " + e.getMessage());
        }
    }
*/
    private void updateTotal() {
        if (panier != null) {
            double total = panier.getCourses().stream()
                    .mapToDouble(Course::getPrice)
                    .sum();
            totalLabel.setText(String.format("Total: %.2f DT", total));
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showSuccess(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void openCourseResources(Course course) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/UserCourseResourceView.fxml"));
            VBox root = loader.load();

            UserCourseResourceController controller = loader.getController();
            controller.setCourse(course);

            Stage stage = new Stage();
            stage.setTitle("Ressources - " + course.getTitle());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showError("Erreur", "Impossible d'ouvrir les ressources du cours");
        }
    }
} 