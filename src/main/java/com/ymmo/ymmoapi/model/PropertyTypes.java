package com.ymmo.ymmoapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity(name = "property_types")
public class PropertyTypes {
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;
}
