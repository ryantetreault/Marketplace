package com.cboard.marketplace.marketplace_frontend;
import com.cboard.marketplace.marketplace_common.ItemDto;
import com.cboard.marketplace.marketplace_frontend.Utility.CategoryUtility;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.animation.PauseTransition;
import javafx.util.Duration;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import okhttp3.Request;
import okhttp3.Response;
import org.kordamp.ikonli.javafx.FontIcon;
import static com.cboard.marketplace.marketplace_frontend.Utility.AlertUtility.ALERT_UTILITY;
import static com.cboard.marketplace.marketplace_frontend.Utility.HttpUtility.HTTP_UTILITY;
import static com.cboard.marketplace.marketplace_frontend.Utility.StageUtility.STAGE_UTILITY;
import static com.cboard.marketplace.marketplace_frontend.Utility.TooltipUtility.TOOLTIP_UTILITY;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainPageController {
    @FXML
    ScrollPane scrollPane = new ScrollPane();
    @FXML
    FontIcon profileBtn;
    @FXML
    FontIcon transactionBtn;
    @FXML
    FontIcon helpBtn;
    @FXML
    FontIcon backBtn;
    @FXML
    FontIcon newListingBtn;
    @FXML
    TextField searchTextField;
    @FXML
    ChoiceBox<CategoryUtility> searchChoiceBox;

    private List<ItemDto> items = new ArrayList<>();
    private List<ItemDto> masterItemsList = new ArrayList<>();
    private final GridPane gridPane = new GridPane();

    private int numRow = 0;
    private double cardSize;

    public void initialize() {
        //Run after so it doesn't freak when getting the size of the cards
        Platform.runLater(() -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("productCardSimple.fxml"));
                Parent root = fxmlLoader.load();
                ProductCardSimpleController controller = fxmlLoader.getController();
                this.cardSize = controller.getCardWidth();
                //Set the rows at the start, set the master items list, display
                this.numRow = (int)(scrollPane.getPrefWidth() / this.cardSize);
                this.items = this.getAllItems();
                this.masterItemsList = this.items;
                populate();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        this.setGridViewBuffer(gridPane, new Insets(10));
        TOOLTIP_UTILITY.attachTooltips(
                TOOLTIP_UTILITY.safeMap(
                        newListingBtn, "List New",
                        profileBtn, "Profile",
                        transactionBtn, "Transactions",
                        helpBtn, "Help",
                        backBtn, "Sign Out"
                )
        );

        //For each category in the Utility, add to search box
        for(CategoryUtility category : CategoryUtility.values()) {
            searchChoiceBox.getItems().add(category);
        }

        //Prime the choice box to "All", add the grid to the scroll pane
        searchChoiceBox.setValue(CategoryUtility.ALL);
        this.scrollPane.setContent(gridPane);

        //Add listener to Search text field
        searchTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            //Grab the master list, filter out items that match the new name, then grab all if the Category is "all" or if it matches the item type from the choice box
            this.items = this.masterItemsList.stream()
                    .filter(itemDto -> itemDto.getName().toLowerCase().contains(newValue.toLowerCase()))
                    .filter(itemDto -> this.searchChoiceBox.getValue().equals(CategoryUtility.ALL) || itemDto.getItemType().equalsIgnoreCase(searchChoiceBox.getValue().getValue()))
                    .toList();
            try {
                this.populate();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        //Add listener to Search choices
        searchChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            //Grab the master list, filter out items that match name, then grab everything if the new category is "all" or if the new value matches the item type
            this.items = this.masterItemsList.stream()
                    .filter(itemDto -> itemDto.getName().toLowerCase().contains(this.searchTextField.getText().toLowerCase()))
                    .filter(itemDto -> newValue.equals(CategoryUtility.ALL) || itemDto.getItemType().equalsIgnoreCase(newValue.getValue()))
                    .toList();
            try {
                this.populate();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        //Pause transition to avoid excessive updates
        PauseTransition pause = new PauseTransition(Duration.millis(200));
        //Add a listener to the scroll panes width
        scrollPane.widthProperty().addListener((observable, oldValue, newValue) -> {
            //Calculate the new row length that would result from this new width
            int newRowLength = (int)(((double)newValue) / this.cardSize);
            //If it is the same, it has not changed enough to repopulate / cant be less than 1 per row
            if(newRowLength == this.numRow || newRowLength == 0)
                return;
            //update the rows if changed, pause and then play after delay
            this.numRow = newRowLength;
            pause.stop();
            pause.setOnFinished(e -> {

                try {
                    populate();
                } catch (IOException o) {
                    throw new RuntimeException(o);
                }

            });
            pause.play();
        });
    }

    //Takes the current items and displays them
    public void populate() throws IOException {
        //Clear and set the row and column
        this.gridPane.getChildren().clear();
        int Row = 1;
        int Column = 1;
        //For each item make a new card and add it to the next spot
        for (ItemDto itemDto : this.items)
        {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("productCardSimple.fxml"));
            Parent FXML = fxmlLoader.load();
            ProductCardSimpleController controller = fxmlLoader.getController();
            //Build text from item info
            controller.buildText(itemDto);
            this.gridPane.add(FXML, (Row), Column);
            //Use the numRow variable. Calculated base off the size of the stage and the size of the cards
            //If Row at max, start next
            if(((Row) == this.numRow))
            {
                Column++;
                Row = 0;
            }
            Row++;
        }
        //Margins
        setGridViewBuffer(gridPane, new Insets(10));
    }
    //OUTDATED
    public void profileClicked(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SignUpController.class.getResource("userProfile.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        userProfileController userProfileController = fxmlLoader.getController();
        stage.setScene(scene);

        STAGE_UTILITY.switchStage(stage);
    }

    public void newProfileClicked(MouseEvent event) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("newUserProfile.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        userProfileController userProfileController = fxmlLoader.getController();

        stage.setScene(scene);

        STAGE_UTILITY.switchStage(stage);
    }

    public void handleBack(MouseEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            stage.show();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void transactionsClicked(MouseEvent event)
    {
        try
        {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("transactionPage.fxml"));
            Parent root = loader.load();

            TransactionPageController controller = loader.getController();
            //controller.someFuncToPassDataToNextSceneHere();
            //controller.setPage();

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

    @FXML
    public void handleNewListing(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("listNew.fxml"));
        Parent root = loader.load();

        ListNewController controller = loader.getController();
        controller.setUserId(SessionManager.getUserIdFromToken());

        Scene scene = ((Node) event.getSource()).getScene();
        scene.setRoot(root);
    }

    //Get all the items from Backend
    private List<ItemDto> getAllItems()
    {
        //Make a request
        Request request = new Request.Builder()
                .url("http://localhost:8080/item/all")
                .get()
                .addHeader("Authorization", "Bearer " + SessionManager.getToken())
                .build();

        try(Response response = HTTP_UTILITY.getClient().newCall(request).execute())
        {
            //If successful put values into master list
            if(response.isSuccessful() && response.body() != null)
            {
                String responseBody = response.body().string();
                this.masterItemsList = HTTP_UTILITY.getObjMapper().readValue(responseBody, new TypeReference<List<ItemDto>>() {} ) ;
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
        return this.masterItemsList;
    }
    
    //Take a margin and set for nodes to add space
    private void setGridViewBuffer(GridPane gridPane, Insets margin)
    {
        gridPane.getChildren().forEach(node -> GridPane.setMargin(node, margin));
    }
}