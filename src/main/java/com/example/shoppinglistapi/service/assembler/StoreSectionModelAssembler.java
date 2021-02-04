package com.example.shoppinglistapi.service.assembler;

import com.example.shoppinglistapi.controller.StoreSectionController;
import com.example.shoppinglistapi.model.StoreSection;
import com.example.shoppinglistapi.dto.storesection.StoreSectionReadDto;
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
public class StoreSectionModelAssembler implements RepresentationModelAssembler<StoreSection, StoreSectionReadDto> {

    private final @NonNull ModelMapper modelMapper;

    @Override
    public StoreSectionReadDto toModel(StoreSection entity) {
        StoreSectionReadDto storeSectionReadDto = modelMapper.map(entity, StoreSectionReadDto.class);
        Link selfLink = linkTo(methodOn(StoreSectionController.class).getStoreSectionById(entity.getId())).withSelfRel();
        storeSectionReadDto.add(selfLink);
        return storeSectionReadDto;
    }

    @Override
    public CollectionModel<StoreSectionReadDto> toCollectionModel(Iterable<? extends StoreSection> entities) {
        List<StoreSectionReadDto> storeSectionReadDtos = new ArrayList<>();
        for (StoreSection entity : entities){
            storeSectionReadDtos.add(toModel(entity));
        }
        return CollectionModel.of(storeSectionReadDtos);
    }
}
