package com.cboard.marketplace.marketplace_frontend;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {
    @FXML
    private Label welcomeText;
    @FXML
    private VBox vbox;
    @FXML
    private Pane pane;



    @Override
    public void initialize(URL url, ResourceBundle rb) {



            try {
                Parent fxml = FXMLLoader.load(getClass().getResource("signUp.fxml"));

                vbox.getChildren().setAll(fxml);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }

    }

    @FXML
    public void openSignUp(javafx.event.ActionEvent event) {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vbox);
        t.setToX(0);

        t.setOnFinished((e) -> {
            try {
                Parent fxml = FXMLLoader.load(getClass().getResource("signUp.fxml"));
                vbox.getChildren().removeAll();
                vbox.getChildren().setAll(fxml);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        t.play();

    }
    @FXML
    public void openSignIn(javafx.event.ActionEvent event) {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vbox);
        t.setToX(pane.getWidth() - vbox.getWidth() - 50);

        t.setOnFinished((e) -> {
            try {
                Parent fxml = FXMLLoader.load(getClass().getResource("signIn.fxml"));
                vbox.getChildren().removeAll();
                vbox.getChildren().setAll(fxml);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        t.play();

    }


}


