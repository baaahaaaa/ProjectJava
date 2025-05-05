package ranim.projetpidev.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import ranim.projetpidev.entites.Promocode;
import ranim.projetpidev.services.PromocodeServie;

import java.sql.SQLException;

public class AjouterCodePromoController {

    @FXML private TextField codeField;
    @FXML private TextField discountValueField;
    @FXML private DatePicker expiryDateField;
    @FXML private CheckBox activeCheckBox;
    @FXML private Button addButton;
    @FXML private Label statusLabel;

    private PromocodeServie promocodeService = new PromocodeServie();

    // Méthode pour ajouter un code promo
    @FXML
    public void addPromocode() {
        String code = codeField.getText();
        String discountText = discountValueField.getText();

        // Vérifier que la réduction est un nombre valide
        double discountValue = 0;
        try {
            discountValue = Double.parseDouble(discountText);
        } catch (NumberFormatException e) {
            if (statusLabel != null) {
                statusLabel.setText("Veuillez entrer une valeur numérique valide pour la réduction.");
            }
            return;
        }

        java.time.LocalDate expiryDate = expiryDateField.getValue();
        boolean active = activeCheckBox.isSelected();

        Promocode promocode = new Promocode(0, code, discountValue, expiryDate, active, 0); // ID auto-généré

        try {
            promocodeService.add(promocode);

            // 🔁 Notifier le contrôleur principal pour mettre à jour la TableView
            if (listener != null) {
                listener.onPromoAdded(promocode);
            }

            if (statusLabel != null) {
                statusLabel.setText("Code promo ajouté avec succès !");
            }

            // Optionnel : Fermer la fenêtre après ajout
            // ((Stage) addButton.getScene().getWindow()).close();

        } catch (SQLException e) {
            if (statusLabel != null) {
                statusLabel.setText("Erreur lors de l'ajout du code promo.");
            }
        }
    }

    public interface PromoRefreshListener {
        void onPromoAdded(Promocode promo);

    }
    private PromoRefreshListener listener;

    public void setListener(PromoRefreshListener listener) {
        this.listener = listener;
    }
}
