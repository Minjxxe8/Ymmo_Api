package com.ymmo.ymmoapi.model;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity(name = "transactions")
public class Transactions {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "description")
    private String description;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "amount")
    private double amount;

    @Column(name = "property_id")
    private int propertyId;

    @Column(name = "created_at")
    private Timestamp createdAt;
}
