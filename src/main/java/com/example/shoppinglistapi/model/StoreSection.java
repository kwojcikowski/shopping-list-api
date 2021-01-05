package com.example.shoppinglistapi.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreSection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    @ManyToOne
    private Store store;

    @NonNull
    @ManyToOne
    private Section section;

    @NonNull
    private Integer position;
}
