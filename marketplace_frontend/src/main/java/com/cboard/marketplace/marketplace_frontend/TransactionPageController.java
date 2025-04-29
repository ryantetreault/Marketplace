package com.cboard.marketplace.marketplace_frontend;

import com.cboard.marketplace.marketplace_common.ItemDto;
import com.cboard.marketplace.marketplace_common.TransactionDto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.animation.FadeTransition;
import javafx.util.Duration;
import javafx.stage.Stage;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.cboard.marketplace.marketplace_frontend.HttpUtility.HTTP_UTILITY;
import static com.cboard.marketplace.marketplace_frontend.AlertUtility.ALERT_UTILITY;

public class TransactionPageController
{
    private List<TransactionDto> transactions = new ArrayList<>();
    @FXML
    ScrollPane transactionContainer;
    @FXML
    VBox transactionListVBox;
    @FXML
    TextField searchField;

    public void initialize()
    {
        transactionListVBox = new VBox(10);
        transactionListVBox.setPadding(new Insets(10));
        transactionContainer.setContent(transactionListVBox);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<TransactionDto> filteredTransactions = filterTransactionsBySearch(newValue);
            transactionListVBox.getChildren().clear(); // Clear old results
            displayTransactions(filteredTransactions); // Show new results
        });


        displayTransactions(getAllTransactions());
    }

    private List<TransactionDto> getAllTransactions()
    {
        Request request = new Request.Builder()
                .url("http://localhost:8080/transaction/all")
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
                ALERT_UTILITY.showAlert("ERROR", "Error occurred...");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return transactions;
    }

    private List<TransactionDto> filterTransactionsBySearch(String keyword)
    {
        if (keyword == null || keyword.isBlank())
        {
            return transactions;
        }

        String lowerKeyword = keyword.toLowerCase();

        return transactions.stream()
                .filter(transaction ->
                        String.valueOf(transaction.getTransactionId()).contains(lowerKeyword) ||
                                (transaction.getBuyer() != null && transaction.getBuyer().getUsername() != null &&
                                        transaction.getBuyer().getUsername().toLowerCase().contains(lowerKeyword)) ||
                                (transaction.getSeller() != null && transaction.getSeller().getUsername() != null &&
                                        transaction.getSeller().getUsername().toLowerCase().contains(lowerKeyword)) ||
                                (transaction.getItem() != null &&
                                        ((transaction.getItem().getName() != null && transaction.getItem().getName().toLowerCase().contains(lowerKeyword)) ||
                                                (transaction.getItem().getDescription() != null && transaction.getItem().getDescription().toLowerCase().contains(lowerKeyword)) ||
                                                (transaction.getItem().getCategory() != null && transaction.getItem().getCategory().toLowerCase().contains(lowerKeyword)) ||
                                                (transaction.getItem().getLocation() != null && transaction.getItem().getLocation().toLowerCase().contains(lowerKeyword)) ||
                                                (transaction.getItem().getItemType() != null && transaction.getItem().getItemType().toLowerCase().contains(lowerKeyword)) ||
                                                String.valueOf(transaction.getItem().getPrice()).contains(lowerKeyword) ||
                                                (transaction.getItem().getReleaseDate() != null && transaction.getItem().getReleaseDate().toLowerCase().contains(lowerKeyword)))
                                )
                )
                .toList();
    }


    public void displayTransactions(List<TransactionDto> transactionsToDisplay)
    {
        transactionListVBox.getChildren().clear();

        for (TransactionDto transaction : transactionsToDisplay)
        {
            VBox card = createTransactionCard(transaction);
            transactionListVBox.getChildren().add(card);
            fadeInNode(card);
        }

    }

    private VBox createTransactionCard(TransactionDto transaction)
    {
        VBox card = new VBox(5); // spacing inside card
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: #f0f0f0; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        card.setPrefWidth(900);

        Label itemName = new Label(transaction.getItem().getName());
        Label price = new Label("Price: $" + transaction.getItem().getPrice());
        Label category = new Label("Category: " + transaction.getItem().getCategory());

        card.getChildren().addAll(itemName, price, category);

        // Set click behavior
        card.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent)
            {
                showDetailedTransaction(transaction);
            }

        });

        return card;
    }

    private void showDetailedTransaction(TransactionDto transaction)
    {
        VBox detailView = new VBox(10);
        detailView.setPadding(new Insets(10));
        detailView.setStyle("-fx-background-color: #e0e0ff; -fx-border-color: darkblue; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        detailView.setPrefWidth(900);

        Label itemName = new Label("Item: " + transaction.getItem().getName());
        Label price = new Label("Price: $" + transaction.getItem().getName());
        Label category = new Label("Category: " + transaction.getItem().getCategory());
        Label description = new Label("Description: " + transaction.getItem().getDescription());
        Label releaseDate = new Label("Release Date: " + transaction.getItem().getReleaseDate());
        Label location = new Label("Location: " + transaction.getItem().getLocation());
        Label buyerUsername = new Label("Buyer: " + transaction.getBuyer().getUsername());
        Label sellerUsername = new Label("Seller: " + transaction.getSeller().getUsername());

        Button backButton = new Button("Back to Transactions");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent)
            {
                transactionContainer.setContent(transactionListVBox); // Go back to full list

            }
        });

        detailView.getChildren().addAll(itemName, price, category, description, releaseDate, location, buyerUsername, sellerUsername);

        ItemDto item = transaction.getItem();
        Map<String, String> specificFields = item.getSpecificFields();
        for (Map.Entry<String, String> entry : specificFields.entrySet()) {
            Label label = new Label(entry.getKey() + ": " + entry.getValue());
            detailView.getChildren().add(label);
        }

        detailView.getChildren().add(backButton);

        transactionContainer.setContent(detailView);
    }





    public void profileClicked(MouseEvent mouseEvent) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(SignUpController.class.getResource("userProfile.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        userProfileController userProfileController = fxmlLoader.getController();
        //userProfileController.someFuncToPassDataToNextSceneHere();


        stage.setScene(scene);

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
            stage.show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private void fadeInNode(Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(300), node); // 300ms duration
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    @FXML
    public void openHelpPopup() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HelpPopup.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }
}
