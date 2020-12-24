package com.example.antonapi.controller;

import com.example.antonapi.TestModelMapperConfiguration;
import com.example.antonapi.model.Store;
import com.example.antonapi.repository.StoreRepository;
import com.example.antonapi.service.assembler.StoreModelAssembler;
import com.example.antonapi.service.dto.StoreDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {StoreModelAssembler.class})
@WebMvcTest(controllers = StoreController.class)
@Import({StoreController.class, TestModelMapperConfiguration.class})
public class TestStoreController {

    @MockBean
    private StoreRepository storeRepository;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void testGetAllStoresSuccessful() throws Exception {
        Store store1 = Store.builder()
                .id(1L)
                .name("Store 1")
                .urlFriendlyName("Store-1")
                .build();
        Store store2 = Store.builder()
                .id(2L)
                .name("Store 2")
                .urlFriendlyName("Store-2")
                .build();
        List<Store> stores = List.of(store1, store2);
        when(storeRepository.findAll()).thenReturn(stores);
        mockMvc.perform(get("/stores"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.stores", hasSize(stores.size())));
    }

    @Test
    public void testGetStoreByIdSuccessful() throws Exception {
        Store store1 = Store.builder()
                .id(1L)
                .name("Store 1")
                .urlFriendlyName("Store-1")
                .build();
        when(storeRepository.findById(1L)).thenReturn(Optional.ofNullable(store1));
        mockMvc.perform(get("/stores/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON));
    }

    @Test
    public void testGetStoreByIdReturnNotFoundOnIncorrectId() throws Exception {
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/stores/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddStoreSuccessful() throws Exception {
        Store store = Store.builder()
                .name("Store 1")
                .urlFriendlyName("Store-1")
                .build();
        StoreDTO storeDTO = modelMapper.map(store, StoreDTO.class);
        String postBody = new ObjectMapper().writeValueAsString(storeDTO);
        when(storeRepository.saveAndFlush(Mockito.any(Store.class))).thenAnswer(s -> {
            Store addedStore = s.getArgument(0);
            addedStore.setId(1L);
            return addedStore;
        });
        mockMvc.perform(post("/stores")
                .contentType(MediaTypes.HAL_JSON)
                .content(postBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON));
    }

    @Test
    public void testRemoveStoreSuccessful() throws Exception {
        when(storeRepository.existsById(1L)).thenReturn(true);
        mockMvc.perform(delete("/stores/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testRemoveStoreReturnNotFoundOnNonExistingId() throws Exception {
        when(storeRepository.existsById(1L)).thenReturn(false);
        mockMvc.perform(delete("/stores/1"))
                .andExpect(status().isNotFound());
    }
}
