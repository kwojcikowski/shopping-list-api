package com.example.shoppinglistapi.service.assembler;

import com.example.shoppinglistapi.controller.UnitController;
import com.example.shoppinglistapi.model.Unit;
import com.example.shoppinglistapi.dto.unit.UnitReadDto;
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
public class UnitModelAssembler implements RepresentationModelAssembler<Unit, UnitReadDto> {

    private final @NonNull ModelMapper modelMapper;

    @Override
    public UnitReadDto toModel(Unit entity) {
        UnitReadDto unitReadDto = modelMapper.map(entity, UnitReadDto.class);
        Link selfLink = linkTo(methodOn(UnitController.class).getUnitById(entity.getId())).withSelfRel();
        unitReadDto.add(selfLink);
        return unitReadDto;
    }

    @Override
    public CollectionModel<UnitReadDto> toCollectionModel(Iterable<? extends Unit> entities) {
        List<UnitReadDto> unitReadDtos = new ArrayList<>();
        for(Unit entity : entities)
            unitReadDtos.add(toModel(entity));
        return CollectionModel.of(unitReadDtos);
    }
}
