package com.example.shoppinglistapi.repository;

import com.example.shoppinglistapi.model.StoreSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin
@RepositoryRestResource
public interface StoreSectionRepository extends JpaRepository<StoreSection, Long> {
    List<StoreSection> findAllByStore_IdOrderByPosition(@Param("storeId") Long storeId);
    void removeAllByStore_Id(@Param("storeId") Long storeId);
}
