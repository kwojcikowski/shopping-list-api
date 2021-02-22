package com.example.shoppinglistapi.service.assembler;

import com.example.shoppinglistapi.controller.SectionController;
import com.example.shoppinglistapi.model.Section;
import com.example.shoppinglistapi.dto.section.SectionReadDto;
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
public class SectionModelAssembler implements RepresentationModelAssembler<Section, SectionReadDto> {

    @Override
    public SectionReadDto toModel(Section entity) {
        SectionReadDto sectionReadDto = SectionReadDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
        Link selfLink = linkTo(methodOn(SectionController.class).getSectionById(entity.getId())).withSelfRel();
        sectionReadDto.add(selfLink);
        return sectionReadDto;
    }

    @Override
    public CollectionModel<SectionReadDto> toCollectionModel(Iterable<? extends Section> entities) {
        List<SectionReadDto> sectionReadDtos = new ArrayList<>();
        for (Section entity : entities){
            sectionReadDtos.add(toModel(entity));
        }
        return CollectionModel.of(sectionReadDtos);
    }
}
