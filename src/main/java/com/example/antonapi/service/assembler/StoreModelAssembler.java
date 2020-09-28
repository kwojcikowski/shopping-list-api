package com.example.antonapi.service.assembler;

import com.example.antonapi.controller.SectionController;
import com.example.antonapi.controller.StoreController;
import com.example.antonapi.model.Store;
import com.example.antonapi.service.dto.StoreDTO;
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
public class StoreModelAssembler implements RepresentationModelAssembler<Store, StoreDTO> {

    @Override
    public StoreDTO toModel(Store entity) {
        ModelMapper mapper = new ModelMapper();
        StoreDTO storeDTO = mapper.map(entity, StoreDTO.class);
        Link selfLink = linkTo(methodOn(StoreController.class).getStoreById(entity.getId())).withSelfRel();
        storeDTO.add(selfLink);
        return storeDTO;
    }

    @Override
    public CollectionModel<StoreDTO> toCollectionModel(Iterable<? extends Store> entities) {
        ModelMapper modelMapper = new ModelMapper();
        List<StoreDTO> storeDTOs = new ArrayList<>();
        for (Store entity : entities){
            StoreDTO storeDTO = modelMapper.map(entity, StoreDTO.class);
            Link selfLink = linkTo(methodOn(SectionController.class).getSectionById(entity.getId())).withSelfRel();
            storeDTO.add(selfLink);
            storeDTOs.add(storeDTO);
        }
        return CollectionModel.of(storeDTOs);
    }
}
