package com.cboard.marketplace.marketplace_frontend;

import com.cboard.marketplace.marketplace_common.ProductDto;
import com.cboard.marketplace.marketplace_common.RequestDto;
import com.cboard.marketplace.marketplace_common.ServiceDto;
import com.cboard.marketplace.marketplace_common.dto.LocationDto;
import com.cboard.marketplace.marketplace_common.ItemDto;
import com.cboard.marketplace.marketplace_frontend.Utility.HttpUtility;
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
import java.io.InputStream;
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
        if (item.getImage_date() != null)
        {
            System.out.println("image not null");
            InputStream is = new ByteArrayInputStream(item.getImage_date());
            Image image = new Image(is);
            if(image.isError())
                System.out.println("Image load error: " + image.getException());
            //Image image = new Image(new ByteArrayInputStream(item.getImage_date()));
            productImage.setImage(image);
        } else {
            System.out.println("image null");
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
                .url("http://localhost:8080/" + locationId)
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