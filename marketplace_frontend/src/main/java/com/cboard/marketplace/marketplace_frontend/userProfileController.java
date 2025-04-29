package com.cboard.marketplace.marketplace_frontend;

import com.cboard.marketplace.marketplace_common.dto.UserDto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import javafx.scene.control.Label;
import okhttp3.Request;
import okhttp3.Response;

import java.net.URL;
import java.util.ResourceBundle;

public class userProfileController implements Initializable {
    @FXML
    private Label nameLabel;

    public void closeProfile(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SignUpController.class.getResource("mainPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.setFill(Color.TRANSPARENT);
        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();
        MainPageController mainPageController = fxmlLoader.getController();
        mainPageController.populate(actionEvent);
        stage.setScene(scene);

        stage.show();
    }

    public void listNew(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SignUpController.class.getResource("listNew.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage)((Node) actionEvent.getSource()).getScene().getWindow();


        stage.setScene(scene);

        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadUserProfile();
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
                UserDto user = HttpUtility.HTTP_UTILITY.getGson().fromJson(responseBody, UserDto.class);

                nameLabel.setText(user.getFirstName() + " " + user.getLastName());
            } else {
                System.out.println("Failed to load profile. Server responded with code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}