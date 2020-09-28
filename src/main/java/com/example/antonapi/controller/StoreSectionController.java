package com.example.antonapi.controller;

import com.example.antonapi.model.StoreSection;
import com.example.antonapi.repository.StoreSectionRepository;
import com.example.antonapi.service.assembler.StoreSectionModelAssembler;
import com.example.antonapi.service.dto.StoreSectionDTO;
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
@RequestMapping(path = "/storeSections")
public class StoreSectionController {

    private final @NonNull StoreSectionRepository storeSectionRepository;
    private final @NonNull StoreSectionModelAssembler storeSectionModelAssembler;
    private final @NonNull ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<CollectionModel<StoreSectionDTO>> getAllStoreSections() {
        return ResponseEntity.ok(storeSectionModelAssembler.toCollectionModel(storeSectionRepository.findAll()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<StoreSectionDTO> getStoreSectionById(@PathVariable("id") Long id){
        return ResponseEntity.ok(storeSectionModelAssembler.toModel(storeSectionRepository.getOne(id)));
    }

    @PostMapping
    public ResponseEntity<StoreSectionDTO> addStoreSection(@RequestBody StoreSectionDTO requestStoreSection) {
        StoreSection mappedStoreSection = modelMapper.map(requestStoreSection, StoreSection.class);
        StoreSection addedStoreSection = storeSectionRepository.saveAndFlush(mappedStoreSection);
        return ResponseEntity.ok(storeSectionModelAssembler.toModel(addedStoreSection));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity.HeadersBuilder<?> removeStoreSection(@PathVariable("id") Long id) {
        storeSectionRepository.deleteById(id);
        return ResponseEntity.noContent();
    }
}
