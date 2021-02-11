package com.example.shoppinglistapi.service.impl;

import com.example.shoppinglistapi.dto.store.StoreCreateDto;
import com.example.shoppinglistapi.model.Store;
import com.example.shoppinglistapi.repository.StoreRepository;
import com.example.shoppinglistapi.service.StoreService;
import com.example.shoppinglistapi.service.tools.normalizer.Alphabet;
import org.junit.jupiter.api.Test;

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

    private final StoreService storeService = new StoreServiceImpl(storeRepository, activeAlphabet);

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
}
