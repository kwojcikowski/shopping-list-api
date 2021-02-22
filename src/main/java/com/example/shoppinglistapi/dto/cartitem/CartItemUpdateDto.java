package com.example.shoppinglistapi.dto.cartitem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemUpdateDto {

    @Min(1)
    public Long id;
    @NotBlank
    public String unitAbbreviation;
    @NotNull
    public BigDecimal quantity;

}
