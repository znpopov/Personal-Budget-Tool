package com.kirilv.android.splitme.model;

public class Category {

    private long id;
    private String name;
    private int typeId;
    private String description;

    public Category(long id, String name, String description, int typeId) {
        this.id = id;
        this.name = name;
        this.typeId = typeId;
        this.description = description;
    }

    public Category(String name, String description, int typeId) {
        this.name = name;
        this.typeId = typeId;
        this.description = description;
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
}
