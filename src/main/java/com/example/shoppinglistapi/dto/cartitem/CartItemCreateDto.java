package com.example.shoppinglistapi.dto.cartitem;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemCreateDto {

    @Min(1)
    public Long productId;
    @NotBlank
    public String unitAbbreviation;
    @NotNull
    public BigDecimal quantity;

}
