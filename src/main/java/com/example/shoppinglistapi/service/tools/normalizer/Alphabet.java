package com.example.shoppinglistapi.service.tools.normalizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public enum Alphabet implements BasicAlphabet {
    POLISH(Map.ofEntries(
            Map.entry('ą', 'a'),
            Map.entry('ć', 'c'),
            Map.entry('ę', 'e'),
            Map.entry('ł', 'l'),
            Map.entry('ń', 'n'),
            Map.entry('ó', 'o'),
            Map.entry('ś', 's'),
            Map.entry('ź', 'z'),
            Map.entry('ż', 'z'),
            Map.entry('Ą', 'A'),
            Map.entry('Ę', 'E'),
            Map.entry('Ł', 'L'),
            Map.entry('Ń', 'N'),
            Map.entry('Ó', 'O'),
            Map.entry('Ś', 'S'),
            Map.entry('Ź', 'Z'),
            Map.entry('Ż', 'Z')
    ));

    public Map<Character, Character> dialectReplacements;
    public List<Character> specialCharacters;

    Alphabet(Map<Character, Character> dialectReplacements) {
        this.dialectReplacements = dialectReplacements;
        this.specialCharacters = new ArrayList<>(dialectReplacements.keySet());
    }

    @Override
    public boolean matchesString(String string) {
        return string.chars().mapToObj(c -> (char) c)
                .allMatch(c -> BASIC_LETTERS.contains(c) || specialCharacters.contains(c));
    }

    @Override
    public Character normalizeCharacter(Character c) {
        return dialectReplacements.getOrDefault(c, c);
    }
}

interface BasicAlphabet {
    List<Character> BASIC_LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ "
            .chars().mapToObj(c -> (char) c).collect(Collectors.toList());

    boolean matchesString(String string);

    Character normalizeCharacter(Character c);
}
