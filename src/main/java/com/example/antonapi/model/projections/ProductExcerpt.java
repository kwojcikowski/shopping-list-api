package com.example.antonapi.model.projections;

import com.example.antonapi.model.Product;
import com.example.antonapi.model.Unit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

@Projection(name = "productExcerpt", types = {Product.class})
public interface ProductExcerpt {
    String getName();
    @Value("#{target.defaultUnit.toString()}")
    String getDefaultUnit();
    SectionExcerpt getSection();
}
