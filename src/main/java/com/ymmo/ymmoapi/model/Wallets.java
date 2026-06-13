package com.ymmo.ymmoapi.model;

import com.ymmo.ymmoapi.exception.ResponseException;
import jakarta.persistence.*;


@Entity(name = "wallets")
public class Wallets {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(name = "balance")
    private double balance;

    public Wallets() {

    }

    public Wallets(Users user, double balance) {
        this.user = user;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public Users getUser() {
        return user;
    }

    public double getBalance() {
        return balance;
    }

    public void addBalance(double balance) {
        this.balance += balance;
    }

    public void removeBalance(double balance) {
        if (this.balance - balance < 0) {
            throw new ResponseException("Your wallet balance cannot be under 0", 400);
        }
        this.balance -= balance;
    }
}
