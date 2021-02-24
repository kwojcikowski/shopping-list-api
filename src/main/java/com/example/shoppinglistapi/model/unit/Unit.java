package com.example.shoppinglistapi.model.unit;

import lombok.Getter;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum Unit {

    PIECE(BaseUnit.PIECE, Prefix.NONE, 1),

    GRAM(BaseUnit.GRAM, Prefix.NONE, 10),
    KILOGRAM(BaseUnit.GRAM, Prefix.KILO, Unit.GRAM, 1),

    LITER(BaseUnit.LITER, Prefix.NONE, 1),
    MILLILITER(BaseUnit.LITER, Prefix.MILLI, Unit.LITER, 10);


    BaseUnit baseUnit;
    Prefix prefix;
    Unit masterUnit;
    Integer incrementalStep;

    Unit(BaseUnit baseUnit, Prefix prefix, Unit masterUnit, Integer incrementalStep) {
        this.baseUnit = baseUnit;
        this.prefix = prefix;
        this.masterUnit = masterUnit;
        this.incrementalStep = incrementalStep;
    }

    Unit(BaseUnit baseUnit, Prefix prefix, Integer incrementalStep) {
        this.baseUnit = baseUnit;
        this.prefix = prefix;
        this.incrementalStep = incrementalStep;
    }

    public static Unit fromAbbreviation(String abbreviation) {
        return Arrays.stream(Unit.values())
                .filter(u -> u.toString().equals(abbreviation))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Unable to find unit with provided abbreviation."));
    }

    public static List<Unit> getUnitsByBaseUnitOrderedByScale(BaseUnit baseUnit) {
        return Arrays.stream(Unit.values())
                .filter(u -> u.baseUnit.equals(baseUnit))
                .sorted(Comparator.comparing(u -> u.prefix.scale))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public String toString() {
        return prefix.abbreviation + baseUnit.abbreviation;
    }
}
