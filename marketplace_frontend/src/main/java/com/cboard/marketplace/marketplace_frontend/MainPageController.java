package com.cboard.marketplace.marketplace_frontend;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;

import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;
import static com.cboard.marketplace.marketplace_frontend.Utility.StageUtility.STAGE_UTILITY;
import static com.cboard.marketplace.marketplace_frontend.Utility.TooltipUtility.TOOLTIP_UTILITY;



import java.io.File;
import java.io.IOException;
import java.util.Map;

public class MainPageController {
    @FXML
    public ImageView productImage;
    @FXML
    public TextArea productTextArea;
    @FXML
    ScrollPane scrollPane;
    @FXML
    FontIcon profileBtn;
    @FXML
    FontIcon transactionBtn;
    @FXML
    FontIcon helpBtn;
    @FXML
    FontIcon backBtn;
    @FXML
    FontIcon newListingBtn;


    public void initialize()
    {

        TOOLTIP_UTILITY.attachTooltips(
                TOOLTIP_UTILITY.safeMap(
                            newListingBtn, "List New",
                            profileBtn, "Profile",
                            transactionBtn, "Transactions",
                            helpBtn, "Help",
                            backBtn, "Sign Out"
                        )
                );
    }
    
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

        STAGE_UTILITY.switchStage(stage);
    }

    public void newProfileClicked(MouseEvent event) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("newUserProfile.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        userProfileController userProfileController = fxmlLoader.getController();

        stage.setScene(scene);

        STAGE_UTILITY.switchStage(stage);
    }

    public void openProductCard(MouseEvent mouseEvent) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(SignUpController.class.getResource("productCard.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();


    }

    public void handleBack(MouseEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            stage.show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void transactionsClicked(MouseEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("transactionPage.fxml"));
            Parent root = loader.load();

            TransactionPageController controller = loader.getController();
            //controller.someFuncToPassDataToNextSceneHere();
            //controller.setPage();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            STAGE_UTILITY.switchStage(stage);
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    public void openHelpPopup() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HelpPopup.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    @FXML
    public void handleNewListing(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("listNew.fxml"));
        Parent root = loader.load();

        ListNewController controller = loader.getController();
        controller.setUserId(SessionManager.getUserIdFromToken());

        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }
}