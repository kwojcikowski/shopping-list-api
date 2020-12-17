package com.example.antonapi.controller;

import com.example.antonapi.model.Store;
import com.example.antonapi.repository.StoreRepository;
import com.example.antonapi.service.assembler.StoreModelAssembler;
import com.example.antonapi.service.dto.StoreDTO;
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
@RequestMapping(path = "/stores")
public class StoreController {

    private final @NonNull StoreRepository storeRepository;
    private final @NonNull StoreModelAssembler storeModelAssembler;
    private final @NonNull ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<CollectionModel<StoreDTO>> getAllStores() {
        return ResponseEntity.ok(storeModelAssembler.toCollectionModel(storeRepository.findAll()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<StoreDTO> getStoreById(@PathVariable("id") Long id){
        Store foundStore = storeRepository.findById(id).orElse(null);
        return foundStore == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(storeModelAssembler.toModel(storeRepository.getOne(id)));
    }

    @PostMapping
    public ResponseEntity<StoreDTO> addProduct(@RequestBody StoreDTO requestStore) {
        Store mapperStore = modelMapper.map(requestStore, Store.class);
        Store addedStore = storeRepository.saveAndFlush(mapperStore);
        return ResponseEntity.ok(storeModelAssembler.toModel(addedStore));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> removeProduct(@PathVariable("id") Long id) {
        if(!storeRepository.existsById(id))
            return ResponseEntity.notFound().build();
        storeRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
