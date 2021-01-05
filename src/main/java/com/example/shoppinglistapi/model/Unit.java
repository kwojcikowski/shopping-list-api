package com.example.shoppinglistapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @NonNull
    @ManyToOne
    @JsonIgnore
    BaseUnit baseUnit;

    @NonNull
    @ManyToOne
    @JsonIgnore
    Prefix prefix;

    @ManyToOne
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    Unit masterUnit;

    @Override
    public String toString() {
        return prefix.abbreviation + baseUnit.abbreviation;
    }
}
