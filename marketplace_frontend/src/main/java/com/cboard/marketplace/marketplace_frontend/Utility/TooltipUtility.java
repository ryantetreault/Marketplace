package com.cboard.marketplace.marketplace_frontend.Utility;

import javafx.scene.Node;
import javafx.scene.control.Tooltip;

import java.util.HashMap;
import java.util.Map;

public enum TooltipUtility
{
    TOOLTIP_UTILITY;


    public static void attachTooltips(Map<Node, String> tooltipMap)
    {
        for (Map.Entry<Node, String> entry : tooltipMap.entrySet()) {
            if(entry.getKey() != null && entry.getValue() != null)
                Tooltip.install(entry.getKey(), new Tooltip(entry.getValue()));
        }
    }

    public static Map<Node, String> safeMap(Object... pairs) {
        Map<Node, String> map = new HashMap<>();
        for (int i = 0; i < pairs.length - 1; i += 2) {
            Object key = pairs[i];
            Object value = pairs[i + 1];

            if (key instanceof Node && value instanceof String && key != null && value != null) {
                map.put((Node) key, (String) value);
            }
        }
        return map;
    }
}
