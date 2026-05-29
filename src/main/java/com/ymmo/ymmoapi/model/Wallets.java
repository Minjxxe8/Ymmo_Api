package com.ymmo.ymmoapi.model;

import jakarta.persistence.*;


@Entity(name = "wallets")
public class Wallets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "balance")
    private double balance;
}
