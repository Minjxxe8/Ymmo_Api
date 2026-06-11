package com.ymmo.ymmoapi.model;

public class TransactionsBuilder {
    private String description;
    private Users users;
    private double amount;
    private Properties properties;

    public TransactionsBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public TransactionsBuilder setUsers(Users users) {
        this.users = users;
        return this;
    }

    public TransactionsBuilder setAmount(double amount) {
        this.amount = amount;
        return this;
    }

    public TransactionsBuilder setProperties(Properties properties) {
        this.properties = properties;
        return this;
    }

    public Transactions build() {
        return new Transactions(description, users, amount, properties);
    }
}