package ranim.projetpidev.services;

import org.bouncycastle.cms.RecipientId;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

import static org.bouncycastle.cms.RecipientId.*;

public class MailService {

    public static void sendCode(String to, String code) {
        final String from = "tasnimaraar01@gmail.com"; // À remplacer
        final String password = "vkpvsgmnucyrfuve"; // À remplacer

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, password);
                    }
                });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject("Your Reset Code");
            message.setText("Here is your code: " + code);

            Transport.send(message);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    public void sendEmail(String toEmail, String subject, String body) {
        final String from = "tasnimaraar01@gmail.com"; // À remplacer
        final String password = "vkpvsgmnucyrfuve";
        // Définir les propriétés SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587"); // Activer STARTTLS pour une connexion sécurisée

        // Créer une session avec l'authentification
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);  // Authentifie avec l'email et mot de passe
            }
        });

        try {
            // Créer le message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(toEmail));  // L'email de l'expéditeur
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));  // L'email du destinataire
            message.setSubject(subject);  // Le sujet de l'email
            message.setText(body);  // Le corps de l'email

            // Envoyer l'email
            Transport.send(message);
            System.out.println("E-mail envoyé avec succès.");

        } catch (MessagingException e) {
            e.printStackTrace();  // Si une erreur survient, affiche l'erreur
        }
    }


    public void sendActivationEmail(String toEmail, String activationCode) {
        String subject = "Activate Your Account";  // Sujet de l'email
        String body = "Hello,\n\n" +
                "Please use the following code to activate your account:\n\n" +
                activationCode + "\n\n" +
                "Best regards,\nYour Team";  // Corps du message avec le code d'activation

        sendEmail(toEmail, subject, body);  // Appeler la méthode d'envoi d'email avec ces paramètres
    }
}
  //"tasnimaraar01@gmail.com";  // Votre adresse e-mail
//"vkpvsgmnucyrfuve";