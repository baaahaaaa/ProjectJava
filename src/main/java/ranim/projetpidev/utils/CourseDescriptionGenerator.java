package ranim.projetpidev.utils;

import java.util.HashMap;
import java.util.Map;

public class CourseDescriptionGenerator {
    private static final Map<String, String> TEMPLATES = new HashMap<>();
    
    static {
        TEMPLATES.put("IT", """
            Ce cours de %s offre une formation complète et pratique pour les apprenants.
            
            Objectifs d'apprentissage :
            - Maîtriser les concepts fondamentaux de %s
            - Développer des compétences pratiques à travers des projets concrets
            - Comprendre les meilleures pratiques et les standards de l'industrie
            
            Points clés du cours :
            - Introduction aux concepts de base
            - Exercices pratiques et projets réels
            - Techniques avancées et optimisation
            - Méthodologies modernes de développement
            - Tests et débogage
            - Bonnes pratiques et patterns de conception
            """);
            
        TEMPLATES.put("Marketing", """
            Ce cours de %s propose une approche moderne du marketing digital.
            
            Objectifs d'apprentissage :
            - Comprendre les stratégies de %s
            - Développer une présence marketing efficace
            - Maîtriser les outils et techniques actuels
            
            Points clés du cours :
            - Fondamentaux du marketing digital
            - Stratégies de contenu et de communication
            - Analyse des données et métriques
            - Optimisation des campagnes
            - Études de cas réels
            - Tendances actuelles du marché
            """);
            
        TEMPLATES.put("Management", """
            Formation approfondie en %s pour développer vos compétences en leadership.
            
            Objectifs d'apprentissage :
            - Acquérir les compétences essentielles en %s
            - Développer un style de leadership efficace
            - Maîtriser les techniques de gestion moderne
            
            Points clés du cours :
            - Principes fondamentaux du management
            - Gestion d'équipe et leadership
            - Communication efficace
            - Gestion de projet
            - Résolution de conflits
            - Développement organisationnel
            """);
            
        // Template par défaut pour les autres domaines
        TEMPLATES.put("DEFAULT", """
            Formation complète en %s pour développer votre expertise professionnelle.
            
            Objectifs d'apprentissage :
            - Maîtriser les fondamentaux de %s
            - Développer des compétences pratiques
            - Acquérir une expertise professionnelle
            
            Points clés du cours :
            - Introduction aux concepts essentiels
            - Applications pratiques
            - Études de cas
            - Exercices et projets
            - Évaluation des compétences
            - Certification professionnelle
            """);
    }
    
    public static String generateDescription(String title, String domain) {
        String template = TEMPLATES.getOrDefault(domain, TEMPLATES.get("DEFAULT"));
        return String.format(template, title, title);
    }
} 