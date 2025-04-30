package com.cboard.marketplace.marketplace_frontend;

import com.cboard.marketplace.marketplace_common.dto.AuthResponse;
import com.cboard.marketplace.marketplace_common.dto.LoginRequest;
import com.cboard.marketplace.marketplace_frontend.Utility.HttpUtility;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.cboard.marketplace.marketplace_frontend.Utility.StageUtility.STAGE_UTILITY;

public class LoginController implements Initializable {
    @FXML
    private Label welcomeText;
    @FXML
    private VBox vbox;
    @FXML
    private Pane pane;

    private void fadeInError(Label errorLabel) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), errorLabel);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
            try {
                Parent fxml = FXMLLoader.load(getClass().getResource("signUp.fxml"));

                vbox.getChildren().setAll(fxml);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
    }

    @FXML
    public void openSignUp(javafx.event.ActionEvent event) {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vbox);
        t.setToX(0);

        t.setOnFinished((e) -> {
            try {
                Parent fxml = FXMLLoader.load(getClass().getResource("signUp.fxml"));
                vbox.getChildren().removeAll();
                vbox.getChildren().setAll(fxml);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        t.play();
    }
    @FXML
    public void openSignIn(javafx.event.ActionEvent event) {
        TranslateTransition t = new TranslateTransition(Duration.seconds(1), vbox);
        t.setToX(pane.getWidth() - vbox.getWidth() - 50);

        t.setOnFinished((e) -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("signIn.fxml"));
                Parent fxml = loader.load();

                Button loginButton = (Button) fxml.lookup("#loginButton");
                loginButton.setOnAction(ev -> handleLoginButtonAction(event));

                vbox.getChildren().setAll(fxml);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        t.play();
    }

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    @FXML
    public void handleLoginButtonAction(ActionEvent event) {
        // find fields inside the vbox
        TextField usernameField = (TextField) vbox.lookup("#usernameField");
        TextField passwordField = (TextField) vbox.lookup("#passwordField");
        Label errorLabel = (Label) vbox.lookup("#errorLabel");

        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username and password cannot be empty.");
            errorLabel.setOpacity(0); //instantly hide
            errorLabel.setVisible(true);
            fadeInError(errorLabel);
            return;
        }

        try {
            // make JSON body
            LoginRequest loginRequest = new LoginRequest(username, password);
            String json = HttpUtility.HTTP_UTILITY.getGson().toJson(loginRequest);

            // build HTTP POST request to backend
            RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url("http://localhost:8080/api/auth/login")
                    .post(body)
                    .build();
//            // debugging
//            errorLabel.setText("endpoint reached");
//            errorLabel.setOpacity(0); // instantly hide
//            errorLabel.setVisible(true);
//            fadeInError(errorLabel);

            // execute request
            Response response = HttpUtility.HTTP_UTILITY.getClient().newCall(request).execute();
            String responseBody = response.body().string();
            int responseCode = response.code();

            System.out.println("HTTP response code: " + responseCode);
            System.out.println("HTTP response body: " + responseBody);

            if (response.isSuccessful()) {
                // parse the backend response (contains the token)
                AuthResponse authResponse = HttpUtility.HTTP_UTILITY.getGson().fromJson(responseBody, AuthResponse.class);
                SessionManager.setToken(authResponse.getToken());

                // successful login message
                errorLabel.setText("Login successful!");
                errorLabel.setTextFill(Color.GREEN);
                errorLabel.setOpacity(0); // instantly hide
                errorLabel.setVisible(true);
                fadeInError(errorLabel);

                System.out.println("Login successful! Token: " + SessionManager.getToken());

                switchToMain(event);

                /*// load mainPage.fxml
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mainPage.fxml"));
                Parent root = fxmlLoader.load();

                // populate product cards
                MainPageController mainPageController = fxmlLoader.getController();
                mainPageController.populate(null); // null because no ActionEvent here

                vbox.getScene().setRoot(root);*/
            } else {
                // show login error message
                errorLabel.setText("Login failed. Please check your credentials.");
                errorLabel.setOpacity(0); // instantly hide
                errorLabel.setVisible(true);
                fadeInError(errorLabel);
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

    private void switchToMain(ActionEvent event) throws IOException
    {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("mainPage.fxml"));
        Parent root = loader.load();

        MainPageController controller = loader.getController();
        //controller.someFuncToPassDataToNextSceneHere();

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
        STAGE_UTILITY.switchStage(stage);
    }
}