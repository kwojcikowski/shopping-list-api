package com.example.antonapi.service.tools.normalizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


interface BasicAlphabet{
    List<Character> BASIC_LETTERS = "abcdefghijklmnopqrstuvwxyz "
            .chars().mapToObj(c -> (char) c).collect(Collectors.toList());
    boolean matchesString(String string);
    Character normalizeCharacter(Character c);
}

public enum Alphabet implements BasicAlphabet{
    POLISH(Map.of(
            'ą', 'a',
            'ć', 'c',
            'ę', 'e',
            'ł', 'l',
            'ń', 'n',
            'ó', 'o',
            'ś', 's',
            'ź', 'z',
            'ż', 'z'
    ));

    public Map<Character, Character> dialectReplacements;
    public List<Character> specialCharacters;

    Alphabet(Map<Character, Character> dialectReplacements){
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
