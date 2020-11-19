package com.example.antonapi.service.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDTO extends RepresentationModel<CartItemDTO> {
    private Long id;
    @NonNull
    private ProductDTO product;
    @NonNull
    private UnitDTO unit;
    @NonNull
    private BigDecimal quantity;
}
