package com.example.antonapi.service.assembler;

import com.example.antonapi.controller.SectionController;
import com.example.antonapi.model.Section;
import com.example.antonapi.service.dto.SectionDTO;
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
public class SectionModelAssembler implements RepresentationModelAssembler<Section, SectionDTO> {

    private final @NonNull ModelMapper modelMapper;

    @Override
    public SectionDTO toModel(Section entity) {
        SectionDTO sectionDTO = modelMapper.map(entity, SectionDTO.class);
        Link selfLink = linkTo(methodOn(SectionController.class).getSectionById(entity.getId())).withSelfRel();
        sectionDTO.add(selfLink);
        return sectionDTO;
    }

    @Override
    public CollectionModel<SectionDTO> toCollectionModel(Iterable<? extends Section> entities) {
        List<SectionDTO> sectionDTOS = new ArrayList<>();
        for (Section entity : entities){
            sectionDTOS.add(toModel(entity));
        }
        return CollectionModel.of(sectionDTOS);
    }
}
