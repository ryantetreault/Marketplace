package com.cboard.marketplace.marketplace_frontend.Utility;

import com.cboard.marketplace.marketplace_common.ItemDto;
import com.cboard.marketplace.marketplace_common.TransactionDto;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ItemRenderer {

    public static VBox createItemCard(ItemDto item, Consumer<ItemDto> onClick) {
        VBox card = new VBox(5);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: #f0f0f0; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        card.setPrefWidth(900);

        Label itemName = new Label(item.getName());
        Label price = new Label("Price: $" + item.getPrice());
        Label category = new Label("Category: " + item.getCategory());

        card.getChildren().addAll(itemName, price, category);

        card.setOnMouseClicked((MouseEvent e) -> onClick.accept(item));

        return card;
    }

    public static void showDetailedItem(VBox targetContainer, VBox parentList, ItemDto item, Runnable onBack) {
        VBox detailView = new VBox(10);
        detailView.setPadding(new Insets(10));
        detailView.setStyle("-fx-background-color: #e0e0ff; -fx-border-color: darkblue; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        detailView.setPrefWidth(900);

        Label itemName = new Label("Item: " + item.getName());
        Label price = new Label("Price: $" + item.getPrice());
        Label category = new Label("Category: " + item.getCategory());
        Label description = new Label("Description: " + item.getDescription());
        Label releaseDate = new Label("Release Date: " + item.getReleaseDate());
        Label location = new Label("Location: " + item.getLocation());

        detailView.getChildren().addAll(itemName, price, category, description, releaseDate, location);

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

    public static List<ItemDto> filterItemsBySearch(List<ItemDto> items, String keyword)
    {
        if (keyword == null || keyword.isBlank())
        {
            return items;
        }

        String lowerKeyword = keyword.toLowerCase();

        return items.stream()
                .filter(item ->
                                (item != null &&
                                        ((item.getName() != null && item.getName().toLowerCase().contains(lowerKeyword)) ||
                                                (item.getDescription() != null && item.getDescription().toLowerCase().contains(lowerKeyword)) ||
                                                (item.getCategory() != null && item.getCategory().toLowerCase().contains(lowerKeyword)) ||
                                                (item.getLocation() != null && item.getLocation().toLowerCase().contains(lowerKeyword)) ||
                                                (item.getItemType() != null && item.getItemType().toLowerCase().contains(lowerKeyword)) ||
                                                String.valueOf(item.getPrice()).contains(lowerKeyword) ||
                                                (item.getReleaseDate() != null && item.getReleaseDate().toLowerCase().contains(lowerKeyword))) ||

                                        item.getSpecificFields().entrySet().stream()
                                                .anyMatch(entry ->
                                                        (entry.getKey() != null && entry.getKey().toLowerCase().contains(lowerKeyword)) ||
                                                                (entry.getValue() != null && entry.getValue().toLowerCase().contains(lowerKeyword))
                                                )
                                )
                )
                .toList();
    }

    public static void displayItems(VBox itemListVBox, ScrollPane itemContainer, List<ItemDto> all, List<ItemDto> itemsToDisplay)
    {

        itemListVBox.getChildren().clear();

        for (ItemDto item : itemsToDisplay)
        {
            VBox newList = new VBox(10);
            newList.setPadding(new Insets(10));

            VBox card = ItemRenderer.createItemCard(item, t -> {
                ItemRenderer.showDetailedItem(
                        itemListVBox,
                        newList,
                        t,
                        () -> {
                            itemContainer.setContent(itemListVBox);
                            displayItems(itemListVBox, itemContainer, all, all);
                        }
                );
            });

            itemListVBox.getChildren().add(card);
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
