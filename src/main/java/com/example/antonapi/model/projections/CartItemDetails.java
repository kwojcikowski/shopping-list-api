package com.example.antonapi.model.projections;

import com.example.antonapi.model.CartItem;
import com.example.antonapi.model.BaseUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.math.BigDecimal;

@CrossOrigin
@Projection(name = "cartItemDetails", types = CartItem.class)
public interface CartItemDetails {
    @Value("#{target.product.section.id}")
    Long getSectionId();
    BigDecimal getQuantity();
    BaseUnit getUnit();
    ProductExcerpt getProduct();
}
