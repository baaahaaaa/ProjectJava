package ranim.projetpidev.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ranim.projetpidev.entites.Agent;
import ranim.projetpidev.entites.Student;
import ranim.projetpidev.entites.Tutor;
import ranim.projetpidev.entites.User;
import ranim.projetpidev.services.UserService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AjouterUserController {

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField email;

    @FXML
    private DatePicker entryDate;

    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField companyName;

    @FXML
    private TextField locationField;


    // Champ spécifique Tutor
    @FXML
    private ComboBox<String> domainComboBox;  // Remplace domain par ComboBox

    private final UserService userService = new UserService();

    private final ObservableList<String> specialties = FXCollections.observableArrayList(
            "Informatique", "Mathématiques", "Physique", "Chimie", "Langues", "Histoire", "Arts"
    );

    public void initialize() {
        if (domainComboBox != null) {
            domainComboBox.setItems(specialties);
        } else {
            System.err.println("❌ ComboBox domainComboBox non initialisé.");
        }
    }

    // 👔 Agent
    @FXML
    void addAgent(ActionEvent event) throws SQLException {
        Agent user = new Agent();
        user.setFirstName(firstName.getText());
        user.setLastName(lastName.getText());
        user.setEmail(email.getText());
        LocalDate date = entryDate.getValue();
        user.setEntryDate(date);
        user.setPassword(passwordField.getText());
        user.setType("agent");
        user.setCompanyName(companyName.getText());
        user.setLocation(locationField.getText());

        // Vérifier la validation avant d'ajouter l'utilisateur
        String validationMessage = userService.validateUser(user);  // Assurez-vous que validateUser est appelé dans le service
        if (validationMessage != null) {
            System.err.println(validationMessage);
            showAlert("❌ Erreur", validationMessage);
            return;  // Si la validation échoue, on arrête l'ajout et on affiche l'erreur
        }

        // Si la validation est réussie, on ajoute l'utilisateur à la base de données
        userService.add(user);

        // Afficher un message de succès uniquement après un ajout réussi
        showAlert("✅ Succès", "Agent ajouté avec succès !");
        navigateToWelcomePage(user);
    }


    @FXML
    void addStudent(ActionEvent event) throws SQLException {

        Student user = new Student();
        user.setFirstName(firstName.getText());
        user.setLastName(lastName.getText());
        user.setEmail(email.getText());
        LocalDate date = entryDate.getValue();
        user.setEntryDate(date);
        user.setPassword(passwordField.getText());
        user.setType("student");

        // Validat
        String validationMessage = userService.validateUser(user);  // Assurez-vous que validateUser est appelé dans le service
        if (validationMessage != null) {
            System.err.println(validationMessage);
            showAlert("❌ Erreur", validationMessage);
            return;  // Si la validation échoue, on arrête l'ajout et on affiche l'erreur
        }

        userService.add(user);
        showAlert("✅ Succès", "Student added successfully");
        navigateToWelcomePage(user);

    }

    @FXML
    void addTutor(ActionEvent event) throws SQLException {
        Tutor user = new Tutor();
        user.setFirstName(firstName.getText());
        user.setLastName(lastName.getText());
        user.setEmail(email.getText());
        LocalDate date = entryDate.getValue();
        user.setEntryDate(date);
        user.setPassword(passwordField.getText());

        // Utiliser la spécialité choisie dans le ComboBox
        String selectedSpecialty = domainComboBox.getValue();
        if (selectedSpecialty == null) {
            System.err.println("❌ Veuillez choisir une spécialité !");
            return;
        }

        user.setType("tutor"); // Automatique
        user.setDomain(selectedSpecialty);

        String validationMessage = userService.validateUser(user);  // Assurez-vous que validateUser est appelé dans le service
        if (validationMessage != null) {
            System.err.println(validationMessage);
            showAlert("❌ Erreur", validationMessage);
            return;  // Si la validation échoue, on arrête l'ajout et on affiche l'erreur
        }

        // Validation des données

        userService.add(user);
        showAlert("✅ Succès", "Tuteur ajouté avec succès !");
        navigateToWelcomePage(user);
    }

    // Fonction pour afficher une alerte avec un message
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void navigateToWelcomePage(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/Welcome.fxml"));
            Parent root = loader.load();

            WelcomePageController welcomePageController = loader.getController();
            welcomePageController.initData(user);  // Passer l'utilisateur au contrôleur de la page de bienvenue

            Stage stage = new Stage();
            stage.setTitle("Page de Bienvenue");
            stage.setScene(new Scene(root, 400, 300));
            stage.show();

            // Fermer la scène actuelle
            Stage currentStage = (Stage) firstName.getScene().getWindow();
            currentStage.close();  // Si vous voulez fermer la fenêtre actuelle
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
