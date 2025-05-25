package com.tubes.model;

public abstract class Character {
    protected String name;
    protected String description;
    protected String lantai;

    public Character(String name, String description, String lantai) {
        this.name = name;
        this.description = description;
        this.lantai = lantai;
    }

    public abstract void interact();

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getTtm() { return lantai; }
}
