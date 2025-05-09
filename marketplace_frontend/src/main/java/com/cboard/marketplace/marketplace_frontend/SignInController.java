package com.cboard.marketplace.marketplace_frontend;

import javafx.application.Platform;
import javafx.fxml.FXML;

public class SignInController {
    @FXML
    private void closeProgram()
    {
        Platform.exit();
    }
}
