package com.example.antonapi.repository;

import com.example.antonapi.model.BaseUnit;
import com.example.antonapi.model.CartItem;
import com.example.antonapi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByProductAndUnit_BaseUnit(@Param("product") Product product, @Param("baseUnit") BaseUnit baseUnit);
}
