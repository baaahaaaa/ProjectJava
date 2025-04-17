module ranim.projetpidev {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.base; // Assurez-vous d'ajouter javafx.base si nécessaire

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.j;
    requires jdk.jfr;

    opens ranim.projetpidev to javafx.fxml;
    exports ranim.projetpidev;

    opens ranim.projetpidev.controllers to javafx.fxml;
    exports ranim.projetpidev.controllers;

    opens ranim.projetpidev.entites to javafx.base, javafx.fxml;
    exports ranim.projetpidev.entites;

    exports ranim.projetpidev.tools;
    exports ranim.projetpidev.controllers.candidat;
    opens ranim.projetpidev.controllers.candidat to javafx.fxml;
    exports ranim.projetpidev.controllers.internship;
    opens ranim.projetpidev.controllers.internship to javafx.fxml;
    exports ranim.projetpidev.controllers.feedback;
    opens ranim.projetpidev.controllers.feedback to javafx.fxml;
}
