package com.example.shoppinglistapi.controller;

import com.example.shoppinglistapi.model.Section;
import com.example.shoppinglistapi.repository.SectionRepository;
import com.example.shoppinglistapi.service.assembler.SectionModelAssembler;
import com.example.shoppinglistapi.dto.SectionDTO;
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
    public ResponseEntity<CollectionModel<SectionDTO>> getAllSections() {
        return ResponseEntity.ok(sectionModelAssembler.toCollectionModel(sectionRepository.findAll()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<SectionDTO> getSectionById(@PathVariable("id") Long id){
        Section foundSection = sectionRepository.findById(id).orElse(null);
        return foundSection == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(sectionModelAssembler.toModel(foundSection));
    }

    @PostMapping
    public ResponseEntity<SectionDTO> addSection(@RequestBody SectionDTO requestSection) {
        Section mappedSection = modelMapper.map(requestSection, Section.class);
        Section addedSection = sectionRepository.saveAndFlush(mappedSection);
        SectionDTO returnSectionDTO = sectionModelAssembler.toModel(addedSection);
        return ResponseEntity.created(URI.create(returnSectionDTO.getLink("self").get().getHref()))
                .body(returnSectionDTO);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> removeSection(@PathVariable("id") Long id) {
        if(!sectionRepository.existsById(id))
            return ResponseEntity.notFound().build();
        sectionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
