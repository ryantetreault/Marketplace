package com.cboard.marketplace.marketplace_frontend.Utility;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;

import java.util.Map;

public enum TooltipUtility
{
    TOOLTIP_UTILITY;


    public static void attachTooltips(Map<Node, String> tooltipMap)
    {
        for (Map.Entry<Node, String> entry : tooltipMap.entrySet()) {
            Tooltip.install(entry.getKey(), new Tooltip(entry.getValue()));
        }
    }
}
