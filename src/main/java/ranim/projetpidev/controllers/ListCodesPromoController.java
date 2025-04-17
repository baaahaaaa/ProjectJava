package ranim.projetpidev.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import ranim.projetpidev.entites.Promocode;
import ranim.projetpidev.entites.Student;
import ranim.projetpidev.services.PromocodeServie;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListCodesPromoController {

    @FXML private TableView<Promocode> promoTableView;
    @FXML private TableColumn<Promocode, String> codeCol;
    @FXML private TableColumn<Promocode, Double> discountCol;
    @FXML private TableColumn<Promocode, String> expiryCol;
    @FXML private TableColumn<Promocode, String> statusCol;
    @FXML private TableColumn<Promocode, Void> actionCol;  // Colonne Action avec les boutons
    @FXML private Button closeButton;

    private PromocodeServie promocodeServie = new PromocodeServie();

    // Méthode pour afficher les codes promo d'un étudiant
    public void afficherCodesPromo(Student student) {
        try {
            // Récupérer les codes promo pour cet étudiant à partir du service
            List<Promocode> promocodes = promocodeServie.getAllPromocodesForStudent(student.getId());

            // Si aucun code promo, afficher un message
            if (promocodes.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Pas de codes promo");
                alert.setContentText("Aucun code promo trouvé pour cet étudiant.");
                alert.showAndWait();
            }

            // Remplir la table avec les codes promo
            ObservableList<Promocode> promoList = FXCollections.observableArrayList(promocodes);
            promoTableView.setItems(promoList);

            // Initialiser les colonnes de la TableView
            codeCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getCode()));
            discountCol.setCellValueFactory(cell -> new SimpleDoubleProperty(cell.getValue().getDiscountValue()).asObject());
            expiryCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getExpiryDate().toString()));
            statusCol.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().isActive() ? "Actif" : "Inactif"));

            // Ajouter des boutons dans la colonne Action (Modifier, Supprimer)
            actionCol.setCellFactory(col -> new TableCell<Promocode, Void>() {
                private final Button editBtn = new Button("Modifier");
                private final Button deleteBtn = new Button("Supprimer");
                private final Button ajouterBtn = new Button("Ajouter");

                {
                    // Action de suppression
                    deleteBtn.setOnAction(event -> {
                        Promocode promocode = getTableView().getItems().get(getIndex());
                        try {
                            promocodeServie.delete(promocode.getId());  // Supprimer le code promo de la base de données
                            promoTableView.getItems().remove(promocode);
                            showAlert("Succès", "Code promo supprimé.");
                        } catch (SQLException e) {
                            showAlert("Erreur", "Suppression échouée : " + e.getMessage());
                        }
                    });

                    // Action de modification
                    editBtn.setOnAction(event -> {
                        Promocode promocode = getTableView().getItems().get(getIndex());

                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/UpdateCodePromo.fxml"));
                            Parent root = loader.load();

                            UpdateCodePromoController controller = loader.getController();
                            controller.initData(promocode);

                            // 🔁 Connexion du callback
                            controller.setListener(new UpdateCodePromoController.PromoRefreshListener() {
                                @Override
                                public void onPromoUpdated(Promocode updatedPromo) {
                                    getTableView().getItems().set(getIndex(), updatedPromo);
                                    getTableView().refresh();
                                }
                            });

                            Stage stage = new Stage();
                            stage.setTitle("Modifier Code Promo");
                            stage.setScene(new Scene(root));
                            stage.show();

                        } catch (IOException e) {
                            showAlert("Erreur", "Impossible de charger la fenêtre de mise à jour.");
                        }
                    });
                    ajouterBtn.setOnAction(event -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ranim/projetpidev/ajoutercodepromo.fxml"));
                            Parent root = loader.load();

                            AjouterCodePromoController controller = loader.getController();

                            // 💡 Tu définis le listener ici
                            controller.setListener(new AjouterCodePromoController.PromoRefreshListener() {
                                @Override
                                public void onPromoAdded(Promocode promo) {
                                    // Ajoute l'élément dans la table
                                    promoTableView.getItems().add(promo);
                                }
                            });

                            Stage stage = new Stage();
                            stage.setTitle("Ajouter Code Promo");
                            stage.setScene(new Scene(root));
                            stage.show();
                        } catch (IOException e) {
                            showAlert("Erreur", "Impossible de charger la fenêtre d'ajout.");
                        }
                    });

                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        HBox hbox = new HBox(10, editBtn, deleteBtn,ajouterBtn);
                        setGraphic(hbox);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Impossible de récupérer les codes promo.");
        }
    }

    // Méthode pour fermer la fenêtre
    @FXML
    private void closeWindow() {
        closeButton.getScene().getWindow().hide();
    }

    // Affichage d'un message d'erreur
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
