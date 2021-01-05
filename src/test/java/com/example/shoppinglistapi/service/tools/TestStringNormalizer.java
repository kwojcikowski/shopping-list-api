package com.example.shoppinglistapi.service.tools;

import com.example.shoppinglistapi.service.tools.normalizer.Alphabet;
import com.example.shoppinglistapi.service.tools.normalizer.NormalizationException;
import com.example.shoppinglistapi.service.tools.normalizer.StringNormalizer;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

public class TestStringNormalizer {

    @Test
    public void testNormalizeSuccessfulOnPolishDialect(){
        String input = "ąćęłńóśźż and some string";
        String expected = "acelnoszz and some string";

        try {
            String actual = StringNormalizer.normalize(input, Alphabet.POLISH);
            assertThat(actual).isEqualTo(expected);
        }catch (NormalizationException e){
            fail("Exception should not have been thrown.");
        }
    }

    @Test
    public void testNormalizeThrowExceptionOnSpecialCharacters(){
        // Any character that is not a-z, not space and not in special dialect causes error
        String faultyInput = "some . string";
        try{
            StringNormalizer.normalize(faultyInput, Alphabet.POLISH);
            fail("AlphabetException should had been thrown on special character.");
        }catch (NormalizationException e){
            assertThat(e.getMessage()).isEqualTo("Given string '" + faultyInput
                    + "' does not match the alphabet " + Alphabet.POLISH.name());
        }
    }

    @Test
    public void testNormalizeThrowExceptionOnWrongDialectCharacter(){
        String faultyInput = "some Ĉ not polish string";
        try{
            StringNormalizer.normalize(faultyInput, Alphabet.POLISH);
            fail("AlphabetException should had been thrown on wrong dialect character.");

        }catch (NormalizationException e){
            assertThat(e.getMessage()).isEqualTo("Given string '" + faultyInput
                    + "' does not match the alphabet " + Alphabet.POLISH.name());
        }
    }

    @Test
    public void testNormalizeThrowExceptionOnEmptyString(){
        try{
            StringNormalizer.normalize("", Alphabet.POLISH);
            fail("AlphabetException should had been thrown on empty string.");
        }catch (NormalizationException e){
            assertThat(e.getMessage()).isEqualTo("Empty string supplied.");
        }
    }
}
