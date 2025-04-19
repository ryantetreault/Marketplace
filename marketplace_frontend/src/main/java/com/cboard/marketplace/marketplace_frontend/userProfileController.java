package com.cboard.marketplace.marketplace_frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class userProfileController {

    public void closeProfile(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SignUpController.class.getResource("mainPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.setFill(Color.TRANSPARENT);
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        MainPageController mainPageController = fxmlLoader.getController();
        mainPageController.populate(actionEvent);
        stage.setScene(scene);

        stage.show();
    }

    public void listNew(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SignUpController.class.getResource("listNew.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();


        stage.setScene(scene);

        stage.show();
    }
}
