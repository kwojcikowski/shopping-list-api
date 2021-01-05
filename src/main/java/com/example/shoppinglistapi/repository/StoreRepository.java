package com.example.shoppinglistapi.repository;

import com.example.shoppinglistapi.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource
@CrossOrigin
public interface StoreRepository extends JpaRepository<Store, Long> {
    Store findByUrlFriendlyName(@Param("urlFriendlyName") String urlFriendlyName);
}
