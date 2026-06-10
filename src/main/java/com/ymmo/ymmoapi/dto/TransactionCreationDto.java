package com.ymmo.ymmoapi.dto;

public class TransactionCreationDto {
    private String description;

    private int userId;

    private double amount;

    private int propertyId;

    public String getDescription() {
        return description;
    }

    public int getUserId() {
        return userId;
    }

    public double getAmount() {
        return amount;
    }

    public int getPropertyId() {
        return propertyId;
    }
}
