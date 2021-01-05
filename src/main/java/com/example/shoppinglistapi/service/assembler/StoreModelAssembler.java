package com.example.shoppinglistapi.service.assembler;

import com.example.shoppinglistapi.controller.StoreController;
import com.example.shoppinglistapi.model.Store;
import com.example.shoppinglistapi.dto.StoreDTO;
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
public class StoreModelAssembler implements RepresentationModelAssembler<Store, StoreDTO> {

    private final @NonNull ModelMapper modelMapper;

    @Override
    public StoreDTO toModel(Store entity) {
        StoreDTO storeDTO = modelMapper.map(entity, StoreDTO.class);
        Link selfLink = linkTo(methodOn(StoreController.class).getStoreById(entity.getId())).withSelfRel();
        storeDTO.add(selfLink);
        return storeDTO;
    }

    @Override
    public CollectionModel<StoreDTO> toCollectionModel(Iterable<? extends Store> entities) {
        List<StoreDTO> storeDTOs = new ArrayList<>();
        for (Store entity : entities){
            storeDTOs.add(toModel(entity));
        }
        return CollectionModel.of(storeDTOs);
    }
}
