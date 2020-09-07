package com.example.antonapi.repository;

import com.example.antonapi.model.Unit;
import com.example.antonapi.model.projections.ProductExcerpt;
import com.example.antonapi.model.projections.UnitExcerpt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RepositoryRestResource(excerptProjection = UnitExcerpt.class)
@CrossOrigin
public interface UnitRepository extends JpaRepository<Unit, Long> {
    List<Unit> findAllByBaseUnitIdOrderByPrefix_ScaleAsc(@Param("baseUnitId") Long baseUnitId);
}
