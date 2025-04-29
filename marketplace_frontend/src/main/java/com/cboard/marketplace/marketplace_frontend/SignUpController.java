package com.cboard.marketplace.marketplace_frontend;

import com.cboard.marketplace.marketplace_common.dto.AuthResponse;
import com.cboard.marketplace.marketplace_common.dto.LoginRequest;
import com.cboard.marketplace.marketplace_frontend.Request.SignupRequest;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class SignUpController {
    @FXML
    private Button signUpButton;

    @FXML
    private VBox vbox;


    private void fadeInError(javafx.scene.control.Label errorLabel) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), errorLabel);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    @FXML
    public void handleSignUpButtonAction() {

        // find fields inside the vbox
        TextField firstNameField = (TextField) vbox.lookup("#firstNameField");
        TextField lastNameField = (TextField) vbox.lookup("#lastNameField");
        TextField emailField = (TextField) vbox.lookup("#emailField");
        TextField usernameField = (TextField) vbox.lookup("#usernameField");
        TextField passwordField = (TextField) vbox.lookup("#passwordField");
        Label errorLabel = (Label) vbox.lookup("#errorLabel");

        errorLabel.setText("button clicked!");

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("All fields must be filled out.");
            errorLabel.setOpacity(0); // instantly hide
            errorLabel.setVisible(true);
            fadeInError(errorLabel);
            return;
        }

        try {
            // make JSON body
            SignupRequest signUpRequest = new SignupRequest(firstName, lastName, email, username, password);
            String json = HttpUtility.HTTP_UTILITY.getGson().toJson(signUpRequest);

            // build HTTP POST request to backend
            RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url("http://localhost:8080/api/auth/signup")
                    .post(body)
                    .build();

            // execute request
            Response response = HttpUtility.HTTP_UTILITY.getClient().newCall(request).execute();
            String responseBody = response.body().string();
            int responseCode = response.code();

//            // debugging
//            System.out.println("HTTP response code: " + responseCode);
//            System.out.println("HTTP response body: " + responseBody);

            if (response.isSuccessful()) {
                // successful signup message
                errorLabel.setText("Sign up successful!");
                errorLabel.setTextFill(Color.GREEN);
                errorLabel.setOpacity(0); // instantly hide
                errorLabel.setVisible(true);
                fadeInError(errorLabel);

                // auto log in user to get token
                autoLogin(username, password);

                // load mainPage.fxml
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainPage.fxml"));
                Parent root = fxmlLoader.load();

                // populate product cards
                MainPageController mainPageController = fxmlLoader.getController();
                mainPageController.populate(null); // null because no ActionEvent here

                vbox.getScene().setRoot(root);
            } else {
                // show backend error message if available
                if (responseBody != null && !responseBody.isEmpty()) {
                    errorLabel.setText(responseBody);
                    errorLabel.setOpacity(0); // instantly hide
                    errorLabel.setVisible(true);
                    fadeInError(errorLabel);
                } else {
                    // show error message
                    errorLabel.setText("Sign up failed. Please try again.");
                    errorLabel.setOpacity(0); // instantly hide
                    errorLabel.setVisible(true);
                    fadeInError(errorLabel);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel = (Label) vbox.lookup("#errorLabel");
            errorLabel.setText("Server error. Please try again later.");
            errorLabel.setOpacity(0); // instantly hide
            errorLabel.setVisible(true);
            fadeInError(errorLabel);
        }
    }

    private void autoLogin(String username, String password) {
        try {
            LoginRequest loginRequest = new LoginRequest(username, password);
            String json = HttpUtility.HTTP_UTILITY.getGson().toJson(loginRequest);

            RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url("http://localhost:8080/api/auth/login")
                    .post(body)
                    .build();

            Response response = HttpUtility.HTTP_UTILITY.getClient().newCall(request).execute();
            String responseBody = response.body().string();

            if (response.isSuccessful()) {
                AuthResponse authResponse = HttpUtility.HTTP_UTILITY.getGson().fromJson(responseBody, AuthResponse.class);
                SessionManager.setToken(authResponse.getToken());

                System.out.println("Auto-login successful! Token: " + SessionManager.getToken());
            } else {
                System.out.println("Auto-login failed after signup.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}