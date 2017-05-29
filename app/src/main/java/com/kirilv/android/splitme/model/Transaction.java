package com.kirilv.android.splitme.model;

/**
 * Created by zdravkop on 5/29/2017.
 */

public class Transaction {

    private int id;
    private String name;
    private int typeId; // 1 for income, 2 for expense
    private int categoryId;

    public Transaction(int id, String name, int typeId, int categoryId) {
        this.id = id;
        this.name = name;
        this.typeId = typeId;
        this.categoryId = categoryId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) { this.name = name; }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) { this.typeId = typeId; }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
}
