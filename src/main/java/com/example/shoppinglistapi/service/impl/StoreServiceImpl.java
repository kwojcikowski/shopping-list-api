package com.example.shoppinglistapi.service.impl;

import com.example.shoppinglistapi.dto.store.StoreCreateDto;
import com.example.shoppinglistapi.dto.storesection.StoreSectionCreateDto;
import com.example.shoppinglistapi.model.Store;
import com.example.shoppinglistapi.model.StoreSection;
import com.example.shoppinglistapi.repository.SectionRepository;
import com.example.shoppinglistapi.repository.StoreRepository;
import com.example.shoppinglistapi.repository.StoreSectionRepository;
import com.example.shoppinglistapi.service.StoreService;
import com.example.shoppinglistapi.service.tools.normalizer.Alphabet;
import com.example.shoppinglistapi.service.tools.normalizer.NormalizationException;
import com.example.shoppinglistapi.service.tools.normalizer.StringNormalizer;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreServiceImpl implements StoreService {

    @NonNull
    private final StoreRepository storeRepository;
    @NonNull
    private final SectionRepository sectionRepository;
    @NonNull
    private final StoreSectionRepository storeSectionRepository;
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

    @Override
    public List<StoreSection> getStoreSectionsByStoreId(Long storeId) {
        try {
            Store store = getStoreById(storeId);
            return storeSectionRepository.findAllByStore_IdOrderByPosition(store.getId());
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Unable to fetch store sections: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public List<StoreSection> setStoreSections(Long storeId, List<StoreSectionCreateDto> createDtos)
            throws EntityNotFoundException, IllegalAccessException {

        List<Integer> positions = createDtos.stream().map(dto -> dto.position).collect(Collectors.toList());
        if (createDtos.stream().anyMatch(updateDto -> Collections.frequency(positions, updateDto.position) > 1))
            throw new DataIntegrityViolationException("Unable to update store sections: " +
                    "Each section of a store must have a unique position.");

        if (createDtos.stream().anyMatch(updateDto -> !updateDto.storeId.equals(storeId)))
            throw new IllegalAccessException("Unable to update store sections: " +
                    "At least one of the given store sections does not refer to selected store.");

        storeSectionRepository.removeAllByStore_Id(storeId);

        Store selectedStore = getStoreById(storeId);
        return storeSectionRepository.saveAll(createDtos.stream()
                .map(updateDto -> StoreSection.builder()
                        .store(selectedStore)
                        .section(sectionRepository.findById(updateDto.sectionId)
                                .orElseThrow(() -> new EntityNotFoundException("Unable to update store sections: " +
                                        "Section with given id does not exist.")))
                        .position(updateDto.position)
                        .build())
                .collect(Collectors.toList()));
    }

    @Override
    public void removeStoreSection(Long storeId, Long storeSectionId) throws EntityNotFoundException, IllegalAccessException {
        StoreSection storeSection = storeSectionRepository.findById(storeSectionId)
                .orElseThrow(() -> new EntityNotFoundException("Unable to delete store section: " +
                        "Store section with given id does not exist."));
        if (!storeSection.getStore().getId().equals(storeId))
            throw new IllegalAccessException("Unable to delete store section: " +
                    "Given store section does not refer to this store.");
        storeSectionRepository.deleteById(storeSectionId);
    }

}
