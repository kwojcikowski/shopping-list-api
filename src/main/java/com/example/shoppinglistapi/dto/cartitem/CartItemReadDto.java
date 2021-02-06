package com.example.shoppinglistapi.dto.cartitem;

import com.example.shoppinglistapi.dto.product.ProductReadDto;
import com.example.shoppinglistapi.dto.unit.UnitReadDto;
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
public class CartItemReadDto extends RepresentationModel<CartItemReadDto> {

    @NonNull
    public Long id;
    @NonNull
    public ProductReadDto product;
    @NonNull
    public UnitReadDto unit;
    @NonNull
    public BigDecimal quantity;

}
