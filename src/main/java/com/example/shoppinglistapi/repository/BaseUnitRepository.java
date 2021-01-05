package com.example.shoppinglistapi.repository;

import com.example.shoppinglistapi.model.BaseUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin
@RepositoryRestResource(exported=false)
public interface BaseUnitRepository extends JpaRepository<BaseUnit, Long> {
}
