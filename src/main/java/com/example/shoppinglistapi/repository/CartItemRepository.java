package com.example.shoppinglistapi.repository;

import com.example.shoppinglistapi.model.BaseUnit;
import com.example.shoppinglistapi.model.CartItem;
import com.example.shoppinglistapi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByProduct_IdAndUnit_BaseUnit_Id(@Param("productId") Long productId, @Param("baseUnitId") Long baseUnitId);
}
