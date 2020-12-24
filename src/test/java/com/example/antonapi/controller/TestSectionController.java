package com.example.antonapi.controller;

import com.example.antonapi.TestModelMapperConfiguration;
import com.example.antonapi.model.Section;
import com.example.antonapi.repository.SectionRepository;
import com.example.antonapi.service.assembler.SectionModelAssembler;
import com.example.antonapi.service.dto.SectionDTO;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SectionModelAssembler.class})
@WebMvcTest(controllers = SectionController.class)
@Import({SectionController.class, TestModelMapperConfiguration.class})
public class TestSectionController {

    @MockBean
    private SectionRepository sectionRepository;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void testGetAllSectionsSuccessful() throws Exception {
        Section section1 = Section.builder()
                .id(1L)
                .name("Section 1")
                .build();
        Section section2 = Section.builder()
                .id(2L)
                .name("Section 2")
                .build();
        List<Section> sections = List.of(section1, section2);
        when(sectionRepository.findAll()).thenReturn(sections);
        mockMvc.perform(get("/sections"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.sections", hasSize(sections.size())));
    }

    @Test
    public void testGetSectionByIdSuccessful() throws Exception {
        Section section1 = Section.builder()
                .id(1L)
                .name("Section 1")
                .build();
        when(sectionRepository.findById(1L)).thenReturn(Optional.ofNullable(section1));
        mockMvc.perform(get("/sections/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON));
    }

    @Test
    public void testGetSectionByIdReturnNotFoundOnIncorrectId() throws Exception {
        when(sectionRepository.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/sections/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddSectionSuccessful() throws Exception {
        Section section1 = Section.builder()
                .id(1L)
                .name("Section 1")
                .build();
        SectionDTO sectionDTO = modelMapper.map(section1, SectionDTO.class);
        String postBody = new ObjectMapper().writeValueAsString(sectionDTO);
        when(sectionRepository.saveAndFlush(Mockito.any(Section.class))).thenAnswer(s -> {
            Section addedSection = s.getArgument(0);
            addedSection.setId(1L);
            return addedSection;
        });
        mockMvc.perform(post("/sections")
                .contentType(MediaTypes.HAL_JSON)
                .content(postBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON));
    }

    @Test
    public void testRemoveSectionSuccessful() throws Exception {
        when(sectionRepository.existsById(1L)).thenReturn(true);
        mockMvc.perform(delete("/sections/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testRemoveSectionReturnNotFoundOnNonExistingId() throws Exception {
        when(sectionRepository.existsById(1L)).thenReturn(false);
        mockMvc.perform(delete("/sections/1"))
                .andExpect(status().isNotFound());
    }
}
