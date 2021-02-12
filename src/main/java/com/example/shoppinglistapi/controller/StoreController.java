package com.example.shoppinglistapi.controller;

import com.example.shoppinglistapi.dto.store.StoreCreateDto;
import com.example.shoppinglistapi.dto.store.StoreReadDto;
import com.example.shoppinglistapi.dto.storesection.StoreSectionCreateDto;
import com.example.shoppinglistapi.model.Store;
import com.example.shoppinglistapi.service.StoreService;
import com.example.shoppinglistapi.service.assembler.StoreModelAssembler;
import com.example.shoppinglistapi.service.assembler.StoreSectionModelAssembler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RepositoryRestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(path = "/stores")
public class StoreController {

    private final @NonNull StoreService storeService;
    private final @NonNull StoreModelAssembler storeModelAssembler;
    private final @NonNull StoreSectionModelAssembler storeSectionModelAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<StoreReadDto>> getAllStores() {
        return ResponseEntity.ok(storeModelAssembler.toCollectionModel(storeService.getAllStores()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<StoreReadDto> getStoreById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(storeModelAssembler.toModel(storeService.getStoreById(id)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> addStore(@RequestBody @Valid StoreCreateDto createDto) {
        try {
            Store addedStore = storeService.createStore(createDto);
            StoreReadDto responseDto = storeModelAssembler.toModel(addedStore);
            return ResponseEntity.created(URI.create(responseDto.getLink("self").orElseThrow().getHref()))
                    .body(responseDto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> removeStore(@PathVariable("id") Long id) {
        try {
            storeService.removeStoreById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/{id}/storeSections")
    public ResponseEntity<?> getStoreSections(@PathVariable("id") Long id) {
        return ResponseEntity.ok(
                storeSectionModelAssembler.toCollectionModel(storeService.getStoreSectionsByStoreId(id)));
    }


    @PostMapping(path = "/{id}/storeSections")
    public ResponseEntity<?> addStoreSection(@PathVariable("id") Long id, @RequestBody List<StoreSectionCreateDto> createDtos) {
        try{
            return ResponseEntity.ok(
                    storeSectionModelAssembler.toCollectionModel(storeService.setStoreSections(id, createDtos)));
        }catch (DataIntegrityViolationException | IllegalAccessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping(path = "/{storeId}/storeSections/{storeSectionId}")
    public ResponseEntity<?>  removeStoreSection(@PathVariable("storeId") Long storeId,
                                                 @PathVariable("storeId") Long storeSectionId) {
        try{
            storeService.removeStoreSection(storeId, storeSectionId);
            return ResponseEntity.noContent().build();
        }catch (EntityNotFoundException | IllegalAccessException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
