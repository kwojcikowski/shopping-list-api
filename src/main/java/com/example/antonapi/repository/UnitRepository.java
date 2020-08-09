package com.example.antonapi.repository;

import com.example.antonapi.model.Unit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Repository
@CrossOrigin
public interface UnitRepository extends JpaRepository<Unit, Long> {
    List<Unit> findAllByBaseUnitId(@Param("baseUnitId") Long baseUnitId);
}
