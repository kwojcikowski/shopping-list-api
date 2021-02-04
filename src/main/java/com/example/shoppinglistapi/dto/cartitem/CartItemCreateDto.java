package com.example.shoppinglistapi.dto.cartitem;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemCreateDto {

    @NonNull
    public Long productId;
    @NonNull
    public Long unitId;
    @NonNull
    public BigDecimal quantity;

}
