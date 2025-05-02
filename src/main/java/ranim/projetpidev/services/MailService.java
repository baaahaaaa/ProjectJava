package ranim.projetpidev.services;


import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

import static javax.mail.Transport.send;


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
            message.setSubject("Your Password Reset Code");

            // HTML message body with CSS and explanation
            String htmlContent = "<html>" +
                    "<head>" +
                    "<style>" +
                    "body { font-family: Arial, sans-serif; background-color: #f9f9f9; padding: 20px; }" +
                    "h1 { color: #333366; }" +
                    "p { font-size: 14px; color: #555555; }" +
                    ".code-box { background-color: #f1f1f1; padding: 10px; margin: 10px 0; font-size: 18px; font-weight: bold; }" +
                    ".footer { font-size: 12px; color: #888888; margin-top: 20px; }" +
                    "</style>" +
                    "</head>" +
                    "<body>" +
                    "<h1>Password Reset Request</h1>" +
                    "<p>Hello,</p>" +
                    "<p>You requested a password reset for your account. To proceed, please use the following code:</p>" +
                    "<div class='code-box'>" + code + "</div>" +
                    "<p>Simply enter this code in the password reset form on our website to reset your password.</p>" +
                    "<p>If you did not request a password reset, please ignore this email.</p>" +
                    "<p>If you have any questions, feel free to contact our support team.</p>" +
                    "<div class='footer'>" +
                    "<p>Thank you for using our service.</p>" +
                    "<p>Best regards,<br>Your Support Team</p>" +
                    "</div>" +
                    "</body>" +
                    "</html>";

            // Set the HTML content to the message
            message.setContent(htmlContent, "text/html");

            // Send the message
            send(message);

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
            send(message);
            System.out.println("E-mail envoyé avec succès.");

        } catch (MessagingException e) {
            e.printStackTrace();  // Si une erreur survient, affiche l'erreur
        }
    }


    public void sendActivationEmail(String toEmail, String activationCode) {
        String subject = "Activate Your Account";

        // Créer le corps du message en HTML avec du CSS
        String body = "<html>"
                + "<head>"
                + "<style>"
                + "body { font-family: Arial, sans-serif; background-color: #f0f4f8; color: #333; padding: 20px; }"
                + ".container { width: 100%; max-width: 600px; margin: auto; background-color: #ffffff; padding: 20px; border-radius: 8px; box-shadow: 0 4px 8px rgba(0,0,0,0.1); }"
                + ".header { background-color: #4CAF50; color: white; padding: 10px 20px; text-align: center; border-radius: 8px 8px 0 0; }"
                + ".content { padding: 20px; text-align: center; }"
                + ".button { background-color: #4CAF50; color: white; padding: 10px 20px; font-size: 16px; text-decoration: none; border-radius: 5px; display: inline-block; margin-top: 20px; }"
                + ".footer { text-align: center; color: #888; font-size: 12px; margin-top: 40px; }"
                + "</style>"
                + "</head>"
                + "<body>"
                + "<div class='container'>"
                + "<div class='header'><h1>Account Activation</h1></div>"
                + "<div class='content'>"
                + "<p>Hello,</p>"
                + "<p>Thank you for signing up! To activate your account, please use the following activation code:</p>"
                + "<h2>" + activationCode + "</h2>"
                + "<a href='#' class='button'>Activate Account</a>"
                + "</div>"
                + "<div class='footer'>"
                + "<p>Best regards,</p>"
                + "<p>Your Team</p>"
                + "</div>"
                + "</div>"
                + "</body>"
                + "</html>";

        MailService mailService = new MailService();
        mailService.sendEmailWithHtml(toEmail, subject, body);
    }
    public void sendEmailWithHtml(String toEmail, String subject, String body) {
        String fromEmail = "tasnimaraar01@gmail.com";  // Ton email
        String password = "vkpvsgmnucyrfuve";  // Ton mot de passe ou un mot de passe d'application

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            // Créer un message avec un contenu HTML
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(body, "text/html");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            message.setContent(multipart);
            send(message);
            System.out.println("Email envoyé avec succès.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
  //"tasnimaraar01@gmail.com";  // Votre adresse e-mail
//"vkpvsgmnucyrfuve";