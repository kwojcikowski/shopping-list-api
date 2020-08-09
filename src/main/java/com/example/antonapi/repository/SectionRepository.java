package com.example.antonapi.repository;

import com.example.antonapi.model.Section;
import com.example.antonapi.model.projections.SectionExcerpt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(excerptProjection = SectionExcerpt.class)
@CrossOrigin
@Repository
public interface SectionRepository extends JpaRepository<Section, Long> {
}
