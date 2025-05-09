package com.cboard.marketplace.marketplace_frontend.Utility;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import javafx.event.ActionEvent;

import java.io.IOException;

public class WelcomePopupRenderer {
    private final String fxmlPath;
    private final Duration fadeInDuration = Duration.seconds(0.8);
    private final Duration visibleDuration = Duration.seconds(1.8);
    private final Duration fadeOutDuration = Duration.seconds(0.8);

    public WelcomePopupRenderer(String fxmlPath) {
        this.fxmlPath = fxmlPath;
    }

    public void show(ActionEvent event, Runnable onComplete) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            Stage popupStage = new Stage();
            popupStage.initStyle(StageStyle.TRANSPARENT);
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.initOwner(((javafx.scene.Node) event.getSource()).getScene().getWindow());

            Scene scene = new Scene(root);
            scene.setFill(null);
            popupStage.setScene(scene);

            FadeTransition fadeIn = new FadeTransition(fadeInDuration, root);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            FadeTransition fadeOut = new FadeTransition(fadeOutDuration, root);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setDelay(visibleDuration);

            fadeOut.setOnFinished(e -> {
                popupStage.close();
                if (onComplete != null) {
                    onComplete.run();
                }
            });

            fadeIn.setOnFinished(e -> fadeOut.play());

            popupStage.show();
            fadeIn.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
