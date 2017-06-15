package com.kirilv.android.splitme.model;

public class Category {

    private long id;
    private String name;
    private int typeId;
    private String description;
    private int budget;

    public Category(long id, String name, String description, int typeId, int budget) {
        this.id = id;
        this.name = name;
        this.typeId = typeId;
        this.description = description;
        this.budget = budget;
    }

    public Category(String name, String description, int typeId, int budget) {
        this.name = name;
        this.typeId = typeId;
        this.description = description;
        this.budget = budget;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return typeId;
    }

    public String getDescription() {
        return description;
    }

    public int getBudget() { return budget; }

    public void setBudget(int budget) { this.budget = budget; }
}
