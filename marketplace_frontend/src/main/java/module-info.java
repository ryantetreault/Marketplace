module com.cboard.marketplace.marketplace_frontend {
    requires com.cboard.marketplace.marketplace_common;

    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires javafx.controls;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    // requires org.kordamp.ikonli.fontawesome5; // Force Ikonli to load FontAwesome Solid icons

    requires com.google.gson;
    requires okhttp3;

    opens com.cboard.marketplace.marketplace_frontend to javafx.fxml;
    exports com.cboard.marketplace.marketplace_frontend;
}