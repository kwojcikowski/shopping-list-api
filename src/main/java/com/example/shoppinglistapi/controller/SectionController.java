package com.example.shoppinglistapi.controller;

import com.example.shoppinglistapi.model.Section;
import com.example.shoppinglistapi.repository.SectionRepository;
import com.example.shoppinglistapi.service.assembler.SectionModelAssembler;
import com.example.shoppinglistapi.dto.section.SectionReadDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RepositoryRestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(path = "/sections")
public class SectionController {

    private final @NonNull SectionRepository sectionRepository;
    private final @NonNull SectionModelAssembler sectionModelAssembler;
    private final @NonNull ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<CollectionModel<SectionReadDto>> getAllSections() {
        return ResponseEntity.ok(sectionModelAssembler.toCollectionModel(sectionRepository.findAll()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<SectionReadDto> getSectionById(@PathVariable("id") Long id){
        Section foundSection = sectionRepository.findById(id).orElse(null);
        return foundSection == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(sectionModelAssembler.toModel(foundSection));
    }

    @PostMapping
    public ResponseEntity<SectionReadDto> addSection(@RequestBody SectionReadDto requestSection) {
        Section mappedSection = modelMapper.map(requestSection, Section.class);
        Section addedSection = sectionRepository.saveAndFlush(mappedSection);
        SectionReadDto returnSectionReadDto = sectionModelAssembler.toModel(addedSection);
        return ResponseEntity.created(URI.create(returnSectionReadDto.getLink("self").get().getHref()))
                .body(returnSectionReadDto);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> removeSection(@PathVariable("id") Long id) {
        if(!sectionRepository.existsById(id))
            return ResponseEntity.notFound().build();
        sectionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
