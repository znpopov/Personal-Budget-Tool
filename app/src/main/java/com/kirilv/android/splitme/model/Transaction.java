package com.kirilv.android.splitme.model;

public class Transaction {
    private long id;
    private int categoryId;
    private int typeId; // 1 for Expenses, 2 for Incomes and 3 for Savings
    private double amount;
    private long createdAt;

    public Transaction(int categoryId, int typeId, double amount) {
        this.categoryId = categoryId;
        this.typeId = typeId;
        this.amount = amount;
        createdAt = System.currentTimeMillis()/1000L;
    }

    public Transaction(long id, int categoryId, int typeId, double amount) {
        this.id = id;
        this.typeId = typeId;
        this.amount = amount;
        createdAt = System.currentTimeMillis()/1000L;
    }

    public long getId(){
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
