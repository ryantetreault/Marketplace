package com.cboard.marketplace.marketplace_frontend;

import com.cboard.marketplace.marketplace_common.dto.LocationDto;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListNewController {
    @FXML
    private ComboBox<String> locationDropdown;

    private Map<String, Integer> locationMap = new HashMap<>();

    private int userId;

    public void closeListNew(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize() {
        loadSafeLocations();
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
}