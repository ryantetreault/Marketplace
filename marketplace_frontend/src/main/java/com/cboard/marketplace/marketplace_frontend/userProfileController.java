package com.cboard.marketplace.marketplace_frontend;

import com.cboard.marketplace.marketplace_common.ItemDto;
import com.cboard.marketplace.marketplace_common.TransactionDto;
import com.cboard.marketplace.marketplace_common.dto.UserDto;
import com.cboard.marketplace.marketplace_frontend.Utility.HttpUtility;
import com.cboard.marketplace.marketplace_frontend.Utility.ItemRenderer;
import com.cboard.marketplace.marketplace_frontend.Utility.TransactionRenderer;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;
import org.kordamp.ikonli.javafx.FontIcon;

import java.net.URL;
import java.util.*;

import static com.cboard.marketplace.marketplace_frontend.Utility.AlertUtility.ALERT_UTILITY;
import static com.cboard.marketplace.marketplace_frontend.Utility.HttpUtility.HTTP_UTILITY;
import static com.cboard.marketplace.marketplace_frontend.Utility.StageUtility.STAGE_UTILITY;
import static com.cboard.marketplace.marketplace_frontend.Utility.TooltipUtility.TOOLTIP_UTILITY;

public class userProfileController implements Initializable
{
    private List<TransactionDto> transactions = new ArrayList<>();
    private List<ItemDto> items = new ArrayList<>();
    private UserDto user;
    @FXML
    private Label nameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label ratingLabel;
    @FXML
    ScrollPane objContainer;
    @FXML
    VBox objListVBox;
    @FXML
    TextField searchField;
    @FXML
    Label containerLabel;
    @FXML
    FontIcon profileBtn;
    @FXML
    FontIcon transactionBtn;
    @FXML
    FontIcon helpBtn;
    @FXML
    FontIcon backBtn;
    @FXML
    FontIcon curListingsBtn;
    @FXML
    FontIcon boughtBtn;
    @FXML
    FontIcon soldBtn;
    @FXML
    FontIcon deleteBtn;
    @FXML
    FontIcon newListingBtn;


    public void listNew(MouseEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("listNew.fxml"));
            Parent root = loader.load();

            ListNewController controller = loader.getController();

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

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        TOOLTIP_UTILITY.attachTooltips(Map.of(
                profileBtn, "Profile",
                transactionBtn, "Transactions",
                helpBtn, "Help",
                backBtn, "Home",
                curListingsBtn, "Current Listings",
                boughtBtn, "Items Bought",
                soldBtn, "Items Sold",
                deleteBtn, "Delete Account",
                newListingBtn, "List New"

        ));
        loadUserProfile();

        objListVBox = new VBox(10);
        objListVBox.setPadding(new Insets(10));
        objContainer.setContent(objListVBox);

        nameLabel.setText(user.getFirstName() + " " + user.getLastName());
        emailLabel.setText("Email: " + user.getEmail());
        usernameLabel.setText("Username: " + user.getUsername());
        System.out.println(user.getAverageRating());
        ratingLabel.setText("Average Rating: " +
                (user.getAverageRating() != null ? String.format("%.2f", user.getAverageRating()) : "No ratings yet")
        );

        curListingsClick();

    }

    public void curListingsClick()
    {
        containerLabel.setText("Current Listings");

        getItems();


        try
        {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                List<ItemDto> filteredItems = ItemRenderer.filterItemsBySearch(items, newValue);
                objListVBox.getChildren().clear(); // Clear old results
                ItemRenderer.displayItems(objListVBox, objContainer, items, filteredItems); // Show new results
            });
        }
        catch(Exception e)
        {
            System.out.println("Keyboard error");
        }

        ItemRenderer.displayItems(objListVBox, objContainer, items, items); // Show new results

    }

    public void boughtClick()
    {
        containerLabel.setText("Items Purchased");

        getTransactions(0);

        try
        {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                List<TransactionDto> filteredTransactions = TransactionRenderer.filterTransactionsBySearch(transactions, newValue);
                objListVBox.getChildren().clear(); // Clear old results
                TransactionRenderer.displayTransactions(objListVBox, objContainer, transactions, filteredTransactions);
            });
        }
        catch(Exception e)
        {
            System.out.println("Keyboard error");
        }

        TransactionRenderer.displayTransactions(objListVBox, objContainer, transactions, transactions);

    }

    public void soldClick()
    {
        containerLabel.setText("Items Sold");

        getTransactions(1);

        try
        {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                List<TransactionDto> filteredTransactions = TransactionRenderer.filterTransactionsBySearch(transactions, newValue);
                objListVBox.getChildren().clear(); // Clear old results
                TransactionRenderer.displayTransactions(objListVBox, objContainer, transactions, filteredTransactions);
            });
        }
        catch(Exception e)
        {
            System.out.println("Keyboard error");
        }

        TransactionRenderer.displayTransactions(objListVBox, objContainer, transactions, transactions);

    }




    private void getTransactions(int option)
    {
        String choice = "";
        if(option == 0)
            choice = "buyer";
        if(option == 1)
            choice = "seller";


        Request request = new Request.Builder()
                .url("http://localhost:8080/transaction/" + choice + "/" + user.getUserId() + "/all")
                .get()
                .addHeader("Authorization", "Bearer " + SessionManager.getToken())
                .build();

        try(Response response = HTTP_UTILITY.getClient().newCall(request).execute())
        {
            if(response.isSuccessful() && response.body() != null)
            {
                String responseBody = response.body().string();
                transactions = HTTP_UTILITY.getObjMapper().readValue(responseBody, new TypeReference<List<TransactionDto>>() {} ) ;
            }
            else
            {
                System.out.println("Server" + response.code());
                ALERT_UTILITY.showAlert("ERROR", "Error loading transactions...");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }


    private void getItems()
    {
        Request request = new Request.Builder()
                .url("http://localhost:8080/item/" + user.getUserId() + "/owner")
                .get()
                .addHeader("Authorization", "Bearer " + SessionManager.getToken())
                .build();

        try(Response response = HTTP_UTILITY.getClient().newCall(request).execute())
        {
            if(response.isSuccessful() && response.body() != null)
            {
                String responseBody = response.body().string();
                items = HTTP_UTILITY.getObjMapper().readValue(responseBody, new TypeReference<List<ItemDto>>() {} ) ;
            }
            else
            {
                System.out.println("Server" + response.code());
                ALERT_UTILITY.showAlert("ERROR", "Error loading items...");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    public void deleteClick(MouseEvent event)
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setHeaderText("Are you sure you want to delete your account?");
        alert.setContentText("This action cannot be undone.");

        ButtonType confirmButton = new ButtonType("Delete");
        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(confirmButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == confirmButton)
        {
            deleteUser();

            try
            {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
                Scene scene = new Scene(fxmlLoader.load());

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();


                stage.setScene(scene);

                stage.show();
            }
            catch(Exception e)
            {
                e.printStackTrace();
                ALERT_UTILITY.showAlert("ERROR", "Error loading transactions page...");
            }
        }

    }

    public void deleteUser()
    {
        Request request = new Request.Builder()
                .url("http://localhost:8080/user/" + user.getUserId() + "/delete")
                .delete()
                .addHeader("Authorization", "Bearer " + SessionManager.getToken())
                .build();

        try(Response response = HTTP_UTILITY.getClient().newCall(request).execute())
        {
            if(response.isSuccessful() && response.body() != null)
            {
                String responseBody = response.body().string();
                ALERT_UTILITY.showAlert("", responseBody);
            }
            else
            {
                String responseBody = response.body().string();
                System.out.println("Server" + response.code());
                ALERT_UTILITY.showAlert("ERROR", responseBody);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ALERT_UTILITY.showAlert("ERROR", "Internal error...");
        }
    }


    private void loadUserProfile() {
        try {
            Request request = new Request.Builder()
                    .url("http://localhost:8080/user/api/profile")
                    .get()
                    .addHeader("Authorization", "Bearer " + SessionManager.getToken())
                    .build();

            Response response = HttpUtility.HTTP_UTILITY.getClient().newCall(request).execute();
            String responseBody = response.body().string();
            int responseCode = response.code();

            if (response.isSuccessful()) {
                user = HttpUtility.HTTP_UTILITY.getGson().fromJson(responseBody, UserDto.class);


//                if (user != null && user.getFirstName() != null && user.getLastName() != null) {
//                    System.out.println("DEBUG: Parsed user = " + user.getFirstName() + " " + user.getLastName());
//                    nameLabel.setText(user.getFirstName() + " " + user.getLastName());
//                } else {
//                    System.out.println("DEBUG: User parsed is null or missing names");
//                    nameLabel.setText("Unknown User");
//                }
            } else {
                System.out.println("Failed to load profile. Server responded with code: " + responseCode);
                nameLabel.setText("Profile Load Failed");
            }
        } catch (Exception e) {
            e.printStackTrace();
            nameLabel.setText("Error Loading Profile");
        }
    }

    @FXML
    public void openHelpPopup() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HelpPopup.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }

    public void handleBack(MouseEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainPage.fxml"));
            Parent root = loader.load();

            MainPageController controller = loader.getController();
            //controller.someFuncToPassDataToNextSceneHere();

            // populate product cards
            controller.populate(null); // null because no ActionEvent here

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

    public void loadTransactionPage(MouseEvent event)
    {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("transactionPage.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();


            stage.setScene(scene);

            STAGE_UTILITY.switchStage(stage);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            ALERT_UTILITY.showAlert("ERROR", "Error loading transactions page...");
        }
    }


}