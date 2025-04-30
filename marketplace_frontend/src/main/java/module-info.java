module com.cboard.marketplace.marketplace_frontend {
    requires com.cboard.marketplace.marketplace_common;

    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires javafx.controls;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires org.kordamp.ikonli.fontawesome5;
    requires com.google.gson;
    requires okhttp3;
    requires javafx.web; // Force Ikonli to load FontAwesome Solid icons

    opens com.cboard.marketplace.marketplace_frontend to javafx.fxml;
    opens com.cboard.marketplace.marketplace_frontend.Request to com.google.gson;
    exports com.cboard.marketplace.marketplace_frontend;
}