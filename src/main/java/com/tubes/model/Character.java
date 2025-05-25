package com.tubes.model;

public abstract class Character {
    protected String name;
    protected String description;
    protected String ttm;

    public Character(String name, String description, String ttm) {
        this.name = name;
        this.description = description;
        this.ttm = ttm;
    }

    public abstract void interact();

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getTtm() { return ttm; }
}
