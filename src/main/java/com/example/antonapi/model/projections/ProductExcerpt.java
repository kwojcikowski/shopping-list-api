package com.example.antonapi.model.projections;

import com.example.antonapi.model.Product;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "productExcerpt", types = {Product.class})
public interface ProductExcerpt {
    @JsonIgnore
    Long getId();
    String getName();
    UnitExcerpt getDefaultUnit();
    SectionExcerpt getSection();
}
