package com.esprit.views;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainLayout extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger le fichier FXML
        Parent root = FXMLLoader.load(getClass().getResource("/com/esprit/views/MainLayout.fxml"));
        
        // Créer la scène
        Scene scene = new Scene(root);
        
        // Configurer la fenêtre
        primaryStage.setTitle("Votre Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
} 