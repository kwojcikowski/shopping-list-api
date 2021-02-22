package com.example.shoppinglistapi.model.unit;

import lombok.Getter;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum Unit {

    PIECE(BaseUnit.PIECE, Prefix.NONE),

    GRAM(BaseUnit.GRAM, Prefix.NONE),
    KILOGRAM(BaseUnit.GRAM, Prefix.KILO, Unit.GRAM),

    LITER(BaseUnit.LITER, Prefix.NONE),
    MILLILITER(BaseUnit.LITER, Prefix.MILLI, Unit.LITER);


    BaseUnit baseUnit;
    Prefix prefix;
    Unit masterUnit;

    Unit(BaseUnit baseUnit, Prefix prefix, Unit masterUnit) {
        this.baseUnit = baseUnit;
        this.prefix = prefix;
        this.masterUnit = masterUnit;
    }

    Unit(BaseUnit baseUnit, Prefix prefix) {
        this.baseUnit = baseUnit;
        this.prefix = prefix;
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
