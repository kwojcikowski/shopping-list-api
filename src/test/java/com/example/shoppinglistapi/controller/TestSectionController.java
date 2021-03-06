package com.example.shoppinglistapi.controller;

import com.example.shoppinglistapi.TestModelMapperConfiguration;
import com.example.shoppinglistapi.model.Section;
import com.example.shoppinglistapi.repository.SectionRepository;
import com.example.shoppinglistapi.service.assembler.SectionModelAssembler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
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
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {SectionModelAssembler.class})
@WebMvcTest(controllers = SectionController.class)
@Import({SectionController.class, TestModelMapperConfiguration.class})
@AutoConfigureRestDocs(outputDir = "target/generated-snippets/sections")
public class TestSectionController {

    @MockBean
    private SectionRepository sectionRepository;

    @Autowired
    private MockMvc mockMvc;

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
                .andExpect(jsonPath("$._embedded.sections", hasSize(sections.size())))
                .andDo(document("get-all-sections",
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("_embedded.sections[].id").description("The id of a section."),
                                fieldWithPath("_embedded.sections[].name").description("Name of a section."),
                                subsectionWithPath("_embedded.sections[]._links").description("Links to connected resources")
                        )));
    }

    @Test
    public void testGetSectionByIdSuccessful() throws Exception {
        Section section1 = Section.builder()
                .id(1L)
                .name("Section 1")
                .build();
        when(sectionRepository.findById(1L)).thenReturn(Optional.ofNullable(section1));
        mockMvc.perform(get("/sections/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andDo(document("get-section-by-id",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("The id of a section to fetch.")
                        ),
                        responseFields(
                                fieldWithPath("id").description("The id of a section."),
                                fieldWithPath("name").description("Name of a section."),
                                subsectionWithPath("_links").description("Links to connected resources")
                        ),
                        links(
                                linkWithRel("self").description("Link with a self reference to a section.")
                        )));
    }

    @Test
    public void testGetSectionByIdReturnNotFoundOnIncorrectId() throws Exception {
        when(sectionRepository.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/sections/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddSectionSuccessful() throws Exception {
        String postBody = "{\"name\": \"Section 1\"}";
        when(sectionRepository.saveAndFlush(Mockito.any(Section.class))).thenAnswer(s -> {
            Section addedSection = s.getArgument(0);
            addedSection.setId(1L);
            return addedSection;
        });
        mockMvc.perform(post("/sections")
                .contentType(MediaTypes.HAL_JSON)
                .content(postBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andDo(document("add-section",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("Name of a section")
                        ),
                        responseFields(
                                fieldWithPath("id").description("The id of a section."),
                                fieldWithPath("name").description("Name of a section."),
                                subsectionWithPath("_links").description("Links to connected resources")
                        ),
                        links(
                                linkWithRel("self").description("Link with a self reference to a section.")
                        )));
    }

    @Test
    public void testRemoveSectionSuccessful() throws Exception {
        when(sectionRepository.existsById(1L)).thenReturn(true);
        mockMvc.perform(delete("/sections/{id}", 1))
                .andExpect(status().isNoContent())
                .andDo(document("remove-section",
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("Id of a section to remove")
                        )));
    }

    @Test
    public void testRemoveSectionReturnNotFoundOnNonExistingId() throws Exception {
        when(sectionRepository.existsById(1L)).thenReturn(false);
        mockMvc.perform(delete("/sections/1"))
                .andExpect(status().isNotFound());
    }
}
