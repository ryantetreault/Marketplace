package com.cboard.marketplace.marketplace_frontend.Utility;

import javafx.scene.control.Alert;

public enum AlertUtility
{
    ALERT_UTILITY;

    public void showAlert(String title, String message)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
