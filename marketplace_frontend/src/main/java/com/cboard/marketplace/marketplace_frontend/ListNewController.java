package com.cboard.marketplace.marketplace_frontend;

import com.cboard.marketplace.marketplace_common.dto.LocationDto;
import com.cboard.marketplace.marketplace_frontend.Utility.HttpUtility;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import okhttp3.Request;
import okhttp3.Response;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.cboard.marketplace.marketplace_frontend.Utility.StageUtility.STAGE_UTILITY;
import static com.cboard.marketplace.marketplace_frontend.Utility.TooltipUtility.TOOLTIP_UTILITY;

public class ListNewController {
    @FXML
    private ComboBox<String> locationDropdown;

    private Map<String, Integer> locationMap = new HashMap<>();

    private int userId;

    @FXML
    private ComboBox<String> itemTypeComboBox;

    @FXML
    private VBox dynamicFormContainer;

    @FXML
    private javafx.scene.control.Label errorLabel;

    @FXML
    private ComboBox<String> categoryComboBox;

    private Map<String, Integer> categoryMap = new HashMap<>();



    public void closeListNew(ActionEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainPage.fxml"));
            Parent root = loader.load();

            MainPageController controller = loader.getController();
            //controller.someFuncToPassDataToNextSceneHere();

            // populate product cards
            //controller.populate(event); // null because no ActionEvent here

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
    public void initialize() {

        loadSafeLocations();
        loadItemTypes();
        loadCategories();

        itemTypeComboBox.setOnAction(event -> handleItemTypeSelection());
    }

    public void handleItemTypeSelection() {
        String selectedType = itemTypeComboBox.getValue();
        String fxmlFile = null;

        switch (selectedType) {
            case "Product":
                fxmlFile = "productForm.fxml";
                break;
            case "Service":
                fxmlFile = "serviceForm.fxml";
                break;
            case "Request":
                fxmlFile = "requestForm.fxml";
                break;
            default:
                dynamicFormContainer.getChildren().clear();
                return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent form = loader.load();
            dynamicFormContainer.getChildren().setAll(form);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadSafeLocations() {
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/locations")
                .get()
                .addHeader("Authorization", "Bearer " + SessionManager.getToken())
                .build();

        try (Response response = HttpUtility.HTTP_UTILITY.getClient().newCall(request).execute()) {
            if (response.isSuccessful()) {
                String json = response.body().string();

                Type listType = new TypeToken<List<LocationDto>>() {}.getType();
                List<LocationDto> locations = HttpUtility.HTTP_UTILITY.getGson().fromJson(json, listType);

                for (LocationDto loc : locations) {
                    locationDropdown.getItems().add(loc.getName());
                    locationMap.put(loc.getName(), loc.getLocationId());
                }
            } else {
                System.out.println("Failed to load locations. Status code: " + response.code());
                System.out.println("Body: " + response.body().string());
                System.out.println("Failed to load locations.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUserId(int userId) {
        this.userId = userId;
        System.out.println("Listing screen opened by user: " + userId);
    }

    private void loadItemTypes() {
        Request request = new Request.Builder()
                .url("http://localhost:8080/item/types")
                .get()
                .addHeader("Authorization", "Bearer " + SessionManager.getToken())
                .build();

        try (Response response = HttpUtility.HTTP_UTILITY.getClient().newCall(request).execute()) {
            if (response.isSuccessful()) {
                String json = response.body().string();

                Type listType = new TypeToken<List<String>>() {}.getType();
                List<String> itemTypes = HttpUtility.HTTP_UTILITY.getGson().fromJson(json, listType);

                // populate ComboBox
                itemTypeComboBox.getItems().addAll(itemTypes);
            } else {
                System.out.println("Failed to load item types. Code: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void createListing(ActionEvent event) {
        errorLabel.setText("");

        String selectedType = itemTypeComboBox.getValue();
        String categoryName = categoryComboBox.getValue();
        String locationName = locationDropdown.getValue();

        selectedType =  selectedType.toLowerCase();

        if (selectedType == null || locationName == null) {
            errorLabel.setText("Please select an item type and a location.");
            return;
        }

        int locationId = locationMap.get(locationName);
        int userId = this.userId;

        String category = categoryComboBox.getValue();
        if (category == null) {
            errorLabel.setText("Please select a category.");
            return;
        }

        String releaseDate = java.time.LocalDate.now().toString();
        boolean available = true;
        String imageName = "placeholder.jpg";
        String imageType = "image/jpeg";
        byte[] imageData = new byte[0];

        var nameField = (javafx.scene.control.TextField) dynamicFormContainer.lookup("#nameField");
        var descriptionField = (javafx.scene.control.TextField) dynamicFormContainer.lookup("#descriptionField");
        var priceField = (javafx.scene.control.TextField) dynamicFormContainer.lookup("#priceField");

        if (nameField == null || priceField == null || nameField.getText().isBlank() || priceField.getText().isBlank()) {
            errorLabel.setText("Name and price are required.");
            return;
        }

        String name = nameField.getText();
        String description = (descriptionField != null) ? descriptionField.getText() : "";
        double price;

        try {
            price = Double.parseDouble(priceField.getText());
        } catch (NumberFormatException e) {
            errorLabel.setText("Price must be a valid number.");
            return;
        }

        Object dto = null;

        try {
            switch (selectedType) {
                case "product" -> {
                    var quantityField = (javafx.scene.control.TextField) dynamicFormContainer.lookup("#quantityField");
                    var brandField = (javafx.scene.control.TextField) dynamicFormContainer.lookup("#brandField");

                    if (quantityField == null || quantityField.getText().isBlank()) {
                        errorLabel.setText("Quantity is required for a product.");
                        return;
                    }

                    int quantity = Integer.parseInt(quantityField.getText());
                    String brand = (brandField != null) ? brandField.getText() : "";

                    dto = new com.cboard.marketplace.marketplace_common.ProductDto(
                            0, name, description, price, userId, category, releaseDate, available,
                            locationName, selectedType, imageName, imageType, imageData, quantity, brand
                    );
                }
                case "service" -> {
                    var durationField = (javafx.scene.control.TextField) dynamicFormContainer.lookup("#durationField");

                    if (durationField == null || durationField.getText().isBlank()) {
                        errorLabel.setText("Duration is required for a service.");
                        return;
                    }

                    int duration = Integer.parseInt(durationField.getText());

                    dto = new com.cboard.marketplace.marketplace_common.ServiceDto(
                            0, name, description, price, userId, category, releaseDate, available,
                            locationName, selectedType, imageName, imageType, imageData, duration
                    );
                }
                case "request" -> {
                    var deadlineField = (javafx.scene.control.TextField) dynamicFormContainer.lookup("#deadlineField");

                    if (deadlineField == null || deadlineField.getText().isBlank()) {
                        errorLabel.setText("Deadline is required for a request.");
                        return;
                    }

                    String deadline = deadlineField.getText();

                    dto = new com.cboard.marketplace.marketplace_common.RequestDto(
                            0, name, description, price, userId, category, releaseDate, available,
                            locationName, selectedType, imageName, imageType, imageData, deadline
                    );
                }
                default -> {
                    errorLabel.setText("Unknown item type.");
                    return;
                }
            }

            // send the DTO to the backend
            String json = HttpUtility.HTTP_UTILITY.getGson().toJson(dto);
            okhttp3.RequestBody body = okhttp3.RequestBody.create(json, HttpUtility.HTTP_UTILITY.getJSON());

            Request request = new Request.Builder()
                    .url("http://localhost:8080/item/add")
                    .post(body)
                    .addHeader("Authorization", "Bearer " + SessionManager.getToken())
                    .build();

            try (Response response = HttpUtility.HTTP_UTILITY.getClient().newCall(request).execute()) {
                if (response.isSuccessful()) {
                    errorLabel.setText(""); // clear error on success
                    System.out.println("Listing created!");
                    closeListNew(event); // Return to main page
                } else {
                    errorLabel.setText("Failed to create listing: " + response.code());
                    System.out.println(response.body().string());
                }
            }

        } catch (Exception e) {
            errorLabel.setText("Something went wrong. Check your input.");
            e.printStackTrace();
        }
    }

    private void loadCategories() {
        Request request = new Request.Builder()
                .url("http://localhost:8080/item/categories")
                .get()
                .addHeader("Authorization", "Bearer " + SessionManager.getToken())
                .build();

        try (Response response = HttpUtility.HTTP_UTILITY.getClient().newCall(request).execute()) {
            if (response.isSuccessful()) {
                String json = response.body().string();

                Type listType = new TypeToken<List<Map<String, Object>>>() {}.getType();
                List<Map<String, Object>> categories = HttpUtility.HTTP_UTILITY.getGson().fromJson(json, listType);

                for (Map<String, Object> category : categories) {
                    String name = (String) category.get("name");
                    Integer id = ((Double) category.get("categoryId")).intValue();
                    categoryComboBox.getItems().add(name);
                    categoryMap.put(name, id);
                }
            } else {
                System.out.println("Failed to load categories: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}