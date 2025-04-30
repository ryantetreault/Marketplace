package com.cboard.marketplace.marketplace_frontend.Utility;


import javafx.stage.Stage;

public enum StageUtility
{
    STAGE_UTILITY;

    private static final Integer STAGE_WIDTH = 1100;
    private static final Integer STAGE_HEIGHT = 750;

    public static void switchStage(Stage stage)
    {
        stage.setResizable(false);
        stage.setWidth(STAGE_WIDTH);
        stage.setHeight(STAGE_HEIGHT);
        stage.show();
    }
}
