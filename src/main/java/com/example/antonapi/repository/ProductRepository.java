package com.example.antonapi.repository;

import com.example.antonapi.model.Product;
import com.example.antonapi.model.projections.ProductExcerpt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(excerptProjection = ProductExcerpt.class)
@CrossOrigin
public interface ProductRepository extends JpaRepository<Product, Long> {
}
