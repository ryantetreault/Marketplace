package com.cboard.marketplace.marketplace_frontend.Utility;

import com.cboard.marketplace.marketplace_common.CategoryDto;
import com.cboard.marketplace.marketplace_common.ItemDto;
import com.cboard.marketplace.marketplace_common.dto.LocationDto;
import com.cboard.marketplace.marketplace_frontend.SessionManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.gson.reflect.TypeToken;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import okhttp3.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static com.cboard.marketplace.marketplace_frontend.Utility.AlertUtility.ALERT_UTILITY;
import static com.cboard.marketplace.marketplace_frontend.Utility.HttpUtility.HTTP_UTILITY;

public class ItemRenderer {

    public static VBox createItemCard(ItemDto item, Consumer<ItemDto> onClick) {
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

        if (item.getImage_date() != null && item.getImage_date().length > 0) {
            Image image = new Image(new ByteArrayInputStream(item.getImage_date()));
            imageView.setImage(image);
        }
        else
        {
            try
            {
                imageView.setImage(new Image("file:/Users/cboard/Desktop/image.jpeg"));
            }
            catch(Exception e)
            {
                imageView.setImage(null);
            }
        }

        VBox textBox = new VBox(5);
        Label itemName = new Label(item.getName());
        itemName.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label price = new Label("Price: $" + String.format("%.2f", item.getPrice()));
        Label category = new Label("Category: " + item.getCategory());
        textBox.getChildren().addAll(itemName, price, category);

        contentBox.getChildren().addAll(imageView, textBox);

        card.getChildren().add(contentBox);

        card.setOnMouseClicked((MouseEvent e) -> onClick.accept(item));

        return card;
    }

    public static void showDetailedItemForUser(VBox targetContainer, VBox parentList, ItemDto item, Runnable onBack) {
        VBox detailView = new VBox(10);
        detailView.setPadding(new Insets(10));
        detailView.setStyle("-fx-background-color: #d0f0ff; -fx-border-color: darkblue; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        detailView.setPrefWidth(1060);

        ImageView imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(200);
        imageView.setPreserveRatio(true);

        if (item.getImage_date() != null && item.getImage_date().length > 0) {
            imageView.setImage(new Image(new ByteArrayInputStream(item.getImage_date())));
        } else
        {
            try
            {
                imageView.setImage(new Image("file:/Users/cboard/Desktop/image.jpeg"));
            }
            catch(Exception e)
            {
                imageView.setImage(null);
            }
        }

        Label itemName = new Label("Item: " + item.getName());
        itemName.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label price = new Label("Price: $" + String.format("%.2f", item.getPrice()));
        Label category = new Label("Category: " + item.getCategory());
        Label description = new Label("Description: " + item.getDescription());
        Label releaseDate = new Label("Release Date: " + item.getReleaseDate());
        Label location = new Label("Location: " + item.getLocation());

        detailView.getChildren().addAll(imageView, itemName, price, category, description, releaseDate, location);

        Map<String, String> specificFields = item.getSpecificFields();
        for (Map.Entry<String, String> entry : specificFields.entrySet()) {
            Label label = new Label(entry.getKey() + ": " + entry.getValue());
            detailView.getChildren().add(label);
        }

        Button backButton = new Button("Back");
        backButton.setStyle(
                "-fx-background-color: black; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: white;"
        );
        backButton.setOnAction(e -> {
            targetContainer.getChildren().setAll(parentList.getChildren());
            onBack.run();
        });

        Button editButton = new Button("Edit");
        editButton.setStyle(
                "-fx-background-color: orange; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: red;"
        );
        editButton.setOnAction(e -> {
            ItemRenderer.showEditItemView(targetContainer, parentList, item, onBack);
        });

        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(backButton, editButton);

        detailView.getChildren().addAll(buttons);

        targetContainer.getChildren().setAll(detailView);
    }

    public static void showEditItemView(VBox targetContainer, VBox parentList, ItemDto item, Runnable onBack)
    {
        VBox editView = new VBox(10);
        editView.setPadding(new Insets(10));
        editView.setStyle("-fx-background-color: #fff0f0; -fx-border-color: darkred; -fx-border-radius: 8px; -fx-background-radius: 8px;");
        editView.setPrefWidth(1060);

        TextField nameField = new TextField(item.getName());
        nameField.setStyle("-fx-background-color: white; -fx-text-fill: black;");
        TextField priceField = new TextField(String.format("%.2f", item.getPrice()));

        ComboBox<String> categoryField = new ComboBox<>();
        categoryField.setValue(item.getCategory());
        for(CategoryDto cat : getCategories())
            categoryField.getItems().add(cat.getName());

        TextField descriptionField = new TextField(item.getDescription());
        TextField releaseDateField = new TextField(item.getReleaseDate());

        ComboBox<String> locationField = new ComboBox<>();
        locationField.setValue(item.getLocation());
        for(LocationDto loc : getLocations())
            locationField.getItems().add(loc.getName());

        Label nameLabel = new Label("Name:");
        nameLabel.setStyle("-fx-font-weight: bold; -fx-underline: true; -fx-font-size: 13px;");
        Label priceLabel = new Label("Price:");
        priceLabel.setStyle("-fx-font-weight: bold; -fx-underline: true; -fx-font-size: 13px;");
        Label descriptionLabel = new Label("Description:");
        descriptionLabel.setStyle("-fx-font-weight: bold; -fx-underline: true; -fx-font-size: 13px;");
        Label releaseDatLabel = new Label("Release Date:");
        releaseDatLabel.setStyle("-fx-font-weight: bold; -fx-underline: true; -fx-font-size: 13px;");
        Label categoryLabel = new Label("Category");
        categoryLabel.setStyle("-fx-font-weight: bold; -fx-underline: true; -fx-font-size: 13px;");
        Label locationLabel = new Label("Location:");
        locationLabel.setStyle("-fx-font-weight: bold; -fx-underline: true; -fx-font-size: 13px;");

        editView.getChildren().addAll(
                nameLabel, nameField,
                priceLabel, priceField,
                descriptionLabel, descriptionField,
                releaseDatLabel, releaseDateField
        );


        Map<String, String> specificFields = item.getSpecificFields();
        Map<String, TextField> dynamicFieldInputs = new HashMap<>();

        for (Map.Entry<String, String> entry : specificFields.entrySet()) {
            Label label = new Label(entry.getKey() + ":");
            label.setStyle("-fx-font-weight: bold; -fx-underline: true; -fx-font-size: 13px;");
            TextField input = new TextField(entry.getValue());
            dynamicFieldInputs.put(entry.getKey(), input);
            editView.getChildren().addAll(label, input);
        }


        //put dropdowns together at the bottom
        editView.getChildren().addAll(
                categoryLabel, categoryField,
                locationLabel, locationField
        );


        Button backButton = new Button("Cancel");
        backButton.setStyle(
                "-fx-background-color: black; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: white;"
        );
        backButton.setOnAction(e -> {
            targetContainer.getChildren().setAll(parentList.getChildren());
            onBack.run();
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle(
                "-fx-background-color: #8B0000; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: orange;"
        );
        deleteButton.setOnAction(e -> {

            if(ItemRenderer.deleteItem(item.getItemId()))
            {
                List<ItemDto> updatedItems = getItemsForUser();
                displayItems(targetContainer, new ScrollPane(), updatedItems, updatedItems);
               // targetContainer.getChildren().setAll(parentList.getChildren());
                //onBack.run();
            }

        });

        Button saveButton = new Button("Save Changes");
        saveButton.setStyle(
                "-fx-background-color: #90EE90; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: black;"
        );
        saveButton.setOnAction(e -> {

            item.setName(nameField.getText());
            item.setPrice(Double.parseDouble(priceField.getText()));
            item.setCategory(categoryField.getValue());
            item.setDescription(descriptionField.getText());
            item.setReleaseDate(releaseDateField.getText());
            item.setLocation(locationField.getValue());

            Map<String, String> updatedSpecifics = new HashMap<>();
            for (Map.Entry<String, TextField> entry : dynamicFieldInputs.entrySet()) {
                updatedSpecifics.put(entry.getKey(), entry.getValue().getText());
            }
            item.setSpecificFields(updatedSpecifics);


            if(ItemRenderer.saveItem(item))
            {
                targetContainer.getChildren().setAll(parentList.getChildren());
                onBack.run();
            }
        });

        Button saveButtonWithImage = new Button("Save Changes and Add New Image");
        saveButtonWithImage.setStyle(
                "-fx-background-color: white; " +
                        "-fx-font-weight: bold; " +
                        "-fx-text-fill: green;"
        );
        saveButtonWithImage.setOnAction(e -> {

            item.setName(nameField.getText());
            item.setPrice(Double.parseDouble(priceField.getText()));
            item.setCategory(categoryField.getValue());
            item.setDescription(descriptionField.getText());
            item.setReleaseDate(releaseDateField.getText());
            item.setLocation(locationField.getValue());

            Map<String, String> updatedSpecifics = new HashMap<>();
            for (Map.Entry<String, TextField> entry : dynamicFieldInputs.entrySet()) {
                updatedSpecifics.put(entry.getKey(), entry.getValue().getText());
            }
            item.setSpecificFields(updatedSpecifics);


            if(ItemRenderer.saveItemWithImage(targetContainer ,item))
            {
                List<ItemDto> updatedItems = getItemsForUser();
                displayItems(targetContainer, new ScrollPane(), updatedItems, updatedItems);
            }
        });

        HBox buttons = new HBox(10);
        buttons.setAlignment(Pos.CENTER);
        buttons.getChildren().addAll(saveButton, saveButtonWithImage, deleteButton, backButton);

        editView.getChildren().add(buttons);
        targetContainer.getChildren().setAll(editView);
    }

    public static boolean saveItem(ItemDto item)
    {

        // send HTTP PUT or PATCH request to update the item
        try
        {
            String json = HTTP_UTILITY.getGson().toJson(item);
            RequestBody body = RequestBody.create(json, HTTP_UTILITY.getJSON());
            Request request = new Request.Builder()
                    .url("http://localhost:8080/item/update")
                    .put(body)
                    .addHeader("Authorization", "Bearer " + SessionManager.getToken())
                    .build();

            Response response = HttpUtility.HTTP_UTILITY.getClient().newCall(request).execute();
            if (response.isSuccessful())
            {
                // reload parent list or show success
                return true;
            }
            else
            {
                AlertUtility.ALERT_UTILITY.showAlert("Update Failed", "Error updating item.");
            }

        } catch (Exception ex)
        {
            ex.printStackTrace();
            AlertUtility.ALERT_UTILITY.showAlert("Exception", "Something went wrong.");
        }
        return false;

    }

    public static boolean saveItemWithImage(VBox targetContainer, ItemDto item)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select an image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File imageFile = fileChooser.showOpenDialog(targetContainer.getScene().getWindow());

        if (imageFile == null) {
            ALERT_UTILITY.showAlert("ERROR", "No image selected...");
            return false;
        }

        // send HTTP PUT or PATCH request to update the item
        try
        {
            String json = HTTP_UTILITY.getGson().toJson(item);
            //RequestBody body = RequestBody.create(json, HTTP_UTILITY.getJSON());

            RequestBody jsonBody = RequestBody.create(json, MediaType.parse("application/json"));
            RequestBody fileBody = RequestBody.create(imageFile, MediaType.parse("image/*"));

            MultipartBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("item", null, jsonBody)
                    .addFormDataPart("image", imageFile.getName(), fileBody)
                    .build();

            Request request = new Request.Builder()
                    .url("http://localhost:8080/item/update/with-image")
                    .put(requestBody)
                    .addHeader("Authorization", "Bearer " + SessionManager.getToken())
                    .build();

            Response response = HttpUtility.HTTP_UTILITY.getClient().newCall(request).execute();
            if (response.isSuccessful())
            {
                // reload parent list or show success
                return true;
            }
            else
            {
                AlertUtility.ALERT_UTILITY.showAlert("Update Failed", "Error updating item.");
            }

        } catch (Exception ex)
        {
            ex.printStackTrace();
            AlertUtility.ALERT_UTILITY.showAlert("Exception", "Something went wrong.");
        }
        return false;

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
                ItemRenderer.showDetailedItemForUser(
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


    public static boolean deleteItem(int itemId)
    {
        Request request = new Request.Builder()
                .url("http://localhost:8080/item/" + itemId + "/delete")
                .delete()
                .addHeader("Authorization", "Bearer " + SessionManager.getToken())
                .build();

        try(Response response = HTTP_UTILITY.getClient().newCall(request).execute())
        {
            if(response.isSuccessful() && response.body() != null)
            {
                String responseBody = response.body().string();
                ALERT_UTILITY.showAlert("", responseBody);
                return true;
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
        return false;
    }

    public static List<ItemDto> getItemsForUser()
    {
        int userId = SessionManager.getUserIdFromToken();

        Request request = new Request.Builder()
                .url("http://localhost:8080/item/" + userId + "/owner")
                .get()
                .addHeader("Authorization", "Bearer " + SessionManager.getToken())
                .build();

        try(Response response = HTTP_UTILITY.getClient().newCall(request).execute())
        {
            if(response.isSuccessful() && response.body() != null)
            {
                String responseBody = response.body().string();
                return HTTP_UTILITY.getObjMapper().readValue(responseBody, new TypeReference<List<ItemDto>>() {} ) ;
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
        return new ArrayList<>();
    }

    public static void fadeInNode(Node node) {
        FadeTransition ft = new FadeTransition(Duration.millis(300), node);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
    }

    public static List<LocationDto> getLocations()
    {

        Request request = new Request.Builder()
                .url("http://localhost:8080/api/locations")
                .get()
                .addHeader("Authorization", "Bearer " + SessionManager.getToken())
                .build();

        try (Response response = HttpUtility.HTTP_UTILITY.getClient().newCall(request).execute()) {
            if (response.isSuccessful()) {
                String json = response.body().string();

                Type listType = new TypeToken<List<LocationDto>>() {}.getType();
                return HttpUtility.HTTP_UTILITY.getGson().fromJson(json, listType);

            } else {
                System.out.println("Failed to load locations. Status code: " + response.code());
                System.out.println("Body: " + response.body().string());
                System.out.println("Failed to load locations.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static List<CategoryDto> getCategories()
    {
        Request request = new Request.Builder()
                .url("http://localhost:8080/item/categories")
                .get()
                .addHeader("Authorization", "Bearer " + SessionManager.getToken())
                .build();

        try (Response response = HttpUtility.HTTP_UTILITY.getClient().newCall(request).execute()) {
            if (response.isSuccessful()) {
                String json = response.body().string();

                Type listType = new TypeToken<List<CategoryDto>>() {}.getType();
                return HttpUtility.HTTP_UTILITY.getGson().fromJson(json, listType);


            } else {
                System.out.println("Failed to load categories: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
