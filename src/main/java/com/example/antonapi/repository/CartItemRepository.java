package com.example.antonapi.repository;

import com.example.antonapi.model.CartItem;
import com.example.antonapi.model.Product;
import com.example.antonapi.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@Repository
@CrossOrigin
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByProductAndUnit(@Param("product") Product product, @Param("unit") Unit unit);
}
