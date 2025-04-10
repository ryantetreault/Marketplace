module com.cboard.marketplace.marketplace_frontend {
    requires com.cboard.marketplace.marketplace_common;

    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.cboard.marketplace.marketplace_frontend to javafx.fxml;
    exports com.cboard.marketplace.marketplace_frontend;
}