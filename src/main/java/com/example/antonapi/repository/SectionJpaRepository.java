package com.example.antonapi.repository;

import com.example.antonapi.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SectionJpaRepository extends JpaRepository<Section, Long> {
}
