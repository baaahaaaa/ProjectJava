package ranim.projetpidev.controllers;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import ranim.projetpidev.entites.User;
import ranim.projetpidev.services.UserService;

public class ActivateAccountController {

    @FXML
    private TextField digit1, digit2, digit3, digit4, digit5, digit6;

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

    private void displayMaskedEmail() {
        if (user != null && user.getEmail() != null && !user.getEmail().isEmpty()) {
            String userEmail = user.getEmail();  // Utiliser l'email de l'utilisateur
            String firstTwoChars = userEmail.substring(0, 2);
            String maskedEmail = firstTwoChars + "***@" + userEmail.split("@")[1];
            emailLabel.setText(maskedEmail);  // Affichage de l'email masqué
        }
    }
    @FXML
    public void handleActivateAccount() {
        // Récupérer les valeurs des TextFields et les concaténer pour former le code complet
        String code = digit1.getText() + digit2.getText() + digit3.getText() +
                digit4.getText() + digit5.getText() + digit6.getText();

        boolean isActivated = userService.activateAccount(code);

        if (isActivated) {
            messageLabel.setText("Votre compte a été activé avec succès.");
        } else {
            messageLabel.setText("Code d'activation invalide ou expiré.");
        }
    }
}
