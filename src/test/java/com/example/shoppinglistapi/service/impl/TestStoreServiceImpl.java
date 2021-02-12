package com.example.shoppinglistapi.service.impl;

import com.example.shoppinglistapi.dto.store.StoreCreateDto;
import com.example.shoppinglistapi.dto.storesection.StoreSectionCreateDto;
import com.example.shoppinglistapi.model.Section;
import com.example.shoppinglistapi.model.Store;
import com.example.shoppinglistapi.model.StoreSection;
import com.example.shoppinglistapi.repository.SectionRepository;
import com.example.shoppinglistapi.repository.StoreRepository;
import com.example.shoppinglistapi.repository.StoreSectionRepository;
import com.example.shoppinglistapi.service.StoreService;
import com.example.shoppinglistapi.service.tools.normalizer.Alphabet;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class TestStoreServiceImpl {

    private final Alphabet activeAlphabet = Alphabet.POLISH;
    private final StoreRepository storeRepository = mock(StoreRepository.class);
    private final SectionRepository sectionRepository = mock(SectionRepository.class);
    private final StoreSectionRepository storeSectionRepository = mock(StoreSectionRepository.class);

    private final StoreService storeService = new StoreServiceImpl(storeRepository, sectionRepository,
            storeSectionRepository, activeAlphabet);

    @Test
    public void testGetAllStores() {
        Store store1 = Store.builder()
                .id(1L)
                .name("Store One")
                .urlFriendlyName("store-one")
                .build();
        Store store2 = Store.builder()
                .id(2L)
                .name("Store Two")
                .urlFriendlyName("store-two")
                .build();
        List<Store> expectedStores = List.of(store1, store2);
        when(storeRepository.findAll()).thenReturn(expectedStores);

        Iterable<Store> actualStores = assertDoesNotThrow(storeService::getAllStores);
        assertThat(actualStores).isEqualTo(expectedStores);
    }

    @Test
    public void testGetStoreById() {
        Store expectedStore = Store.builder()
                .id(1L)
                .name("Store One")
                .urlFriendlyName("store-one")
                .build();
        when(storeRepository.findById(1L)).thenReturn(Optional.ofNullable(expectedStore));
        Store actualStore = assertDoesNotThrow(
                () -> storeService.getStoreById(1L)
        );

        assertThat(actualStore).isEqualTo(expectedStore);
    }

    @Test
    public void testGetStoreByIdThrowExceptionOnNonExistingStore() {
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());
        Exception thrownException = assertThrows(
                EntityNotFoundException.class,
                () -> storeService.getStoreById(1L),
                "EntityNotFoundException should be thrown on fetching non existing store."
        );
        assertThat(thrownException.getMessage())
                .isEqualTo("Unable to fetch store: Store with a given id does not exist.");
    }

    @Test
    public void testCreateStore() {
        StoreCreateDto createDto = StoreCreateDto.builder()
                .name("New store")
                .build();

        when(storeRepository.save(any(Store.class))).thenAnswer(
                params -> {
                    Store store = params.getArgument(0, Store.class);
                    store.setId(1L);
                    return store;
                }
        );

        Store expectedStore = Store.builder()
                .id(1L)
                .name("New store")
                .urlFriendlyName("new-store")
                .build();
        Store actualStore = assertDoesNotThrow(
                () -> storeService.createStore(createDto),
                "Exception should not had been thrown on correct createStore data."
        );
        assertThat(actualStore).isEqualTo(expectedStore);
    }

    @Test
    public void testCreateStoreThrowExceptionOnIncorrectStoreName() {
        StoreCreateDto createDto = StoreCreateDto.builder()
                .name("New store.")
                .build();
        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> storeService.createStore(createDto),
                "IllegalArgumentException should be thrown on creating store with incorrect name"
        );
        assertThat(exception.getMessage()).isEqualTo("Unable to create store: Name '" + createDto.getName()
                + "' does not match the alphabet " + activeAlphabet.name() + ".");
    }

    @Test
    public void testRemoveStoreById() {
        when(storeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(storeRepository).deleteById(1L);
        assertDoesNotThrow(
                () -> storeService.removeStoreById(1L)
        );
    }

    @Test
    public void testRemoveStoreByIdThrowExceptionOnNonExistingStore() {
        when(storeRepository.existsById(1L)).thenReturn(false);
        doNothing().when(storeRepository).deleteById(1L);
        Exception thrownException = assertThrows(
                EntityNotFoundException.class,
                () -> storeService.removeStoreById(1L),
                "EntityNotFoundException should be thrown on deleting non existing store."
        );
        assertThat(thrownException.getMessage()).isEqualTo("Unable to delete store: " +
                "Store with given id does not exist.");
    }

    @Test
    public void testGetAllStoreSections() {
        Store store = Store.builder()
                .id(1L)
                .name("Store One")
                .urlFriendlyName("store-one")
                .build();
        Section fruits = Section.builder()
                .id(1L)
                .name("Fruits")
                .build();
        Section vegetables = Section.builder()
                .id(2L)
                .name("Vegetables")
                .build();
        StoreSection storeFruits = StoreSection.builder()
                .id(1L)
                .store(store)
                .section(fruits)
                .position(1)
                .build();
        StoreSection storeVegetables = StoreSection.builder()
                .id(1L)
                .store(store)
                .section(vegetables)
                .position(2)
                .build();
        List<StoreSection> storeSections = List.of(storeFruits, storeVegetables);
        when(storeSectionRepository.findAllByStore_Id(1L)).thenReturn(storeSections);
        List<StoreSection> actualStoreSections = assertDoesNotThrow(
                () -> storeService.getStoreSectionsByStoreId(1L),
                "No exception should be thrown on correct store sections fetch."
        );
        assertThat(actualStoreSections).isEqualTo(storeSections);
    }

    @Test
    public void testSetStoreSections() {
        Store groceryStore = Store.builder()
                .id(1L)
                .name("Store One")
                .urlFriendlyName("store-one")
                .build();
        Section fruits = Section.builder()
                .id(1L)
                .name("Fruits")
                .build();
        Section vegetables = Section.builder()
                .id(2L)
                .name("Vegetables")
                .build();
        StoreSectionCreateDto groceryFruitsDto = StoreSectionCreateDto.builder()
                .storeId(groceryStore.getId())
                .sectionId(fruits.getId())
                .position(1)
                .build();
        StoreSectionCreateDto groceryVegetablesDto = StoreSectionCreateDto.builder()
                .storeId(groceryStore.getId())
                .sectionId(vegetables.getId())
                .position(2)
                .build();

        doNothing().when(storeSectionRepository).removeAllByStore_Id(1L);
        when(storeRepository.findById(1L)).thenReturn(Optional.of(groceryStore));
        when(sectionRepository.findById(1L)).thenReturn(Optional.of(fruits));
        when(sectionRepository.findById(2L)).thenReturn(Optional.of(vegetables));

        StoreSection groceryFruits = StoreSection.builder()
                .id(1L)
                .store(groceryStore)
                .section(fruits)
                .position(1)
                .build();
        StoreSection groceryVegetables = StoreSection.builder()
                .id(2L)
                .store(groceryStore)
                .section(vegetables)
                .position(2)
                .build();
        List<StoreSection> expectedStoreSections = List.of(groceryFruits, groceryVegetables);
        when(storeSectionRepository.saveAll(any())).thenReturn(expectedStoreSections);

        List<StoreSection> actualStoreSections = assertDoesNotThrow(
                () -> storeService.setStoreSections(1L, List.of(groceryFruitsDto, groceryVegetablesDto)),
                "Exception should not had been thrown on correct setStoreSections call."
        );
        assertThat(actualStoreSections).isEqualTo(expectedStoreSections);
    }

    @Test
    public void testSetStoreSectionsThrowExceptionOnNonUniquePositions() {
        StoreSectionCreateDto groceryFruitsDto = StoreSectionCreateDto.builder()
                .storeId(1L)
                .sectionId(1L)
                .position(1)
                .build();
        StoreSectionCreateDto groceryVegetablesDto = StoreSectionCreateDto.builder()
                .storeId(1L)
                .sectionId(2L)
                .position(1)
                .build();
        Exception thrownException = assertThrows(
                DataIntegrityViolationException.class,
                () -> storeService.setStoreSections(1L, List.of(groceryFruitsDto, groceryVegetablesDto)),
                "DataIntegrityViolationException should be thrown on non unique store sections positions."
        );
        assertThat(thrownException.getMessage()).isEqualTo("Unable to update store sections: " +
                "Each section of a store must have a unique position.");
    }

    @Test
    public void testSetStoreSectionsThrowExceptionOnIncorrectStoreId() {
        StoreSectionCreateDto groceryFruitsDto = StoreSectionCreateDto.builder()
                .storeId(1L)
                .sectionId(1L)
                .position(1)
                .build();
        StoreSectionCreateDto groceryVegetablesDto = StoreSectionCreateDto.builder()
                .storeId(2L)
                .sectionId(2L)
                .position(2)
                .build();
        Exception thrownException = assertThrows(
                IllegalAccessException.class,
                () -> storeService.setStoreSections(1L, List.of(groceryFruitsDto, groceryVegetablesDto)),
                "IllegalAccessException should be thrown on store section having different store id than provided."
        );
        assertThat(thrownException.getMessage()).isEqualTo("Unable to update store sections: " +
                "At least one of the given store sections does not refer to selected store.");
    }

    @Test
    public void testSetStoreSectionsThrowExceptionOnNonExistingSection() {
        Store groceryStore = Store.builder()
                .id(1L)
                .name("Store One")
                .urlFriendlyName("store-one")
                .build();
        Section fruits = Section.builder()
                .id(1L)
                .name("Fruits")
                .build();
        Section vegetables = Section.builder()
                .id(2L)
                .name("Vegetables")
                .build();
        StoreSectionCreateDto groceryFruitsDto = StoreSectionCreateDto.builder()
                .storeId(groceryStore.getId())
                .sectionId(fruits.getId())
                .position(1)
                .build();
        StoreSectionCreateDto groceryVegetablesDto = StoreSectionCreateDto.builder()
                .storeId(groceryStore.getId())
                .sectionId(vegetables.getId())
                .position(2)
                .build();

        doNothing().when(storeSectionRepository).removeAllByStore_Id(1L);
        when(storeRepository.findById(1L)).thenReturn(Optional.of(groceryStore));
        when(sectionRepository.findById(1L)).thenReturn(Optional.of(fruits));
        when(sectionRepository.findById(2L)).thenReturn(Optional.empty());

        Exception thrownException = assertThrows(
                EntityNotFoundException.class,
                () -> storeService.setStoreSections(1L, List.of(groceryFruitsDto, groceryVegetablesDto)),
                "EntityNotFoundException should be thrown on non existing section."
        );
        assertThat(thrownException.getMessage()).isEqualTo("Unable to update store sections: " +
                "Section with given id does not exist.");
    }

    @Test
    public void testRemoveStoreSection() {
        Store groceryStore = Store.builder()
                .id(1L)
                .name("Store One")
                .urlFriendlyName("store-one")
                .build();
        Section fruits = Section.builder()
                .id(1L)
                .name("Fruits")
                .build();
        StoreSection groceryFruits = StoreSection.builder()
                .id(1L)
                .store(groceryStore)
                .section(fruits)
                .position(1)
                .build();
        when(storeSectionRepository.findById(1L)).thenReturn(Optional.ofNullable(groceryFruits));
        doNothing().when(storeSectionRepository).deleteById(1L);
        assertDoesNotThrow(
                () -> storeService.removeStoreSection(1L, 1L),
                "Exception should not had been thrown on correct removeStoreSections call."
        );
    }

    @Test
    public void testRemoveStoreSectionThrowExceptionOnNonExistingStoreSection() {
        when(storeSectionRepository.findById(1L)).thenReturn(Optional.empty());
        Exception thrownException = assertThrows(
                EntityNotFoundException.class,
                () -> storeService.removeStoreSection(1L, 1L),
                "EntityNotFoundException should had been thrown on non existing store section."
        );
        assertThat(thrownException.getMessage()).isEqualTo("Unable to delete store section: " +
                "Store section with given id does not exist.");
    }

    @Test
    public void testRemoveStoreSectionThrowExceptionOnMismatchingStoreIds() {
        Store groceryStore = Store.builder()
                .id(1L)
                .name("Store One")
                .urlFriendlyName("store-one")
                .build();
        Section fruits = Section.builder()
                .id(1L)
                .name("Fruits")
                .build();
        StoreSection groceryFruits = StoreSection.builder()
                .id(1L)
                .store(groceryStore)
                .section(fruits)
                .position(1)
                .build();
        when(storeSectionRepository.findById(1L)).thenReturn(Optional.ofNullable(groceryFruits));
        Exception thrownException = assertThrows(
                IllegalAccessException.class,
                () -> storeService.removeStoreSection(2L, 1L),
                "IllegalAccessException should be thrown on non mismatched store ids."
        );
        assertThat(thrownException.getMessage()).isEqualTo("Unable to delete store section: " +
                "Given store section does not refer to this store.");
    }
}
