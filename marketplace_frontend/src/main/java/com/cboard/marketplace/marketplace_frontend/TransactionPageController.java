package com.cboard.marketplace.marketplace_frontend;

import com.cboard.marketplace.marketplace_common.TransactionDto;
import com.cboard.marketplace.marketplace_frontend.Utility.TransactionRenderer;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import okhttp3.Request;
import okhttp3.Response;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.cboard.marketplace.marketplace_frontend.Utility.HttpUtility.HTTP_UTILITY;
import static com.cboard.marketplace.marketplace_frontend.Utility.AlertUtility.ALERT_UTILITY;
import static com.cboard.marketplace.marketplace_frontend.Utility.StageUtility.STAGE_UTILITY;
import static com.cboard.marketplace.marketplace_frontend.Utility.TooltipUtility.TOOLTIP_UTILITY;

public class TransactionPageController
{
    private List<TransactionDto> transactions = new ArrayList<>();
    @FXML
    ScrollPane transactionContainer;
    @FXML
    VBox transactionListVBox;
    @FXML
    TextField searchField;
    @FXML
    FontIcon profileBtn;
    @FXML
    FontIcon transactionBtn;
    @FXML
    FontIcon helpBtn;
    @FXML
    FontIcon backBtn;

    public void initialize()
    {
        TOOLTIP_UTILITY.attachTooltips(Map.of(
                profileBtn, "Profile",
                transactionBtn, "Transactions",
                helpBtn, "Help",
                backBtn, "Home"
        ));

        transactions = getAllTransactions();

        transactionListVBox = new VBox(10);
        transactionListVBox.setPadding(new Insets(10));
        transactionContainer.setContent(transactionListVBox);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            List<TransactionDto> filteredTransactions = TransactionRenderer.filterTransactionsBySearch(transactions, newValue);
            transactionListVBox.getChildren().clear(); // Clear old results
            TransactionRenderer.displayTransactions(transactionListVBox, transactionContainer, transactions, filteredTransactions);
        });


        TransactionRenderer.displayTransactions(transactionListVBox, transactionContainer, transactions, transactions);
    }

    private List<TransactionDto> getAllTransactions()
    {
        Request request = new Request.Builder()
                .url("http://localhost:8080/transaction/all")
                .get()
                .addHeader("Authorization", "Bearer " + SessionManager.getToken())
                .build();

        try(Response response = HTTP_UTILITY.getClient().newCall(request).execute())
        {
            if(response.isSuccessful() && response.body() != null)
            {
                String responseBody = response.body().string();
                transactions = HTTP_UTILITY.getObjMapper().readValue(responseBody, new TypeReference<List<TransactionDto>>() {} ) ;
            }
            else
            {
                System.out.println("Server" + response.code());
                ALERT_UTILITY.showAlert("ERROR", "Error occurred...");
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return transactions;
    }


    public void profileClicked(MouseEvent mouseEvent) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(SignUpController.class.getResource("newUserProfile.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        userProfileController userProfileController = fxmlLoader.getController();
        //userProfileController.someFuncToPassDataToNextSceneHere();


        stage.setScene(scene);

        STAGE_UTILITY.switchStage(stage);
    }

    public void handleBack(MouseEvent event)
    {
        try
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
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    @FXML
    public void openHelpPopup() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("HelpPopup.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(loader.load()));
        stage.show();
    }



}
