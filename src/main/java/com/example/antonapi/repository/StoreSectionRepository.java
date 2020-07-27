package com.example.antonapi.repository;

import com.example.antonapi.model.StoreSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreSectionRepository extends JpaRepository<StoreSection, Long> {
}
