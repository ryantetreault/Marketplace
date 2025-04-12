package com.cboard.marketplace.marketplace_common;

public class RequestDto extends ItemDto
{
    private String deadline;

    public RequestDto() {
    }



    public RequestDto(int itemId, String name, String description, double price, String category, String releaseDate, boolean available, String location, String itemType, String image_name, String image_type, byte[] image_date, String deadline)
    {
        super(itemId, name, description, price, category, releaseDate, available, location, itemType, image_name, image_type, image_date);
        this.deadline = deadline;
    }


    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
