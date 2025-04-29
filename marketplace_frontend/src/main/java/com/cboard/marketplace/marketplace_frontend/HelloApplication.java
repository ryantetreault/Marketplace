package com.cboard.marketplace.marketplace_frontend;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

// import org.kordamp.ikonli.fontawesome5.FontAwesomeSolidIkonHandler;

public class HelloApplication extends Application
{

    @Override
    public void start(Stage stage) throws IOException {
        // Force Ikonli to load FontAwesome Solid icons
        // new FontAwesomeSolidIkonHandler().resolve("fas-user");

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.setFill(Color.TRANSPARENT);

        stage.setScene(scene);
        //stage.initStyle(StageStyle.TRANSPARENT);
        stage.show();
    }

    public static void main(String[] args)
    {
        launch();
    }
}