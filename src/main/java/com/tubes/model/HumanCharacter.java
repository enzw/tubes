package com.tubes.model;

public class HumanCharacter extends Character{
    public HumanCharacter(String name, String description, String ttm) {
        super(name, description, ttm);
    }

    @Override
    public void interact() {
        System.out.println(name + ": ..., Geerrz, umh.. Aku Zombie");
    }
}
