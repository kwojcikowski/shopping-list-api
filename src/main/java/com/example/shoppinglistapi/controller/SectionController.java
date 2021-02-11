package com.example.shoppinglistapi.controller;

import com.example.shoppinglistapi.dto.section.SectionReadDto;
import com.example.shoppinglistapi.model.Section;
import com.example.shoppinglistapi.repository.SectionRepository;
import com.example.shoppinglistapi.service.assembler.SectionModelAssembler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public ResponseEntity<CollectionModel<SectionReadDto>> getAllSections() {
        return ResponseEntity.ok(sectionModelAssembler.toCollectionModel(sectionRepository.findAll()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<SectionReadDto> getSectionById(@PathVariable("id") Long id) {
        Section foundSection = sectionRepository.findById(id).orElse(null);
        return foundSection == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(sectionModelAssembler.toModel(foundSection));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> removeSection(@PathVariable("id") Long id) {
        if (!sectionRepository.existsById(id))
            return ResponseEntity.notFound().build();
        sectionRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
