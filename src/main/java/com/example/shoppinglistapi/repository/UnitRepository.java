package com.example.shoppinglistapi.repository;

import com.example.shoppinglistapi.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@RepositoryRestResource
@CrossOrigin
public interface UnitRepository extends JpaRepository<Unit, Long> {
    List<Unit> findAllByBaseUnitIdOrderByPrefix_ScaleAsc(@Param("baseUnitId") Long baseUnitId);
}
