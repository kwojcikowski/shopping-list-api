package com.example.shoppinglistapi.service.assembler;

import com.example.shoppinglistapi.controller.StoreController;
import com.example.shoppinglistapi.model.Store;
import com.example.shoppinglistapi.dto.store.StoreReadDto;
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
public class StoreModelAssembler implements RepresentationModelAssembler<Store, StoreReadDto> {

    @Override
    public StoreReadDto toModel(Store entity) {
        StoreReadDto storeReadDto = StoreReadDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .urlFriendlyName(entity.getUrlFriendlyName())
                .build();
        Link selfLink = linkTo(methodOn(StoreController.class).getStoreById(entity.getId())).withSelfRel();
        storeReadDto.add(selfLink);
        return storeReadDto;
    }

    @Override
    public CollectionModel<StoreReadDto> toCollectionModel(Iterable<? extends Store> entities) {
        List<StoreReadDto> storeReadDtos = new ArrayList<>();
        for (Store entity : entities){
            storeReadDtos.add(toModel(entity));
        }
        return CollectionModel.of(storeReadDtos);
    }
}
