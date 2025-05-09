package com.cboard.marketplace.marketplace_frontend;

import com.cboard.marketplace.marketplace_common.*;
import com.cboard.marketplace.marketplace_common.dto.LocationDto;
import com.cboard.marketplace.marketplace_frontend.Utility.HttpUtility;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    private TextArea descriptionLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private ImageView productImage;

    @FXML
    private Label noLocationLabel;

    @FXML
    private Label locationLabel;
    @FXML
    private  Label posterLabel;
    @FXML
    private Label categoryLabel;
    @FXML
    private VBox fieldsBox;
    @FXML
    private Label sellerNameLabel;
    @FXML
    private Label sellerEmailLabel;

    public void closeProductCard(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SignUpController.class.getResource("mainPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        MainPageController mainPageController = fxmlLoader.getController();
        //mainPageController.populate(actionEvent);
        stage.setScene(scene);

        stage.show();
    }

    public void initializeProduct(ItemDto item) {
        // load the sellers id
        loadSellerInfo(item.getUserId());

        // Set product name
        nameLabel.setText(item.getName());

        // Set description
        descriptionLabel.setText(item.getDescription());

        // Set price
        priceLabel.setText(String.format("$%.2f", item.getPrice()));

        locationLabel.setText(item.getLocation());
        posterLabel.setText("TEMP");
        categoryLabel.setText(item.getCategory());


        Label newLable = new Label();
        HBox hbox = new HBox();
        if(item.getItemType().equalsIgnoreCase("request"))
        {
            newLable.setText("DEADLINE: ");
            hbox.getChildren().add(newLable);
            newLable = new Label();
            RequestDto rdto = (RequestDto) item;
            newLable.setText(rdto.getDeadline());
            hbox.getChildren().add(newLable);
            this.fieldsBox.getChildren().add(hbox);
        }
        else if(item.getItemType().equalsIgnoreCase("product"))
        {
            newLable.setText("BRAND: " + "\n");
            hbox.getChildren().add(newLable);
            newLable = new Label();
            ProductDto rdto = (ProductDto) item;
            newLable.setText(rdto.getBrand());
            hbox.getChildren().add(newLable);
            this.fieldsBox.getChildren().add(hbox);

            hbox = new HBox();
            newLable.setText("QUANTITY: ");
            hbox.getChildren().add(newLable);
            newLable = new Label();
            newLable.setText(""+rdto.getQuantity());
            hbox.getChildren().add(newLable);
            this.fieldsBox.getChildren().add(hbox);

        }
        else if(item.getItemType().equalsIgnoreCase("service"))
        {
            newLable.setText("DURATION: ");
            hbox.getChildren().add(newLable);
            newLable = new Label();
            ServiceDto rdto = (ServiceDto) item;
            newLable.setText(""+rdto.getDurationMinutes());
            hbox.getChildren().add(newLable);
            this.fieldsBox.getChildren().add(hbox);

        }
        // Load product image if present
        if (item.getImage_date() != null) {
            Image image = new Image(new ByteArrayInputStream(item.getImage_date()));
            productImage.setImage(image);
        } else {
            // Optional fallback image or visibility toggle
            productImage.setImage(null);
        }

        System.out.println("item.getLocationId() = " + item.getLocationId());
        // Show map if locationId is present
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
                .addHeader("Authorization", "Bearer " + SessionManager.getToken())
                .build();

        try (Response response = HttpUtility.HTTP_UTILITY.getClient().newCall(request).execute()) {
            if (response.isSuccessful()) {
                LocationDto location = HttpUtility.HTTP_UTILITY.getGson().fromJson(response.body().string(), LocationDto.class);

                Double pad = 0.002;
                Double lat = location.getLatitude();
                Double lng = location.getLongitude();

                if (lat == null || lng == null) {
                    mapView.setVisible(false);
                    noLocationLabel.setVisible(true);
                    return;
                }

                String mapHtml = """
                            <html>
                              <head>
                                <style>
                                  html, body {
                                    margin: 0;
                                    padding: 0;
                                    height: 100%%;
                                    overflow: hidden;
                                  }
                                  iframe {
                                    width: 100%%;
                                    height: 100%%;
                                    border: none;
                                  }
                                </style>
                              </head>
                              <body>
                                <iframe 
                                  src="https://www.openstreetmap.org/export/embed.html?bbox=%f,%f,%f,%f&layer=mapnik&marker=%f,%f&zoom=20">
                                </iframe>
                              </body>
                            </html>
                            """.formatted(lng - pad, lat - pad, lng + pad, lat + pad, lat, lng);


                mapView.getEngine().loadContent(mapHtml, "text/html");
            } else {
                mapView.setVisible(false);
                noLocationLabel.setVisible(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
            mapView.setVisible(false);
            noLocationLabel.setVisible(true);
        }
    }

    private void loadSellerInfo(int sellerId) {
        Request request = new Request.Builder()
                .url("http://localhost:8080/user/" + sellerId)
                .get()
                .addHeader("Authorization", "Bearer " + SessionManager.getToken())
                .build();

        try (Response response = HttpUtility.HTTP_UTILITY.getClient().newCall(request).execute()) {
            if (response.isSuccessful()) {
                UserDto user = HttpUtility.HTTP_UTILITY.getGson().fromJson(response.body().string(), UserDto.class);
                sellerNameLabel.setText(user.getFirstName() + " " + user.getLastName());
                sellerEmailLabel.setText(user.getEmail());
            } else {
                sellerNameLabel.setText("Seller info unavailable");
                sellerEmailLabel.setText("");
            }
        } catch (IOException e) {
            e.printStackTrace();
            posterLabel.setText("Seller info unavailable");
        }
    }
}