package com.example.antonapi.service.assembler;

import com.example.antonapi.controller.UnitController;
import com.example.antonapi.model.Unit;
import com.example.antonapi.service.dto.UnitDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class UnitModelAssembler implements RepresentationModelAssembler<Unit, UnitDTO> {

    private final @NonNull ModelMapper modelMapper;

    @Override
    public UnitDTO toModel(Unit entity) {
        UnitDTO unitDTO = modelMapper.map(entity, UnitDTO.class);
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
