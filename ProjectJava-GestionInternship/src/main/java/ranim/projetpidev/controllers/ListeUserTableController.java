package ranim.projetpidev.controllers;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import ranim.projetpidev.entites.*;
import ranim.projetpidev.services.UserService;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ListeUserTableController {

    @FXML private TableView<User> tableView;
    @FXML private TableColumn<User, Integer> idCol;
    @FXML private TableColumn<User, String> firstNameCol;
    @FXML private TableColumn<User, String> lastNameCol;
    @FXML private TableColumn<User, String> emailCol;
    @FXML private TableColumn<User, LocalDate> entryDateCol;
    @FXML private TableColumn<User, String> typeCol;
    @FXML private TableColumn<User, String> companyCol;
    @FXML private TableColumn<User, String> locationCol;
    @FXML private TableColumn<User, String> domainCol;
    private ObservableList<User> allUsers;
    @FXML private ComboBox<String> typeFilter;
    @FXML private TableColumn<User, Void> actionCol;

    private final UserService userService = new UserService();

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getId()).asObject());
        firstNameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFirstName()));
        lastNameCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getLastName()));
        emailCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEmail()));
        entryDateCol.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().getEntryDate()));
        typeCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getType()));

        companyCol.setCellValueFactory(cell -> {
            if (cell.getValue() instanceof Agent a) {
                return new SimpleStringProperty(a.getCompanyName());
            }
            return new SimpleStringProperty("");
        });

        locationCol.setCellValueFactory(cell -> {
            if (cell.getValue() instanceof Agent a) {
                return new SimpleStringProperty(a.getLocation());
            }
            return new SimpleStringProperty("");
        });

        domainCol.setCellValueFactory(cell -> {
            if (cell.getValue() instanceof Tutor t) {
                return new SimpleStringProperty(t.getDomain());
            }
            return new SimpleStringProperty("");
        });

        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button deleteBtn = new Button("🗑 Supprimer");
            private final Button editBtn = new Button("✏ Modifier");

            {
                deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
                editBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");

                deleteBtn.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    try {
                        userService.delete(user.getId());
                        tableView.getItems().remove(user);
                        showSuccessAlert("Utilisateur supprimé.");
                    } catch (SQLException e) {
                        showAlert("Erreur", "Suppression échouée : " + e.getMessage());
                    }
                });

                editBtn.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/EditUser.fxml"));
                        Parent root = loader.load();

                        EditUserController controller = loader.getController();
                        controller.initData(user);

                        Stage stage = new Stage();
                        stage.setTitle("Modifier utilisateur");
                        stage.setScene(new Scene(root));

                        // 🔄 Rafraîchir après fermeture
                        stage.setOnHiding(e -> {
                            try {
                                List<User> updatedUsers = userService.getAll();
                                allUsers.setAll(updatedUsers);  // met à jour les données de la table
                                filtrerParType(); // conserve le filtre actif
                            } catch (SQLException ex) {
                                showAlert("Erreur", "Rafraîchissement échoué : " + ex.getMessage());
                            }
                        });

                        stage.show();
                    } catch (IOException e) {
                        showAlert("Erreur", "Chargement échoué : " + e.getMessage());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10, editBtn, deleteBtn);
                    setGraphic(hbox);
                }
            }
        });

        typeFilter.getItems().addAll("Tous", "student", "agent", "tutor", "admin");
        typeFilter.setValue("Tous");
        typeFilter.setOnAction(e -> filtrerParType());

        try {
            List<User> users = userService.getAll();
            allUsers = FXCollections.observableArrayList(users);
            tableView.setItems(allUsers);
        } catch (SQLException e) {
            showAlert("Erreur", "Chargement échoué : " + e.getMessage());
        }
    }

    @FXML
    private void retourAccueil(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/ranim/projetpidev/Accueil.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Accueil");
            stage.show();
        } catch (IOException e) {
            showAlert("Erreur", "Impossible de retourner à l'accueil.");
        }
    }

    private void showAlert(String titre, String contenu) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titre);
        alert.setContentText(contenu);
        alert.showAndWait();
    }

    @FXML
    private void filtrerParType() {
        String typeChoisi = typeFilter.getValue();
        ajusterColonnesSelonType(typeChoisi);

        if (typeChoisi == null || typeChoisi.equalsIgnoreCase("Tous")) {
            tableView.setItems(allUsers);
        } else {
            ObservableList<User> filtered = allUsers.filtered(
                    u -> u.getType() != null && u.getType().equalsIgnoreCase(typeChoisi));
            tableView.setItems(filtered);
        }
    }

    private void ajusterColonnesSelonType(String type) {
        switch (type.toLowerCase()) {
            case "student" -> {
                companyCol.setVisible(false);
                locationCol.setVisible(false);
                domainCol.setVisible(false);
            }
            case "agent" -> {
                companyCol.setVisible(true);
                locationCol.setVisible(true);
                domainCol.setVisible(false);
            }
            case "tutor" -> {
                companyCol.setVisible(false);
                locationCol.setVisible(false);
                domainCol.setVisible(true);
            }
            case "admin" -> {
                companyCol.setVisible(false);
                locationCol.setVisible(false);
                domainCol.setVisible(false);
            }
            default -> {
                companyCol.setVisible(true);
                locationCol.setVisible(true);
                domainCol.setVisible(true);
            }

        }
    }
    private void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null); // ✅ pas de titre comme "Erreur"
        alert.setContentText(message);
        alert.showAndWait();
    }


}