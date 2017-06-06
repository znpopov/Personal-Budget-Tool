package com.kirilv.android.splitme.model;

public class Transaction {
    private int id;
    private int categoryId;
    private int typeId;
    private double amount;
    private long createdAt;

    public Transaction(int categoryId, int typeId, double amount) {
        this.categoryId = categoryId;
        this.typeId = typeId;
        this.amount = amount;
        createdAt = System.currentTimeMillis()/1000L;
    }

    public Transaction(int id, int categoryId, int typeId, double amount) {
        this.id = id;
        this.typeId = typeId;
        this.amount = amount;
        createdAt = System.currentTimeMillis()/1000L;
    }

    public int getId(){
        return id;
    }

    public int getCategoryId(){
        return categoryId;
    }

    public int getTypeId(){
        return typeId;
    }

    public double getAmount() {
        return amount;
    }

    public long getCreatedAt(){
        return createdAt;
    }
}
