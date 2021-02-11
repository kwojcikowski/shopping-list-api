package com.example.shoppinglistapi.service.impl;

import com.example.shoppinglistapi.dto.store.StoreCreateDto;
import com.example.shoppinglistapi.model.Store;
import com.example.shoppinglistapi.repository.StoreRepository;
import com.example.shoppinglistapi.service.StoreService;
import com.example.shoppinglistapi.service.tools.normalizer.Alphabet;
import com.example.shoppinglistapi.service.tools.normalizer.NormalizationException;
import com.example.shoppinglistapi.service.tools.normalizer.StringNormalizer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    @NonNull
    private final StoreRepository storeRepository;
    @NonNull
    private final Alphabet activeAlphabet;

    @Override
    public Iterable<Store> getAllStores() {
        return storeRepository.findAll();
    }

    @Override
    public Store getStoreById(Long id) throws EntityNotFoundException {
        return storeRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Unable to fetch store: Store with a given id does not exist."));
    }

    @Override
    public Store createStore(StoreCreateDto storeCreateDto) throws IllegalArgumentException {
        String storeName = storeCreateDto.name;
        try {
            storeName = StringNormalizer.normalize(storeName, activeAlphabet);
        } catch (NormalizationException e) {
            throw new IllegalArgumentException("Unable to create store: Name '" + storeName
                    + "' does not match the alphabet " + activeAlphabet.name() + ".");
        }
        Store storeToAdd = Store.builder()
                .name(storeName)
                .urlFriendlyName(storeName.replace(' ', '-').toLowerCase())
                .build();
        return storeRepository.save(storeToAdd);
    }

    @Override
    public void removeStoreById(Long id) throws EntityNotFoundException {
        if (storeRepository.existsById(id))
            storeRepository.deleteById(id);
        else
            throw new EntityNotFoundException("Unable to delete store: " +
                    "Store with given id does not exist.");
    }
}
