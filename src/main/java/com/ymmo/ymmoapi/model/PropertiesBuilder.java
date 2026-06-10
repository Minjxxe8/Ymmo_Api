package com.ymmo.ymmoapi.model;

public class PropertiesBuilder {

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

    public PropertiesBuilder name(String name) {
        this.name = name;
        return this;
    }

    public PropertiesBuilder typeId(Integer typeId) {
        this.typeId = typeId;
        return this;
    }

    public PropertiesBuilder price(Double price) {
        this.price = price;
        return this;
    }

    public PropertiesBuilder surfaceArea(Double surfaceArea) {
        this.surfaceArea = surfaceArea;
        return this;
    }

    public PropertiesBuilder roomCount(Integer roomCount) {
        this.roomCount = roomCount;
        return this;
    }

    public PropertiesBuilder diagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
        return this;
    }

    public PropertiesBuilder country(String country) {
        this.country = country;
        return this;
    }

    public PropertiesBuilder city(String city) {
        this.city = city;
        return this;
    }

    public PropertiesBuilder area(String area) {
        this.area = area;
        return this;
    }

    public PropertiesBuilder onSale(Boolean onSale) {
        this.onSale = onSale;
        return this;
    }

    public Properties build() {
        Properties properties = new Properties();
        properties.setName(name);
        properties.setTypeId(typeId);
        properties.setPrice(price);
        properties.setSurfaceArea(surfaceArea);
        properties.setRoomCount(roomCount);
        properties.setDiagnostic(diagnostic);
        properties.setCountry(country);
        properties.setCity(city);
        properties.setArea(area);
        properties.setOnSale(onSale);
        return properties;
    }
}