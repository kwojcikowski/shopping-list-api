package com.example.antonapi.service.assembler;

import com.example.antonapi.model.Unit;
import com.example.antonapi.service.dto.UnitDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class UnitModelAssembler implements RepresentationModelAssembler<Unit, UnitDTO> {

    @Override
    public UnitDTO toModel(Unit entity) {
        return null;
    }

    @Override
    public CollectionModel<UnitDTO> toCollectionModel(Iterable<? extends Unit> entities) {
        return null;
    }
}
