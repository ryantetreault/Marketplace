package com.cboard.marketplace.marketplace_frontend;

import java.util.Base64;

public class SessionManager {
    private static String token;

    public static void setToken(String t) {
        token = t;
    }

    public static String getToken() {
        return token;
    }

    public static Integer getUserIdFromToken() {
        try {
            String[] parts = token.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            int idIndex = payload.indexOf("\"id\":");
            if (idIndex == -1) return null;
            int start = idIndex + 5;
            int end = payload.indexOf(",", start);
            if (end == -1) end = payload.indexOf("}", start);
            return Integer.parseInt(payload.substring(start, end).trim());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}