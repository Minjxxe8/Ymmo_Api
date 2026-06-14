package com.ymmo.ymmoapi.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity(name = "property_picture")
public class PropertyPicture {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "path")
    private String path;

    @OneToOne
    @JoinColumn(name = "property_id")
    private Properties property;

    @Column(name = "created_at", updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp created_at;

    public PropertyPicture() {

    }

    public PropertyPicture(String path, Properties property) {
        this.path = path;
        this.property = property;
    }

    public String getPath() {
        return path;
    }

    public Properties getProperty() {
        return property;
    }
}
