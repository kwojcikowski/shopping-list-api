package com.example.antonapi.service.assembler;

import com.example.antonapi.controller.UnitController;
import com.example.antonapi.model.Unit;
import com.example.antonapi.service.dto.UnitDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UnitModelAssembler implements RepresentationModelAssembler<Unit, UnitDTO> {

    @Override
    public UnitDTO toModel(Unit entity) {
        UnitDTO unitDTO = UnitDTO.builder().id(entity.getId()).abbreviation(entity.toString()).build();
        Link selfLink = linkTo(methodOn(UnitController.class).getUnitById(entity.getId())).withSelfRel();
        unitDTO.add(selfLink);
        return unitDTO;
    }

    @Override
    public CollectionModel<UnitDTO> toCollectionModel(Iterable<? extends Unit> entities) {
        List<UnitDTO> unitDTOS = new ArrayList<>();
        for(Unit entity : entities)
            unitDTOS.add(toModel(entity));
        return CollectionModel.of(unitDTOS);
    }
}
