package com.example.antonapi.model;

import com.fasterxml.jackson.annotation.*;
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
    @JsonProperty
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
    @JsonProperty("abbreviation")
    public String toString() {
        return prefix.abbreviation + baseUnit.abbreviation;
    }
}
