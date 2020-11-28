package com.example.antonapi.service.tools.normalizer;

import java.util.stream.Collectors;

public class StringNormalizer {

    public static String normalize(String input, Alphabet language) throws NormalizationException {
        if(input.isEmpty())
            throw new NormalizationException("Empty string supplied.");
        String loweredInput = input.toLowerCase().strip();
        if(!language.matchesString(loweredInput)) throw new NormalizationException("Given string '" + input
                + "' does not match the alphabet " + language.name());
        return loweredInput.chars()
                .mapToObj(c -> (char) c)
                .map(language::normalizeCharacter)
                .map(Object::toString)
                .collect(Collectors.joining());
    }

}
