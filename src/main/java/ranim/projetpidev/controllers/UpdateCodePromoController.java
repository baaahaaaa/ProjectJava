package ranim.projetpidev.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import ranim.projetpidev.entites.Promocode;
import ranim.projetpidev.services.PromocodeServie;

import java.sql.SQLException;

public class UpdateCodePromoController {

    @FXML private TextField codeField;
    @FXML private TextField discountField;
    @FXML private DatePicker expiryDateField;
    @FXML private CheckBox statusField;

    private PromocodeServie promocodeServie = new PromocodeServie();
    private Promocode promocode;

    // Méthode pour initialiser le contrôleur avec les données du code promo
    public void initData(Promocode promocode) {
        this.promocode = promocode;

        // Pré-remplir les champs avec les données existantes
        codeField.setText(promocode.getCode());
        discountField.setText(String.valueOf(promocode.getDiscountValue()));
        expiryDateField.setValue(promocode.getExpiryDate());
        statusField.setSelected(promocode.isActive());
    }

    // Méthode pour enregistrer les modifications du code promo
    @FXML
    private void savePromo() {
        try {
            promocode.setCode(codeField.getText());
            promocode.setDiscountValue(Double.parseDouble(discountField.getText()));
            promocode.setExpiryDate(expiryDateField.getValue());
            promocode.setActive(statusField.isSelected());

            promocodeServie.update(promocode);

            // ✅ Appel du listener
            if (listener != null) {
                listener.onPromoUpdated(promocode);
            }

            showAlert("Succès", "Code promo mis à jour.");
            closeWindow();
        } catch (SQLException e) {
            showAlert("Erreur", "Impossible de mettre à jour le code promo : " + e.getMessage());
        } catch (NumberFormatException e) {
            showAlert("Erreur", "Le champ réduction doit être un nombre valide.");
        }
    }


    // Méthode pour fermer la fenêtre de mise à jour
    @FXML
    private void closeWindow() {
        // Fermer la fenêtre lorsque le bouton "Fermer" est cliqué
        codeField.getScene().getWindow().hide();
    }

    // Affichage d'un message d'alerte
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public interface PromoRefreshListener {
        void onPromoUpdated(Promocode promo);

    }
    private UpdateCodePromoController.PromoRefreshListener listener;

    public void setListener(UpdateCodePromoController.PromoRefreshListener listener) {
        this.listener = listener;
    }

    // Setter pour le contrôleur principal

}
