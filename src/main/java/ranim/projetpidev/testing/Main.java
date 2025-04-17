package ranim.projetpidev.testing;

import ranim.projetpidev.entites.Quiz;
import ranim.projetpidev.entites.Question;
import ranim.projetpidev.services.QuizService;
import ranim.projetpidev.services.QuestionService;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        QuizService quizService = new QuizService();
        QuestionService questionService = new QuestionService();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n========= MENU =========");
            System.out.println("1. Ajouter un quiz");
            System.out.println("2. Afficher tous les quiz");
            System.out.println("3. Modifier un quiz");
            System.out.println("4. Supprimer un quiz");
            System.out.println("5. Ajouter une question");
            System.out.println("6. Afficher toutes les questions");
            System.out.println("7. Modifier une question");
            System.out.println("8. Supprimer une question");
            System.out.println("9. Quitter");
            System.out.print("👉 Choix : ");
            int choix = sc.nextInt();
            sc.nextLine(); // Consommer saut de ligne

            switch (choix) {
                case 1: // ADD QUIZ
                    System.out.print("➕ Titre du quiz : ");
                    String title = sc.nextLine();
                    System.out.print("📝 Description : ");
                    String description = sc.nextLine();
                    System.out.print("📘 Type (ex: Mathématiques, Programmation, etc.) : ");
                    String type = sc.nextLine();

                    Quiz newQuiz = new Quiz(title, description, type); // 👈 constructeur mis à jour
                    quizService.add(newQuiz);

                    break;

                case 2: // DISPLAY QUIZZES
                    System.out.println("📋 Liste des quiz :");
                    for (Quiz q : quizService.getAll()) {
                        System.out.println(q.getId() + " - " + q.getTitle() + " | " + q.getDescription());
                    }
                    break;

                case 3: // UPDATE QUIZ
                    System.out.print("✏️ ID du quiz à modifier : ");
                    int quizIdToUpdate = sc.nextInt();
                    sc.nextLine();
                    Quiz quizToUpdate = quizService.getById(quizIdToUpdate);
                    if (quizToUpdate != null) {
                        System.out.print("🔁 Nouveau titre : ");
                        quizToUpdate.setTitle(sc.nextLine());
                        System.out.print("🔁 Nouvelle description : ");
                        quizToUpdate.setDescription(sc.nextLine());
                        quizService.update(quizToUpdate);
                    } else {
                        System.out.println("❌ Quiz introuvable.");
                    }
                    break;

                case 4: // DELETE QUIZ
                    for (Quiz q : quizService.getAll()) {
                        System.out.println(q.getId() + " - " + q.getTitle());
                    }
                    System.out.print("🗑️ ID du quiz à supprimer : ");
                    int idToDelete = sc.nextInt();
                    sc.nextLine();
                    Quiz quizToDelete = quizService.getById(idToDelete);
                    if (quizToDelete != null) {
                        quizService.delete(quizToDelete); // Suppression en cascade automatique
                    } else {
                        System.out.println("❌ Quiz introuvable.");
                    }
                    break;

                case 5: // ADD QUESTION
                    System.out.print("🔗 ID du quiz associé : ");
                    int quizId = sc.nextInt();
                    sc.nextLine();
                    System.out.print("❓ Texte de la question : ");
                    String questionText = sc.nextLine();
                    System.out.print("A: ");
                    String a = sc.nextLine();
                    System.out.print("B: ");
                    String b = sc.nextLine();
                    System.out.print("C: ");
                    String c = sc.nextLine();
                    System.out.print("✅ Bonne réponse (A/B/C) : ");
                    String correct = sc.nextLine();

                    Question question = new Question(quizId, questionText, a, b, c, correct);
                    questionService.add(question);
                    break;

                case 6: // DISPLAY QUESTIONS
                    System.out.println("📋 Liste des questions :");
                    for (Question qu : questionService.getAll()) {
                        System.out.println("[" + qu.getId() + "] Quiz#" + qu.getQuizId() + " → " + qu.getQuestionText());
                    }
                    break;

                case 7: // UPDATE QUESTION
                    System.out.print("✏️ ID de la question à modifier : ");
                    int qId = sc.nextInt();
                    sc.nextLine();
                    Question qToUpdate = questionService.getById(qId);
                    if (qToUpdate != null) {
                        System.out.print("❓ Nouveau texte : ");
                        qToUpdate.setQuestionText(sc.nextLine());
                        System.out.print("🔁 Option A : ");
                        qToUpdate.setOptionA(sc.nextLine());
                        System.out.print("🔁 Option B : ");
                        qToUpdate.setOptionB(sc.nextLine());
                        System.out.print("🔁 Option C : ");
                        qToUpdate.setOptionC(sc.nextLine());
                        System.out.print("🔁 Bonne réponse : ");
                        qToUpdate.setCorrectAnswer(sc.nextLine());
                        questionService.update(qToUpdate);
                    } else {
                        System.out.println("❌ Question introuvable.");
                    }
                    break;

                case 8: // DELETE QUESTION
                    for (Question q : questionService.getAll()) {
                        System.out.println(q.getId() + " → " + q.getQuestionText());
                    }
                    System.out.print("🗑️ ID de la question à supprimer : ");
                    int qIdDel = sc.nextInt();
                    sc.nextLine();
                    Question qDel = questionService.getById(qIdDel);
                    if (qDel != null) {
                        questionService.delete(qDel);
                    } else {
                        System.out.println("❌ Question introuvable.");
                    }
                    break;

                case 9:
                    System.out.println("👋 Fin du programme.");
                    return;

                default:
                    System.out.println("❌ Choix invalide.");
            }
        }
    }
}
