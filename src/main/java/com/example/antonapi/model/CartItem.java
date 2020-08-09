package com.example.antonapi.model;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NonNull
    private Long id;

    @OneToOne()
    @NonNull
    private Product product;

    @ManyToOne()
    @NonNull
    private Unit unit;

    @NonNull
    private BigDecimal quantity;
}
