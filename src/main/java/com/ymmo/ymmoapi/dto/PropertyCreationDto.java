package com.ymmo.ymmoapi.dto;

import jakarta.persistence.Column;

public class PropertyCreationDto {

    private String name;

    private Integer typeId;

    private Double price;

    private Double surfaceArea;

    private Integer roomCount;

    private String diagnostic;

    private String country;

    private String city;

    private String area;

    private Boolean onSale;

    public PropertyCreationDto(String name, Integer typeId, Double price, Double surfaceArea, Integer roomCount, String diagnostic, String country, String city, String area, Boolean onSale) {
        this.name = name;
        this.typeId = typeId;
        this.price = price;
        this.surfaceArea = surfaceArea;
        this.roomCount = roomCount;
        this.diagnostic = diagnostic;
        this.country = country;
        this.area = area;
        this.city = city;
        this.onSale = onSale;
    }

    public String getName() {
        return name;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public Double getPrice() {
        return price;
    }

    public Double getSurfaceArea() {
        return surfaceArea;
    }

    public Integer getRoomCount() {
        return roomCount;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getArea() {
        return area;
    }

    public Boolean getOnSale() {
        return onSale;
    }
}
