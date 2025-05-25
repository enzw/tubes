package com.tubes.model;

public class ZombieCharacter extends Character{
    public ZombieCharacter (String name, String description, String ttm){
        super(name, description, ttm);
    }

    @Override
    public void interact(){
        System.out.println(name + " : Grrz.., Saya Zombie...");
    }
}
