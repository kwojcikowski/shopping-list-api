package com.example.shoppinglistapi.model.unit;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UnitTest {

    @ParameterizedTest(name = "{0}")
    @MethodSource("getUnitAbbreviations")
    public void testUnitFromAbbreviation(String correctAbbreviation) {
        assertDoesNotThrow(
                () -> Unit.fromAbbreviation(correctAbbreviation),
                "No Exception should be thrown on parsing correct abbreviation."
        );
    }

    public static Stream<String> getUnitAbbreviations() {
        return Arrays.stream(Unit.values()).map(Unit::toString);
    }

    @Test
    public void testUnitFromAbbreviationThrowExceptionOnNonExistingAbbreviation() {
        Exception expectedException = assertThrows(
                EntityNotFoundException.class,
                () -> Unit.fromAbbreviation("non-existing-abbreviation"),
                "EntityNotFoundException should be thrown on parsing non existing abbreviation."
        );
        assertThat(expectedException.getMessage()).isEqualTo("Unable to find unit with provided abbreviation.");
    }
}
