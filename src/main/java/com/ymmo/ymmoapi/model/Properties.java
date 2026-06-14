package com.ymmo.ymmoapi.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity(name = "properties")
public class Properties {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @OneToOne
    @JoinColumn(name = "type_id")
    private PropertyTypes type;

    @Column(name = "price")
    private Double price;

    @Column(name = "surface_area")
    private Double surfaceArea;

    @Column(name = "room_count")
    private Integer roomCount;

    @Column(name = "diagnostic")
    private String diagnostic;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "area")
    private String area;

    @Column(name = "on_sale")
    private Boolean onSale;

    @Column(name = "created_at", updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    public Properties() {

    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public PropertyTypes getType() {
        return type;
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

    public Boolean isOnSale() {
        return onSale;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(PropertyTypes type) {
        this.type = type;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setSurfaceArea(Double surfaceArea) {
        this.surfaceArea = surfaceArea;
    }

    public void setRoomCount(Integer roomCount) {
        this.roomCount = roomCount;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public void setOnSale(Boolean onSale) {
        this.onSale = onSale;
    }

    public Properties(String name, PropertyTypes type, Double price, Double surfaceArea, Integer roomCount, String diagnostic, String country, String city, String area, Boolean onSale) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.surfaceArea = surfaceArea;
        this.roomCount = roomCount;
        this.diagnostic = diagnostic;
        this.country = country;
        this.city = city;
        this.area = area;
        this.onSale = onSale;
    }
}
