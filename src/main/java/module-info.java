module com.example.tvzprojekt {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires jbcrypt;
    requires org.apache.httpcomponents.client5.httpclient5;
    requires org.apache.httpcomponents.core5.httpcore5;
    requires json.simple;
    requires org.slf4j;

    opens com.example.tvzprojekt to javafx.fxml;
    opens com.example.tvzprojekt.Model to javafx.base;
    exports com.example.tvzprojekt;
    exports com.example.tvzprojekt.Controllers;
    exports com.example.tvzprojekt.Model;
}