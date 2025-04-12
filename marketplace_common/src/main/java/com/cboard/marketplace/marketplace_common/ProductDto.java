package com.cboard.marketplace.marketplace_common;

public class ProductDto extends ItemDto
{
    private int quantity;
    private String brand;

    public ProductDto() {
    }

    public ProductDto(int itemId, String name, String description, double price, String category, String releaseDate, boolean available, String location, String itemType, String image_name, String image_type, byte[] image_date, int quantity, String brand)
    {
        super(itemId, name, description, price, category, releaseDate, available, location, itemType, image_name, image_type, image_date);
        this.quantity = quantity;
        this.brand = brand;
    }


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
