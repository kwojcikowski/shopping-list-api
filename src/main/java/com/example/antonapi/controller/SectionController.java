package com.example.antonapi.controller;

import com.example.antonapi.model.Section;
import com.example.antonapi.repository.SectionRepository;
import com.example.antonapi.service.assembler.SectionModelAssembler;
import com.example.antonapi.service.dto.SectionDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RepositoryRestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(path = "/sections")
public class SectionController {

    private final @NonNull SectionRepository sectionRepository;
    private final @NonNull SectionModelAssembler sectionModelAssembler;
    private final @NonNull ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<CollectionModel<SectionDTO>> getAllSections() {
        return ResponseEntity.ok(sectionModelAssembler.toCollectionModel(sectionRepository.findAll()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<SectionDTO> getSectionById(@PathVariable("id") Long id){
        return ResponseEntity.ok(sectionModelAssembler.toModel(sectionRepository.getOne(id)));
    }

    @PostMapping
    public ResponseEntity<SectionDTO> addSection(@RequestBody SectionDTO requestSection) {
        Section mappedSection = modelMapper.map(requestSection, Section.class);
        Section addedSection = sectionRepository.saveAndFlush(mappedSection);
        return ResponseEntity.ok(sectionModelAssembler.toModel(addedSection));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity.HeadersBuilder<?> removeSection(@PathVariable("id") Long id) {
        sectionRepository.deleteById(id);
        return ResponseEntity.noContent();
    }
}
