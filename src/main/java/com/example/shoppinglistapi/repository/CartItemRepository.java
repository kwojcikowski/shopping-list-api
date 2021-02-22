package com.example.shoppinglistapi.repository;

import com.example.shoppinglistapi.model.CartItem;
import com.example.shoppinglistapi.model.unit.BaseUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin
@RepositoryRestResource
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findAllByProduct_Id(@Param("productId") Long productId);
}
