package ranim.projetpidev.controllers;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import javafx.application.Platform;
import javafx.concurrent.Worker;
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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
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
import java.util.Timer;
import java.util.TimerTask;
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
        if (panier == null || panier.getCourses().isEmpty()) {
            showError("Erreur", "Votre panier est vide");
                    return;
                }

        try {
            // Calculer le montant total
            double total = panier.getCourses().stream()
                    .mapToDouble(Course::getPrice)
                    .sum();
            
            // Convertir en euros et s'assurer que le montant est au moins 0,50€
            double totalInEuros = total * 0.31; // Conversion approximative TND vers EUR
            if (totalInEuros < 0.50) {
                showError("Erreur", "Le montant minimum pour un paiement est de 0,50€ (environ 1,60 DT)");
                    return;
                }

            long amount = Math.round(totalInEuros * 100); // Convertir en centimes

            // Créer l'intention de paiement
                PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                        .setAmount(amount)
                        .setCurrency("eur")
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                        .setEnabled(true)
                                    .setAllowRedirects(PaymentIntentCreateParams.AutomaticPaymentMethods.AllowRedirects.NEVER)
                                        .build()
                        )
                    .setDescription("Paiement de cours en ligne")
                        .build();

            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Créer la boîte de dialogue de paiement
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Paiement Sécurisé");
            dialog.setHeaderText("Paiement pour vos cours");

            // Créer la WebView pour le formulaire de paiement
            WebView webView = new WebView();
            webView.setPrefSize(500, 400);
            WebEngine engine = webView.getEngine();

            // HTML pour le formulaire de paiement
            String htmlContent = String.format("""
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="utf-8">
                    <title>Paiement</title>
                    <script src="https://js.stripe.com/v3/"></script>
                    <style>
                        body { 
                            font-family: -apple-system, sans-serif; 
                            padding: 20px; 
                            background: #f8f9fa; 
                            color: #333;
                        }
                        .container { 
                            max-width: 450px; 
                            margin: 0 auto; 
                            background: white; 
                            padding: 30px; 
                            border-radius: 12px;
                            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
                        }
                        .amount {
                            font-size: 24px;
                            font-weight: bold;
                            text-align: center;
                            margin-bottom: 20px;
                            color: #2d3748;
                        }
                        .form-row {
                            margin-bottom: 25px;
                        }
                        .form-row label {
                            display: block;
                            margin-bottom: 8px;
                            font-weight: 500;
                            color: #2d3748;
                        }
                        #card-element {
                            padding: 15px;
                            border: 2px solid #e2e8f0;
                            border-radius: 8px;
                            background: white;
                            transition: border-color 0.2s ease;
                        }
                        #card-element.invalid {
                            border-color: #e53e3e;
                        }
                        button { 
                            background: #4CAF50; 
                            color: white; 
                            padding: 16px;
                            border: none; 
                            border-radius: 8px; 
                            width: 100%%;
                            margin: 25px 0 15px; 
                            font-size: 16px;
                            font-weight: 600;
                            cursor: pointer;
                            transition: all 0.2s ease;
                        }
                        button:disabled {
                            background: #9ca3af;
                            cursor: not-allowed;
                        }
                        button:hover:not(:disabled) {
                            background: #43a047;
                        }
                        .success { 
                            color: #2f855a; 
                            margin-top: 10px;
                            text-align: center;
                            font-weight: 500;
                        }
                        .error { 
                            color: #e53e3e; 
                            margin-top: 10px;
                            font-size: 14px;
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="amount">%.2f DT</div>
                        <form id="payment-form">
                            <div class="form-row">
                                <label for="card-element">Informations de carte</label>
                                <div id="card-element"></div>
                            </div>
                            <button type="submit" id="submit-button">Payer maintenant</button>
                            <div id="success-message" class="success"></div>
                        </form>
                    </div>

                    <script>
                        const stripe = Stripe('%s');
                        const elements = stripe.elements();
                        const card = elements.create('card', {
                            style: {
                                base: {
                                    fontSize: '16px',
                                    fontFamily: '-apple-system, BlinkMacSystemFont, Segoe UI, Roboto, sans-serif',
                                    color: '#2d3748',
                                    '::placeholder': {
                                        color: '#a0aec0'
                                    },
                                    padding: '12px'
                                }
                            }
                        });
                        card.mount('#card-element');

                        const form = document.getElementById('payment-form');
                        const submitButton = document.getElementById('submit-button');
                        const successDiv = document.getElementById('success-message');

                        form.addEventListener('submit', async (e) => {
                            e.preventDefault();
                            submitButton.disabled = true;
                            submitButton.textContent = 'Traitement en cours...';

                            const result = await stripe.confirmCardPayment('%s', {
                                payment_method: {
                                    card: card
                                }
                            });

                            if (result.paymentIntent.status === 'succeeded') {
                                successDiv.textContent = 'Paiement réussi!';
                                window.paymentSuccessful = true;
                            }
                        });
                    </script>
                </body>
                </html>
            """, total, STRIPE_PUBLIC_KEY, paymentIntent.getClientSecret());

            engine.loadContent(htmlContent);

            final boolean[] paymentSuccessful = {false};
            final Timer[] timer = {null};
            engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED) {
                    timer[0] = new Timer(true);
                    timer[0].scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
        Platform.runLater(() -> {
                                try {
                                    Boolean success = (Boolean) engine.executeScript("window.paymentSuccessful === true");
                                    if (Boolean.TRUE.equals(success) && !paymentSuccessful[0]) {
                                        paymentSuccessful[0] = true;
                                        if (timer[0] != null) {
                                            timer[0].cancel();
                                            timer[0] = null;
                                        }
                                        dialog.close();
                                        finalizePayment();
                                    }
                                } catch (Exception e) {
                                    // Ignorer les erreurs de script
                                }
                            });
                        }
                    }, 1000, 500);
                }
            });

            dialog.setOnCloseRequest(event -> {
                if (timer[0] != null) {
                    timer[0].cancel();
                    timer[0] = null;
                }
            });

            ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().add(cancelButton);
            dialog.getDialogPane().setContent(webView);
            dialog.getDialogPane().setPrefSize(550, 600);

            dialog.showAndWait();

        } catch (Exception e) {
                e.printStackTrace();
            showError("Erreur", "Une erreur est survenue lors du paiement: " + e.getMessage());
        }
    }

    private void finalizePayment() {
        try {
            // Marquer les cours comme payés dans la base de données
            panierService.validerPanier(panier.getId());
            
            // Vider le panier en mémoire
            panier.getCourses().clear();
            
            // Mettre à jour l'interface
            updatePanierButton();
            updateTotal();
            
            showSuccess("Succès", "Paiement effectué avec succès");
            
            // Fermer la fenêtre du panier
            if (panierStage != null) {
                panierStage.close();
                panierStage = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Erreur", "Erreur lors de la finalisation du paiement: " + e.getMessage());
        }
    }

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