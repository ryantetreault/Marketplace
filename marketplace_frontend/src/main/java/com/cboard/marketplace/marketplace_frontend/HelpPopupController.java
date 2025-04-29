package com.cboard.marketplace.marketplace_frontend;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class HelpPopupController {
    @FXML
    private TextArea helpTextArea;

    @FXML
    private TextField searchField;

    private String fullHelpText;

    @FXML
    public void initialize() {
        // load help text
        fullHelpText = HelpContent.FULL_HELP_TEXT;
        helpTextArea.setText(fullHelpText);

        // searchbar typing
        searchField.textProperty().addListener((observable, oldValue, newValue) -> searchHelp(newValue));
    }

    public void setHelpText(String text) {
        this.fullHelpText = text;
        helpTextArea.setText(text);
    }

    public void closeHelp() {
        Stage stage = (Stage) helpTextArea.getScene().getWindow();
        stage.close();
    }

    private void searchHelp(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            helpTextArea.setText(fullHelpText);
            return;
        }
        String[] lines = fullHelpText.split("\n");
        StringBuilder result = new StringBuilder();
        for (String line : lines) {
            if (line.toLowerCase().contains(keyword.toLowerCase())) {
                result.append(line).append("\n");
            }
        }
        helpTextArea.setText(result.toString());
    }
}