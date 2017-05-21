package com.kirilv.android.splitme.model;

public class Category {

    private String id;
    private String name;
    private String typeId;
    private String description;

    public Category(String id, String name, String description, String typeId) {
        this.id = id;
        this.name = name;
        this.typeId = typeId;
        this.description = description;
    }

    public Category(String name, String description, String typeId) {
        this.name = name;
        this.typeId = typeId;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return typeId;
    }

    public String getDescription() {
        return description;
    }
}
