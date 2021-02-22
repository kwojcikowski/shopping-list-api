package com.example.shoppinglistapi.service.assembler;

import com.example.shoppinglistapi.dto.storesection.StoreSectionReadDto;
import com.example.shoppinglistapi.model.StoreSection;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StoreSectionModelAssembler implements RepresentationModelAssembler<StoreSection, StoreSectionReadDto> {

    private final @NonNull SectionModelAssembler sectionModelAssembler;
    private final @NonNull StoreModelAssembler storeModelAssembler;

    @Override
    public StoreSectionReadDto toModel(StoreSection entity) {
        return StoreSectionReadDto.builder()
                .id(entity.getId())
                .store(storeModelAssembler.toModel(entity.getStore()))
                .section(sectionModelAssembler.toModel(entity.getSection()))
                .position(entity.getPosition())
                .build();
    }

    @Override
    public CollectionModel<StoreSectionReadDto> toCollectionModel(Iterable<? extends StoreSection> entities) {
        List<StoreSectionReadDto> storeSectionReadDtos = new ArrayList<>();
        for (StoreSection entity : entities) {
            storeSectionReadDtos.add(toModel(entity));
        }
        return CollectionModel.of(storeSectionReadDtos);
    }
}
