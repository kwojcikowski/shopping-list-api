package com.example.shoppinglistapi.controller;

import com.example.shoppinglistapi.model.StoreSection;
import com.example.shoppinglistapi.repository.StoreSectionRepository;
import com.example.shoppinglistapi.service.assembler.StoreSectionModelAssembler;
import com.example.shoppinglistapi.dto.storesection.StoreSectionReadDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RepositoryRestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(path = "/storeSections")
public class StoreSectionController {

    private final @NonNull StoreSectionRepository storeSectionRepository;
    private final @NonNull StoreSectionModelAssembler storeSectionModelAssembler;
    private final @NonNull ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<CollectionModel<StoreSectionReadDto>> getAllStoreSections() {
        return ResponseEntity.ok(storeSectionModelAssembler.toCollectionModel(storeSectionRepository.findAll()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<StoreSectionReadDto> getStoreSectionById(@PathVariable("id") Long id){
        StoreSection foundStoreSection = storeSectionRepository.findById(id).orElse(null);
        return foundStoreSection == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(storeSectionModelAssembler.toModel(foundStoreSection));
    }

    @PostMapping
    public ResponseEntity<StoreSectionReadDto> addStoreSection(@RequestBody StoreSectionReadDto requestStoreSection) {
        StoreSection mappedStoreSection = modelMapper.map(requestStoreSection, StoreSection.class);
        StoreSection addedStoreSection = storeSectionRepository.saveAndFlush(mappedStoreSection);
        StoreSectionReadDto returnStoreSection = storeSectionModelAssembler.toModel(addedStoreSection);
        return ResponseEntity.created(URI.create(returnStoreSection.getLink("self").get().getHref()))
                .body(returnStoreSection);
    }

    @PatchMapping
    public ResponseEntity<?> updateStoreSections(@RequestBody List<StoreSectionReadDto> requestStoreSections) {
        storeSectionRepository.saveAll(
                requestStoreSections.stream()
                        .map(storeSectionDTO -> modelMapper.map(storeSectionDTO, StoreSection.class))
                        .collect(Collectors.toList()));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?>  removeStoreSection(@PathVariable("id") Long id) {
        if(storeSectionRepository.existsById(id)) {
            storeSectionRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
