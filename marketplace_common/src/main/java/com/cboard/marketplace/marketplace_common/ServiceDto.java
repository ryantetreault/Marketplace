package com.cboard.marketplace.marketplace_common;

public class ServiceDto extends ItemDto
{
    private int durationMinutes;

    public ServiceDto() {
    }

    public ServiceDto(int itemId, String name, String description, double price, String category, String releaseDate, boolean available, String location, String itemType, String image_name, String image_type, byte[] image_date, int durationMinutes)
    {
        super(itemId, name, description, price, category, releaseDate, available, location, itemType, image_name, image_type, image_date);
        this.durationMinutes = durationMinutes;
    }

    public int getDurationMinutes()
    {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes)
    {
        this.durationMinutes = durationMinutes;
    }

}
