package com.example.antonapi.model.projections;

import com.example.antonapi.model.CartItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;

@Projection(name = "cartItemExcerpt", types = {CartItem.class})
public interface CartItemExcerpt {
    @Value("#{target.product.section.id}")
    Long getSectionId();
    BigDecimal getQuantity();
    UnitExcerpt getUnit();
    ProductExcerpt getProduct();
}
