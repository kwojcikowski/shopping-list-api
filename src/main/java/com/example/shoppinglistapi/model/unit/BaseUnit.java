package com.example.shoppinglistapi.model.unit;

public enum BaseUnit {

    GRAM("g"),
    PIECE("pcs"),
    LITER("l");

    String abbreviation;

    BaseUnit(String abbreviation) {
        this.abbreviation = abbreviation;
    }

}
