package ranim.projetpidev.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ranim.projetpidev.entites.Event;
import ranim.projetpidev.entites.User;
import ranim.projetpidev.services.UserService;

public class ActivateAccountController {

    @FXML
    private TextField activationCodeField;

    @FXML
    private Label messageLabel;
    @FXML
    private Label emailLabel; // Le label qui affichera l'email partiellement masqué

    private String userEmail = "exampleemail@example.com";

    private UserService userService = new UserService();
    private User user;
    public void initData(User user) {
        this.user = user;
        displayMaskedEmail();  // Met à jour le label avec l'email masqué
    }

    // Afficher l'email masqué (les deux premiers caractères de l'email uniquement)
    private void displayMaskedEmail() {
        if (userEmail != null && !userEmail.isEmpty()) {
            // Extraire les deux premiers caractères de l'email avant le symbole @
            String firstTwoChars = userEmail.substring(0, 2);  // On récupère les deux premiers caractères de l'email
            String maskedEmail = firstTwoChars + "***@" + userEmail.split("@")[1];  // Masquer l'email après les deux premiers caractères
            emailLabel.setText(maskedEmail);  // Mettre à jour le label avec l'email masqué
        }
    }

    @FXML
    public void handleActivateAccount() {
        String activationCode = activationCodeField.getText().trim();

        boolean isActivated = userService.activateAccount(activationCode);

        if (isActivated) {
            messageLabel.setText("Votre compte a été activé avec succès.");
        } else {
            messageLabel.setText("Code d'activation invalide ou expiré.");
        }
    }
}
