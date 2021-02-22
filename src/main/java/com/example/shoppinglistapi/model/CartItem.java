package com.example.shoppinglistapi.model;
import com.example.shoppinglistapi.model.unit.Unit;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CartItem{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @NonNull
    private Product product;

    @NonNull
    private Unit unit;

    @NonNull
    private BigDecimal quantity;
}
