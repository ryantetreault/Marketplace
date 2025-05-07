package com.cboard.marketplace.marketplace_frontend.Utility;

public enum CategoryUtility {
    ALL("All"),
    PRODUCTS("Product"),
    SERVICES("Service"),
    REQUESTS("Request");
    String value;
    CategoryUtility(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        if(value == "All")
        return value;
        else return value + "s";
    }
    public String getValue() {
        return value;
    }
}
