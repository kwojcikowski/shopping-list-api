package com.example.shoppinglistapi.controller;

import com.example.shoppinglistapi.model.unit.Unit;
import com.example.shoppinglistapi.service.assembler.UnitModelAssembler;
import com.example.shoppinglistapi.dto.unit.UnitReadDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@RepositoryRestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(path = "/units")
public class UnitController {

    private final @NonNull UnitModelAssembler unitModelAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<UnitReadDto>> getAllUnits() {
        return ResponseEntity.ok(unitModelAssembler.toCollectionModel(List.of(Unit.values())));
    }

    @GetMapping(path = "/{abbreviation}")
    public ResponseEntity<UnitReadDto> getUnitByAbbreviation(@PathVariable("abbreviation") String abbreviation){
        try{
            Unit unit = Unit.fromAbbreviation(abbreviation);
            return ResponseEntity.ok(unitModelAssembler.toModel(unit));
        }catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
