package com.example.shoppinglistapi.service;

import com.example.shoppinglistapi.dto.store.StoreCreateDto;
import com.example.shoppinglistapi.dto.storesection.StoreSectionCreateDto;
import com.example.shoppinglistapi.model.Store;
import com.example.shoppinglistapi.model.StoreSection;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface StoreService {
    Iterable<Store> getAllStores();
    Store getStoreById(Long id) throws EntityNotFoundException;
    Store createStore(StoreCreateDto storeCreateDto) throws IllegalArgumentException;
    void removeStoreById(Long id) throws EntityNotFoundException;

    List<StoreSection> getStoreSectionsByStoreId(Long storeId);
    List<StoreSection> setStoreSections(Long storeId, List<StoreSectionCreateDto> createDtos)
            throws EntityNotFoundException, IllegalAccessException;
    void removeStoreSection(Long storeId, Long storeSectionId) throws EntityNotFoundException, IllegalAccessException;
}
