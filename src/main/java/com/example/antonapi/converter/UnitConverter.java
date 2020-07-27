package com.example.antonapi.converter;

import com.example.antonapi.model.Unit;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class UnitConverter implements AttributeConverter<Unit, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Unit unit) {
        if (unit == null)
            return null;

        Integer dbValue;
        switch (unit){
            case GRAMS:
                dbValue = 1;
                break;
            case LITERS:
                dbValue = 2;
                break;
            case PIECES:
                dbValue = 3;
                break;
            default:
                throw new IllegalArgumentException("Unit [" + unit.toString() + "] is not supported.");
        }
        return dbValue;
    }

    @Override
    public Unit convertToEntityAttribute(Integer integer) {
        if(integer == null)
            return null;

        Unit unit;
        switch (integer){
            case 1:
                unit = Unit.GRAMS;
                break;
            case 2:
                unit = Unit.LITERS;
                break;
            case 3:
                unit = Unit.PIECES;
                break;
            default:
                throw new IllegalArgumentException("Could not convert Integer [" + integer + "] to unit value.");
        }
        return unit;
    }
}
