package ranim.projetpidev.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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
    private TextField domain;

    private final UserService userService = new UserService();

    // 👔 Agent
    @FXML
    void addAgent(ActionEvent event) throws SQLException {
        Agent user = new Agent();
        user.setFirstName(firstName.getText());
        user.setLastName(lastName.getText());
        user.setEmail(email.getText());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = entryDate.getValue();
        if (date == null) {
            System.err.println("❌ Veuillez choisir une date !");
            return;
        }
        user.setEntryDate(date);
        user.setPassword(passwordField.getText());
        user.setType("agent");
        user.setCompanyName(companyName.getText());
        user.setLocation(locationField.getText());

        userService.add(user);
        System.out.println("Agent ajouté avec succès !");
    }
    @FXML
    void addStudent(ActionEvent event) throws SQLException {
        Student user = new Student();
        user.setFirstName(firstName.getText());
        user.setLastName(lastName.getText());
        user.setEmail(email.getText());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = entryDate.getValue();
        if (date == null) {
            System.err.println("❌ Veuillez choisir une date !");
            return;
        }
        user.setEntryDate(date);
        user.setPassword(passwordField.getText());
        user.setType("student");
        userService.add(user);
        System.out.println("Stduent added succssfully");
    }
    @FXML
    void addTutor(ActionEvent event) throws SQLException {
        Tutor user = new Tutor();
        user.setFirstName(firstName.getText());
        user.setLastName(lastName.getText());
        user.setEmail(email.getText());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate date = entryDate.getValue();
        if (date == null) {
            System.err.println("❌ Veuillez choisir une date !");
            return;
        }
        user.setEntryDate(date);
        user.setPassword(passwordField.getText());

        user.setType("tutor"); // Automatique
        user.setDomain(domain.getText());

        userService.add(user);
        System.out.println("✅ Tuteur ajouté avec succès !");
    }
}
