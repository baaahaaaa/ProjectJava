package ranim.projetpidev.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import ranim.projetpidev.entites.Agent;
import ranim.projetpidev.entites.Student;
import ranim.projetpidev.entites.Tutor;
import ranim.projetpidev.entites.User;
import ranim.projetpidev.services.UserService;

import java.time.LocalDate;

public class AjouterUserController {

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField email;

    @FXML
    private TextField entryDate;

    @FXML
    private TextField password;

    // Champs spécifiques Agent
    @FXML
    private TextField companyName;

    @FXML
    private TextField location;

    // Champ spécifique Tutor
    @FXML
    private TextField domain;

    private final UserService userService = new UserService();

    // 👔 Agent
    @FXML
    void addAgent(ActionEvent event) {
        Agent user = new Agent();
        user.setFirstName(firstName.getText());
        user.setLastName(lastName.getText());
        user.setEmail(email.getText());
        user.setEntryDate(LocalDate.parse(entryDate.getText()));
        user.setPassword(password.getText());

        user.setType("agent");
        user.setCompanyName(companyName.getText());
        user.setLocation(location.getText());

        userService.add(user);
        System.out.println("Agent ajouté avec succès !");
    }
    @FXML
    void addStudent(ActionEvent event) {
        Student user = new Student();
        user.setFirstName(firstName.getText());
        user.setLastName(lastName.getText());
        user.setEmail(email.getText());
        user.setEntryDate(LocalDate.parse(entryDate.getText()));
        user.setPassword(password.getText());
        user.setType("student");
        userService.add(user);
        System.out.println("Stduent added succssfully");
    }
    @FXML
    void addTutor(ActionEvent event) {
        Tutor user = new Tutor();
        user.setFirstName(firstName.getText());
        user.setLastName(lastName.getText());
        user.setEmail(email.getText());
        user.setEntryDate(LocalDate.parse(entryDate.getText()));
        user.setPassword(password.getText());

        user.setType("tutor"); // Automatique
        user.setDomain(domain.getText());

        userService.add(user);
        System.out.println("✅ Tuteur ajouté avec succès !");
    }
}
