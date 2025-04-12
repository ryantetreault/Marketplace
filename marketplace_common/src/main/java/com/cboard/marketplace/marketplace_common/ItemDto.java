package com.cboard.marketplace.marketplace_common;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "itemType",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = ProductDto.class, name = "product"),
        @JsonSubTypes.Type(value = ServiceDto.class, name = "service"),
        @JsonSubTypes.Type(value = RequestDto.class, name = "service")
})


public abstract class ItemDto
{
    private int itemId;
    private String name;
    private String description;
    private double price;
    private String category;
    private String releaseDate;
    private boolean available;
    private String location;
    private String itemType;
    private String image_name;
    private String image_type;
    private byte[] image_date;

    public ItemDto() {
    }

    public ItemDto(int itemId, String name, String description, double price, String category, String releaseDate, boolean available, String location, String itemType, String image_name, String image_type, byte[] image_date) {
        this.itemId = itemId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.releaseDate = releaseDate;
        this.available = available;
        this.location = location;
        this.itemType = itemType;
        this.image_name = image_name;
        this.image_type = image_type;
        this.image_date = image_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getImage_type() {
        return image_type;
    }

    public void setImage_type(String image_type) {
        this.image_type = image_type;
    }

    public byte[] getImage_date() {
        return image_date;
    }

    public void setImage_date(byte[] image_date) {
        this.image_date = image_date;
    }
}
