package com.cboard.marketplace.marketplace_common;

import jakarta.validation.constraints.NotNull;

public class ServiceDto extends ItemDto
{
    @NotNull(message = "Duration is required...")
    private Integer durationMinutes;

    public ServiceDto() {
    }

    public ServiceDto(int itemId, String name, String description, Double price, String category, String releaseDate, boolean available, String location, String itemType, String image_name, String image_type, byte[] image_date, Integer durationMinutes)
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
