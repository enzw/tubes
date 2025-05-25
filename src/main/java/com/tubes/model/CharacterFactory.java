package com.tubes.model;

public class CharacterFactory {
    public static Character createCharacter(String type, String name, String description, String ttm) {
    switch (type) {
        case "zombie":
            return new ZombieCharacter(name, description, ttm);
        case "human":
            return new HumanCharacter(name, description, ttm);
        default:
            throw new IllegalArgumentException("Unknown character type");
    }
}

}
