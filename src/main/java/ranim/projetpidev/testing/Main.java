package ranim.projetpidev.testing;

import ranim.projetpidev.entites.Agent;
import ranim.projetpidev.entites.Student;
import ranim.projetpidev.entites.Tutor;
import ranim.projetpidev.entites.User;
import ranim.projetpidev.services.UserService;
import ranim.projetpidev.tools.MyDataBase;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class Main {
    UserService userService = new UserService();

    public static void main(String[] args) throws SQLException {
        // Connexion à la base de données
        MyDataBase md = MyDataBase.getInstance();
        UserService userService = new UserService();
        Agent agent = new Agent(
                0,
                "Ali",
                "Amari",
                "ali.amari@exemple.com",
                LocalDate.of(2024, 5, 1),
                "agentpass",
                "agent",
                "TechCorp",
                "Tunis"
        );
        userService.add(agent);
        Tutor tutor = new Tutor(
                0,
                "Sara",
                "Ben Ali",
                "sara.tutor@exemple.com",
                LocalDate.of(2024, 4, 10),
                "tutorpass",
                "tutor",
                "Informatique"
        );
        userService.add(tutor);
        Student student = new Student(
                0,
                "Wassim",
                "Trabelsi",
                "wassim.student@exemple.com",
                LocalDate.of(2024, 3, 15),
                "studentpass",
                "student"
        );
        userService.add(student);
        try {
            List<User> users = userService.getAll();

            for (User user : users) {
                System.out.println("👤 " + user.getFirstName() + " " + user.getLastName() + " (" + user.getType() + ")");

                // Affichage des infos spécifiques selon le type
                if (user instanceof Agent) {
                    System.out.println("   🏢 Société : " + agent.getCompanyName());
                    System.out.println("   📍 Localisation : " + agent.getLocation());
                } else if (user instanceof Tutor) {
                    System.out.println("   📚 Domaine : " + tutor.getDomain());
                } else if (user instanceof Student) {
                    System.out.println("   🎓 Type : Étudiant (aucune info spécifique)");
                }

                System.out.println("------------------------------------------------");
            }

        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la récupération des utilisateurs : " + e.getMessage());
        }
    }
    }
