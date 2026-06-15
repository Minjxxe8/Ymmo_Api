package com.ymmo.ymmoapi.model;

import jakarta.persistence.*;

@Entity(name = "property_types")
public class PropertyTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
