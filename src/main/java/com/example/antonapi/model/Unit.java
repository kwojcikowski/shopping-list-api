package com.example.antonapi.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import javax.persistence.*;
import java.util.Objects;


@Entity
@Data
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

    @NonNull
    @ManyToOne
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    Unit masterUnit;

    @Override
    public String toString() {
        return prefix.abbreviation + baseUnit.abbreviation;
    }
}
