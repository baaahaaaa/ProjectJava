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
    requires java.desktop;

    opens ranim.projetpidev.entites to javafx.base;
    opens ranim.projetpidev to javafx.fxml;
    exports ranim.projetpidev;

    opens ranim.projetpidev.controllers to javafx.fxml;
    exports ranim.projetpidev.controllers;

    exports ranim.projetpidev.tools;

    opens com.esprit.views to javafx.fxml;
    exports com.esprit.views;
}