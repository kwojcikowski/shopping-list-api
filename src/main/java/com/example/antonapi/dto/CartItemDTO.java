package com.example.antonapi.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(itemRelation = "cartItem", collectionRelation = "cartItems")
public class CartItemDTO extends RepresentationModel<CartItemDTO> {
    private Long id;
    @NonNull
    private ProductDTO product;
    @NonNull
    private UnitDTO unit;
    @NonNull
    private BigDecimal quantity;
}
