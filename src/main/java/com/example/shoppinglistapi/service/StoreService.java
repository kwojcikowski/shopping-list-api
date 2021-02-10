package com.example.shoppinglistapi.service;

import com.example.shoppinglistapi.dto.store.StoreCreateDto;
import com.example.shoppinglistapi.model.Store;

import javax.persistence.EntityNotFoundException;

public interface StoreService {
    public Iterable<Store> getAllStores();
    public Store getStoreById(Long id) throws EntityNotFoundException;
    public Store createStore(StoreCreateDto storeCreateDto) throws IllegalArgumentException;
    public void removeStoreById(Long id) throws EntityNotFoundException;
}
