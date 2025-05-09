package com.cboard.marketplace.marketplace_frontend.Utility;

import com.cboard.marketplace.marketplace_common.ItemDto;
import com.cboard.marketplace.marketplace_common.TransactionDto;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TransactionRenderer {

    public static VBox createTransactionCard(TransactionDto transaction, Consumer<TransactionDto> onClick) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: #d0f0ff; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        card.setPrefWidth(1060);

        HBox contentBox = new HBox(10);
        contentBox.setAlignment(Pos.CENTER_LEFT);


        ImageView imageView = new ImageView();
        imageView.setFitWidth(80);
        imageView.setFitHeight(80);
        imageView.setPreserveRatio(true);

        if (transaction.getItem().getImage_date() != null && transaction.getItem().getImage_date().length > 0) {
            Image image = new Image(new ByteArrayInputStream(transaction.getItem().getImage_date()));
            imageView.setImage(image);
        }
        else
        {
            imageView.setImage(new Image("file:/Users/cboard/Desktop/image.jpeg"));
        }

        VBox textBox = new VBox(5);

        Label itemName = new Label(transaction.getItem().getName());
        itemName.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label price = new Label("Price: $" + String.format("%.2f", transaction.getItem().getPrice()) );
        Label category = new Label("Category: " + transaction.getItem().getCategory());
        textBox.getChildren().addAll(itemName, price, category);

        contentBox.getChildren().addAll(imageView, textBox);

        card.getChildren().addAll(contentBox);

        card.setOnMouseClicked((MouseEvent e) -> onClick.accept(transaction));

        return card;
    }

    public static void showDetailedTransaction(VBox targetContainer, VBox parentList, TransactionDto transaction, Runnable onBack) {
        VBox detailView = new VBox(10);
        detailView.setPadding(new Insets(10));
        detailView.setStyle("-fx-background-color: #d0f0ff; -fx-border-color: darkblue; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        detailView.setPrefWidth(1060);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        if (transaction.getItem().getImage_date() != null && transaction.getItem().getImage_date().length > 0) {
            imageView.setImage(new Image(new ByteArrayInputStream(transaction.getItem().getImage_date())));
        } else
        {
            imageView.setImage(new Image("file:/Users/cboard/Desktop/image.jpeg"));
        }

        Label itemName = new Label("Item: " + transaction.getItem().getName());
        itemName.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label price = new Label("Price: $" + String.format("%.2f", transaction.getItem().getPrice()));
        Label category = new Label("Category: " + transaction.getItem().getCategory());
        Label description = new Label("Description: " + transaction.getItem().getDescription());
        Label releaseDate = new Label("Release Date: " + transaction.getItem().getReleaseDate());
        Label location = new Label("Location: " + transaction.getItem().getLocation());
        Label buyerUsername = new Label("Buyer: " + transaction.getBuyer().getUsername());
        Label sellerUsername = new Label("Seller: " + transaction.getSeller().getUsername());

        detailView.getChildren().addAll(imageView, itemName, price, category, description, releaseDate, location, buyerUsername, sellerUsername);

        ItemDto item = transaction.getItem();
        Map<String, String> specificFields = item.getSpecificFields();
        for (Map.Entry<String, String> entry : specificFields.entrySet()) {
            Label label = new Label(entry.getKey() + ": " + entry.getValue());
            detailView.getChildren().add(label);
        }

        Button backButton = new Button("Back");
        backButton.setOnAction(e -> {
            targetContainer.getChildren().setAll(parentList.getChildren());
            onBack.run();
        });

        detailView.getChildren().add(backButton);

        targetContainer.getChildren().setAll(detailView);
    }

    public static List<TransactionDto> filterTransactionsBySearch(List<TransactionDto> transactions, String keyword)
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
                                                (transaction.getItem().getReleaseDate() != null && transaction.getItem().getReleaseDate().toLowerCase().contains(lowerKeyword))) ||

                                        transaction.getItem().getSpecificFields().entrySet().stream()
                                                .anyMatch(entry ->
                                                        (entry.getKey() != null && entry.getKey().toLowerCase().contains(lowerKeyword)) ||
                                                                (entry.getValue() != null && entry.getValue().toLowerCase().contains(lowerKeyword))
                                                )
                                )
                )
                .toList();
    }


    public static void displayTransactions(VBox transactionListVBox, ScrollPane transactionContainer, List<TransactionDto> all, List<TransactionDto> transactionsToDisplay)
    {

        transactionListVBox.getChildren().clear();

        for (TransactionDto transaction : transactionsToDisplay)
        {
            VBox newList = new VBox(10);
            newList.setPadding(new Insets(10));

            VBox card = TransactionRenderer.createTransactionCard(transaction, t -> {
                TransactionRenderer.showDetailedTransaction(
                        transactionListVBox,
                        newList,
                        t,
                        () -> {
                            transactionContainer.setContent(transactionListVBox);
                            displayTransactions(transactionListVBox, transactionContainer, all, all);
                        }
                );
            });

            transactionListVBox.getChildren().add(card);
            TransactionRenderer.fadeInNode(card);
        }

    }

    public static void fadeInNode(Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(300), node);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }
}
