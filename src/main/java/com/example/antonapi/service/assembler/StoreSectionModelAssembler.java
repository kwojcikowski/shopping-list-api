package com.example.antonapi.service.assembler;

import com.example.antonapi.model.StoreSection;
import com.example.antonapi.service.dto.StoreSectionDTO;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class StoreSectionModelAssembler implements RepresentationModelAssembler<StoreSection, StoreSectionDTO> {

    @Override
    public StoreSectionDTO toModel(StoreSection entity) {
        return null;
    }

    @Override
    public CollectionModel<StoreSectionDTO> toCollectionModel(Iterable<? extends StoreSection> entities) {
        return null;
    }
}
