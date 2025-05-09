package com.cboard.marketplace.marketplace_frontend;

import com.cboard.marketplace.marketplace_common.ItemDto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProductCardSimpleController {
    @FXML
    private TextArea productTextArea;
    @FXML
    private TextField titleTextField;
    @FXML
    private VBox simpleCardVbox;
    @FXML
    private ImageView productImage;
    private ItemDto item = null;
    public void openProductCard(MouseEvent mouseEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("productCard.fxml"));

        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) ((Node) mouseEvent.getSource()).getScene().getWindow();
        ProductCardController Controller = fxmlLoader.getController();
        Controller.initializeProduct(this.item);
        stage.setScene(scene);
    }
    public void build(ItemDto itemDto)
    {
        this.item = itemDto;
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
        titleTextField.setText(itemDto.getName());
        productTextArea.appendText(itemDto.getDescription() + "\n");
        productTextArea.appendText("Cost: $" + itemDto.getPrice());
    }
    public double getCardWidth()
    {
        return this.simpleCardVbox.prefWidth(-1) + 20;
    }

}
