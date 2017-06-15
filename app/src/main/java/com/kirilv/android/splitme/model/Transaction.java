package com.kirilv.android.splitme.model;

import java.util.Date;

public class Transaction {
    private long id;
    private String name;
    private long categoryId;
    private int typeId; // 1 for Expenses, 2 for Incomes and 3 for Savings
    private double amount;
    private long createdAt;
    private Date date;

    public Transaction(String name, long categoryId, int typeId, double amount, Date date) {
        this.name = name;
        this.categoryId = categoryId;
        this.typeId = typeId;
        this.amount = amount;
        this.date = date;
        createdAt = System.currentTimeMillis() / 1000L;
    }

    public Transaction(long id, String name, long categoryId, int typeId, double amount) {
        this.id = id;
        this.name = name;
        this.typeId = typeId;
        this.amount = amount;
        createdAt = System.currentTimeMillis() / 1000L;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public int getTypeId() {
        return typeId;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public long getCreatedAt() {
        return createdAt;
    }
}
