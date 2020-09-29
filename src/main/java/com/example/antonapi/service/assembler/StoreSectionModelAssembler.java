package com.example.antonapi.service.assembler;

import com.example.antonapi.controller.StoreSectionController;
import com.example.antonapi.model.StoreSection;
import com.example.antonapi.service.dto.StoreSectionDTO;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class StoreSectionModelAssembler implements RepresentationModelAssembler<StoreSection, StoreSectionDTO> {

    @Override
    public StoreSectionDTO toModel(StoreSection entity) {
        ModelMapper mapper = new ModelMapper();
        StoreSectionDTO storeSectionDTO = mapper.map(entity, StoreSectionDTO.class);
        Link selfLink = linkTo(methodOn(StoreSectionController.class).getStoreSectionById(entity.getId())).withSelfRel();
        storeSectionDTO.add(selfLink);
        return storeSectionDTO;
    }

    @Override
    public CollectionModel<StoreSectionDTO> toCollectionModel(Iterable<? extends StoreSection> entities) {
        List<StoreSectionDTO> storeSectionDTOS = new ArrayList<>();
        for (StoreSection entity : entities){
            storeSectionDTOS.add(toModel(entity));
        }
        return CollectionModel.of(storeSectionDTOS);
    }
}
