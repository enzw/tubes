package com.tubes.util;

import com.tubes.model.Character;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CharacterUtils {
    public static List<Character> filter(List<Character> characters, Predicate<Character> condition) {
        return characters.stream().filter(condition).collect(Collectors.toList());
    }
}