package com.example.antonapi.controller;


import com.example.antonapi.TestModelMapperConfiguration;
import com.example.antonapi.model.Section;
import com.example.antonapi.model.Store;
import com.example.antonapi.model.StoreSection;
import com.example.antonapi.repository.StoreSectionRepository;
import com.example.antonapi.service.assembler.StoreSectionModelAssembler;
import com.example.antonapi.service.dto.StoreSectionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {StoreSectionModelAssembler.class})
@WebMvcTest(controllers = StoreSectionController.class)
@Import({StoreSectionController.class, TestModelMapperConfiguration.class})
public class TestStoreSectionController {

    @MockBean
    private StoreSectionRepository storeSectionRepository;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ModelMapper modelMapper;

    final Store store = Store.builder()
            .id(1L)
            .name("Store")
            .urlFriendlyName("store")
            .build();
    final Section section1 = Section.builder()
            .id(1L)
            .name("Section 1")
            .build();
    final Section section2 = Section.builder()
            .id(2L)
            .name("Section 2")
            .build();

    @Test
    public void testGetAllStoreSectionsSuccessful() throws Exception {
        StoreSection storeSection1 = StoreSection.builder()
                .id(1L)
                .store(store)
                .section(section1)
                .position(1)
                .build();
        StoreSection storeSection2 = StoreSection.builder()
                .id(2L)
                .store(store)
                .section(section2)
                .position(2)
                .build();
        List<StoreSection> storeSections = List.of(storeSection1, storeSection2);
        when(storeSectionRepository.findAll()).thenReturn(storeSections);
        mockMvc.perform(get("/storeSections"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.storeSections", hasSize(storeSections.size())));
    }

    @Test
    public void testGetStoreSectionByIdSuccessful() throws Exception {
        StoreSection storeSection1 = StoreSection.builder()
                .id(1L)
                .store(store)
                .section(section1)
                .position(1)
                .build();
        when(storeSectionRepository.findById(1L)).thenReturn(Optional.ofNullable(storeSection1));
        mockMvc.perform(get("/storeSections/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON));
    }

    @Test
    public void testGetStoreSectionByIdReturnNotFoundOnNonExistingId() throws Exception {
        when(storeSectionRepository.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/storeSections/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddStoreSectionSuccessful() throws Exception {
        StoreSection storeSectionToAdd = StoreSection.builder()
                .store(store)
                .section(section1)
                .position(1)
                .build();
        StoreSectionDTO storeSectionDTO = modelMapper.map(storeSectionToAdd, StoreSectionDTO.class);
        String postBody = new ObjectMapper().writeValueAsString(storeSectionDTO);
        when(storeSectionRepository.saveAndFlush(storeSectionToAdd))
                .thenAnswer(s -> {
                    StoreSection addedStoreSection = s.getArgument(0);
                    addedStoreSection.setId(1L);
                    return addedStoreSection;
                });
        mockMvc.perform(post("/storeSections")
                    .contentType(MediaTypes.HAL_JSON)
                    .content(postBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON));
    }

    @Test
    public void testUpdateStoreSectionsSuccessful() throws Exception {
        StoreSection storeSection1 = StoreSection.builder()
                .id(1L)
                .store(store)
                .section(section1)
                .position(1)
                .build();
        StoreSection storeSection2 = StoreSection.builder()
                .id(2L)
                .store(store)
                .section(section2)
                .position(2)
                .build();
        List<StoreSection> storeSections = List.of(storeSection1, storeSection2);
        List<StoreSectionDTO> storeSectionDTOs = storeSections
                .stream()
                .map(storeSection -> modelMapper.map(storeSection, StoreSectionDTO.class))
                .collect(Collectors.toList());
        ObjectMapper mapper = new ObjectMapper();
        String patchBody = mapper.writeValueAsString(storeSectionDTOs);
        when(storeSectionRepository.saveAll(anyIterable())).thenReturn(storeSections);
        mockMvc.perform(patch("/storeSections")
                    .contentType(MediaTypes.HAL_JSON)
                    .content(patchBody))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testRemoveStoreSectionSuccessful() throws Exception {
        when(storeSectionRepository.existsById(1L)).thenReturn(true);
        mockMvc.perform(delete("/storeSections/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testRemoveStoreSectionReturnNotFoundOnNonExistingId() throws Exception {
        when(storeSectionRepository.existsById(1L)).thenReturn(false);
        mockMvc.perform(delete("/storeSections/1"))
                .andExpect(status().isNotFound());
    }
}
