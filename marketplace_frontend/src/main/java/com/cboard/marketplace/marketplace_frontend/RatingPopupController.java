package com.cboard.marketplace.marketplace_frontend;

import com.cboard.marketplace.marketplace_frontend.Utility.AlertUtility;
import com.cboard.marketplace.marketplace_frontend.Utility.HttpUtility;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import okhttp3.*;

import java.io.IOException;

public class RatingPopupController {

    @FXML
    private Slider ratingSlider;

    private int sellerId;

    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }

    @FXML
    private void submitRating() {
        double score = ratingSlider.getValue();
        System.out.println("Submitting rating: " + score + " to seller ID: " + sellerId);

        String url = "http://localhost:8080/user/api/rate/" + sellerId + "?score=" + score;

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(new byte[0])) // empty body
                .addHeader("Authorization", "Bearer " + SessionManager.getToken())
                .build();

        HttpUtility.HTTP_UTILITY.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> returnToMainPage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                Platform.runLater(() -> returnToMainPage());
            }
        });
    }

    @FXML
    private void closePopup() {
        returnToMainPage();
    }

    private void returnToMainPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mainPage.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ratingSlider.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}