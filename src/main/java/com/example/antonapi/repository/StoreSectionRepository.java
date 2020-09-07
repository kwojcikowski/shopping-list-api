package com.example.antonapi.repository;

import com.example.antonapi.model.Store;
import com.example.antonapi.model.StoreSection;
import com.example.antonapi.model.projections.StoreSectionExcerpt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin
@RepositoryRestResource(excerptProjection = StoreSectionExcerpt.class)
public interface StoreSectionRepository extends JpaRepository<StoreSection, Long> {
    List<StoreSection> findByStore_UrlFriendlyNameOrderByPosition(@Param("storeUrlFriendlyName") String urlFriendlyName);
}
