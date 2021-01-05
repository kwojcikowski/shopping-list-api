package com.example.antonapi.repository;

import com.example.antonapi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource
@CrossOrigin
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findProductById(@Param("id") Long id);
    Product findProductByName(@Param("name") String name);
}
