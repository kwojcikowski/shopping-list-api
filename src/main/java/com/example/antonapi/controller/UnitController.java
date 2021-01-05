package com.example.antonapi.controller;

import com.example.antonapi.model.Unit;
import com.example.antonapi.repository.UnitRepository;
import com.example.antonapi.service.assembler.UnitModelAssembler;
import com.example.antonapi.dto.UnitDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RepositoryRestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(path = "/units")
public class UnitController {

    private final @NonNull UnitRepository unitRepository;
    private final @NonNull UnitModelAssembler unitModelAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<UnitDTO>> getAllUnits() {
        return ResponseEntity.ok(unitModelAssembler.toCollectionModel(unitRepository.findAll()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<UnitDTO> getUnitById(@PathVariable("id") Long id){
        Unit unit = unitRepository.findById(id).orElse(null);
        return unit == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(unitModelAssembler.toModel(unit));
    }
}
