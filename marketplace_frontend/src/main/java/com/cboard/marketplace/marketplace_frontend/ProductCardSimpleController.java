package com.cboard.marketplace.marketplace_frontend;

import com.cboard.marketplace.marketplace_common.ItemDto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class ProductCardSimpleController {
    @FXML
    private TextArea productTextArea;
    @FXML
    private TextField titleTextField;
    @FXML
    private VBox simpleCardVbox;
    public void openProductCard(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProductCardController.fxml"));
        Parent fxml = fxmlLoader.load();
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        userProfileController userProfileController = fxmlLoader.getController();
        stage.setScene(scene);
    }
    public void buildText(ItemDto itemDto)
    {
        titleTextField.setText(itemDto.getName());
        productTextArea.appendText(itemDto.getDescription() + "\n");
        productTextArea.appendText("Cost: " + itemDto.getPrice());
    }
    public double getCardWidth()
    {
        return this.simpleCardVbox.prefWidth(-1) + 20;
    }
}
