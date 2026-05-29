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

    @Column(name = "type_id")
    private int typeId;

    @Column(name = "price")
    private double price;

    @Column(name = "surface_area")
    private double surfaceArea;

    @Column(name = "room_count")
    private int roomCount;

    @Column(name = "diagnostic")
    private String diagnostic;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "on_sale")
    private boolean onSale;

    @Column(name = "created_at")
    private Timestamp createdAt;
}
