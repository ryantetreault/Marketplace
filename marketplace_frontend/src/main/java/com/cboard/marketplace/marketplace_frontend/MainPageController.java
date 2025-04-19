package com.cboard.marketplace.marketplace_frontend;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;


import java.io.File;
import java.io.IOException;

public class MainPageController {
    @FXML
    public ImageView productImage;
    @FXML
    public TextArea productTextArea;
    @FXML
    ScrollPane scrollPane;
    @FXML
    FontIcon profileBtn;
    
    public void populate(javafx.event.ActionEvent actionEvent) throws IOException {
        //FileChooser fileChooser = new FileChooser();
        //Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        //File file = fileChooser.showOpenDialog(stage);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("productCardSimple.fxml"));
        Parent FXML = fxmlLoader.load();
        scrollPane.setContent(FXML);
        MainPageController mainPageController = fxmlLoader.getController();

    }
    public void profileClicked(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SignUpController.class.getResource("userProfile.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        userProfileController userProfileController = fxmlLoader.getController();

        stage.setScene(scene);

        stage.show();
    }

    public void openProductCard(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SignUpController.class.getResource("productCard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();


    }
}