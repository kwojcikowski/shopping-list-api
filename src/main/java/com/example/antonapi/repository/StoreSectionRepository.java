package com.example.antonapi.repository;

import com.example.antonapi.model.Store;
import com.example.antonapi.model.StoreSection;
import com.example.antonapi.model.projections.StoreSectionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin
@Repository
public interface StoreSectionRepository extends JpaRepository<StoreSection, Long> {
    List<StoreSection> findAllByStore(@Param("store") Store store);
}
