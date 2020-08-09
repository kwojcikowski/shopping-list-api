package com.example.antonapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Unit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    Long id;

    @NonNull
    @ManyToOne
    BaseUnit baseUnit;

    @NonNull
    @ManyToOne
    Prefix prefix;

//    @Override
//    public String toString() {
//        return prefix.abbreviation + baseUnit.abbreviation;
//    }
}
