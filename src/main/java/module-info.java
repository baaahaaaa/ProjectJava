module ranim.projetpidev {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.sql;

    // Assurez-vous que le paquet contenant votre contrôleur est accessible par JavaFX

    opens ranim.projetpidev to javafx.fxml;
    opens ranim.projetpidev.controllers to javafx.fxml;
    opens ranim.projetpidev.entites to javafx.base;


    exports ranim.projetpidev;
    exports ranim.projetpidev.tools;
    exports ranim.projetpidev.controllers;
}
