package com.example.shoppinglistapi.model.unit;

import lombok.Getter;

@Getter
public enum Prefix {

    MILLI("m", 0.001),
    NONE("", 1d),
    DEKA("da", 10d),
    KILO("k", 1000d);

    String abbreviation;
    Double scale;

    Prefix(String abbreviation, Double scale) {
        this.abbreviation = abbreviation;
        this.scale = scale;
    }
}
