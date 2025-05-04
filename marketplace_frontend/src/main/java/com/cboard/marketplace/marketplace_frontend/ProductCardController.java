package com.cboard.marketplace.marketplace_frontend;

import com.cboard.marketplace.marketplace_common.dto.LocationDto;
import com.cboard.marketplace.marketplace_common.ItemDto;
import com.cboard.marketplace.marketplace_frontend.Utility.HttpUtility;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.web.WebView;
import okhttp3.Request;
import okhttp3.Response;
import javafx.scene.control.Label;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Type;

public class ProductCardController {
    @FXML
    private WebView mapView;

    @FXML
    private Label nameLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private ImageView productImage;

    @FXML
    private Label noLocationLabel;

    public void closeProductCard(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SignUpController.class.getResource("mainPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        MainPageController mainPageController = fxmlLoader.getController();
        mainPageController.populate(actionEvent);
        stage.setScene(scene);

        stage.show();
    }

    public void initializeProduct(ItemDto item) {
        // Set product name
        nameLabel.setText(item.getName());

        // Set description
        descriptionLabel.setText(item.getDescription());

        // Set price
        priceLabel.setText(String.format("$%.2f", item.getPrice()));

        // Load product image if present
        if (item.getImage_date() != null) {
            Image image = new Image(new ByteArrayInputStream(item.getImage_date()));
            productImage.setImage(image);
        } else {
            // Optional fallback image or visibility toggle
            productImage.setImage(null);
        }

        // Show  map if locationId is present
        if (item.getLocationId() != null) {
            mapView.setVisible(true);
            noLocationLabel.setVisible(false);
            loadMap(item.getLocationId());
        } else {
            mapView.setVisible(false);
            noLocationLabel.setVisible(true);
        }
    }

    private void loadMap(int locationId) {
        Request request = new Request.Builder()
                .url("http://localhost:8080/api/locations/" + locationId)
                .get()
                .build();

        try (Response response = HttpUtility.HTTP_UTILITY.getClient().newCall(request).execute()) {
            if (response.isSuccessful()) {
                String json = response.body().string();

                Type locationType = new TypeToken<LocationDto>() {}.getType();
                LocationDto location = HttpUtility.HTTP_UTILITY.getGson().fromJson(json, locationType);

                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                String mapUrl = "https://maps.google.com/maps?q=" + latitude + "," + longitude + "&z=15&output=embed";
                mapView.getEngine().load(mapUrl);
                mapView.setVisible(true);
            } else {
                mapView.setVisible(false);
                System.out.println("Failed to load location.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            mapView.setVisible(false);
        }
    }
}