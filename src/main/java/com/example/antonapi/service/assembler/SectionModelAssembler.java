package com.example.antonapi.service.assembler;

import com.example.antonapi.controller.SectionController;
import com.example.antonapi.model.Section;
import com.example.antonapi.service.dto.SectionDTO;
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
public class SectionModelAssembler implements RepresentationModelAssembler<Section, SectionDTO> {

    @Override
    public SectionDTO toModel(Section entity) {
        ModelMapper mapper = new ModelMapper();
        SectionDTO sectionDTO = mapper.map(entity, SectionDTO.class);
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
