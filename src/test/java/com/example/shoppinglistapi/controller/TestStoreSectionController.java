package com.example.shoppinglistapi.controller;


import com.example.shoppinglistapi.TestModelMapperConfiguration;
import com.example.shoppinglistapi.model.Section;
import com.example.shoppinglistapi.model.Store;
import com.example.shoppinglistapi.model.StoreSection;
import com.example.shoppinglistapi.repository.StoreSectionRepository;
import com.example.shoppinglistapi.service.assembler.StoreSectionModelAssembler;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import static org.mockito.ArgumentMatchers.*;
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
@ContextConfiguration(classes = {StoreSectionModelAssembler.class})
@WebMvcTest(controllers = StoreSectionController.class)
@Import({StoreSectionController.class, TestModelMapperConfiguration.class})
@AutoConfigureRestDocs(outputDir = "target/generated-snippets/store-sections")
public class TestStoreSectionController {

    @MockBean
    private StoreSectionRepository storeSectionRepository;

    @Autowired
    private MockMvc mockMvc;

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
                .andExpect(jsonPath("$._embedded.storeSections", hasSize(storeSections.size())))
                .andDo(document("get-all-store-sections",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("_embedded.storeSections[].id")
                                        .description("A unique identifier for this store section."),
                                subsectionWithPath("_embedded.storeSections[].store")
                                        .description("Store object that the relation refers to."),
                                subsectionWithPath("_embedded.storeSections[].section")
                                        .description("Section in a store"),
                                fieldWithPath("_embedded.storeSections[].position")
                                        .description("Position of a section in a store counting from a store entrance."),
                                subsectionWithPath("_embedded.storeSections[]._links")
                                        .description("Links to resources.")
                        )));
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
        mockMvc.perform(get("/storeSections/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andDo(document("get-store-section-by-id",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("The id of a store section to be fetched")
                        ),
                        responseFields(
                                fieldWithPath("id")
                                        .description("A unique identifier for this store section."),
                                subsectionWithPath("store")
                                        .description("Store object that the relation refers to."),
                                subsectionWithPath("section")
                                        .description("Section in a store"),
                                fieldWithPath("position")
                                        .description("Position of a section in a store counting from a store entrance."),
                                subsectionWithPath("_links")
                                        .description("Links to resources.")
                        ),
                        links(
                                linkWithRel("self").description("Link with a self reference to a store section.")
                        )));
    }

    @Test
    public void testGetStoreSectionByIdReturnNotFoundOnNonExistingId() throws Exception {
        when(storeSectionRepository.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/storeSections/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddStoreSectionSuccessful() throws Exception {
        String postBody =
                "{" +
                "\"store\": {" +
                "   \"id\": 1," +
                "   \"name\": \"Store\"," +
                "   \"urlFriendlyName\": \"Store-one\"}," +
                "\"section\": {" +
                "   \"id\": 1," +
                "   \"name\": \"Section One\"}," +
                "\"position\": 3" +
                "}";
        when(storeSectionRepository.saveAndFlush(any(StoreSection.class)))
                .thenAnswer(s -> {
                    StoreSection addedStoreSection = s.getArgument(0);
                    addedStoreSection.setId(1L);
                    return addedStoreSection;
                });
        mockMvc.perform(post("/storeSections")
                    .contentType(MediaTypes.HAL_JSON)
                    .content(postBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andDo(document("add-store-section",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                subsectionWithPath("store")
                                        .description("Store object that the relation refers to."),
                                subsectionWithPath("section")
                                        .description("Section in a store"),
                                fieldWithPath("position")
                                        .description("Position of a section in a store counting from a store entrance.")
                        ),
                        responseFields(
                                fieldWithPath("id")
                                        .description("A unique identifier for this store section."),
                                subsectionWithPath("store")
                                        .description("Store object that the relation refers to."),
                                subsectionWithPath("section")
                                        .description("Section in a store"),
                                fieldWithPath("position")
                                        .description("Position of a section in a store counting from a store entrance."),
                                subsectionWithPath("_links")
                                        .description("Links to resources.")
                        ),
                        links(
                                linkWithRel("self").description("Link with a self reference to a store section.")
                        )));
    }

    @Test
    public void testUpdateStoreSectionsSuccessful() throws Exception {
        String patchBody = "[{" +
                "\"id\": 1," +
                "\"store\": {" +
                "   \"id\": 1," +
                "   \"name\": \"Store\"," +
                "   \"urlFriendlyName\": \"Store-one\"}," +
                "\"section\": {" +
                "   \"id\": 1," +
                "   \"name\": \"Section One\"}," +
                "\"position\": 3" +
                "}," +
                "{" +
                "\"id\": 2," +
                "\"store\": {" +
                "   \"id\": 1," +
                "   \"name\": \"Store\"," +
                "   \"urlFriendlyName\": \"Store-one\"}," +
                "\"section\": {" +
                "   \"id\": 2," +
                "   \"name\": \"Section Two\"}," +
                "\"position\": 4" +
                "}]";
        when(storeSectionRepository.saveAll(anyIterable())).thenReturn(anyList());
        mockMvc.perform(patch("/storeSections")
                    .contentType(MediaTypes.HAL_JSON)
                    .content(patchBody))
                .andExpect(status().isNoContent())
                .andDo(document("update-store-section",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("[]").description("A collection of store sections to update."),
                                fieldWithPath("[].id")
                                        .description("A unique identifier for this store section."),
                                subsectionWithPath("[].store")
                                        .description("Store object that the relation refers to."),
                                subsectionWithPath("[].section")
                                        .description("Section in a store"),
                                fieldWithPath("[].position")
                                        .description("Position of a section in a store counting from a store entrance.")
                        )));
    }

    @Test
    public void testRemoveStoreSectionSuccessful() throws Exception {
        when(storeSectionRepository.existsById(1L)).thenReturn(true);
        mockMvc.perform(delete("/storeSections/{id}", 1))
                .andExpect(status().isNoContent())
                .andDo(document("remove-store-section",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("The id of a store section to be removed.")
                        )));
    }

    @Test
    public void testRemoveStoreSectionReturnNotFoundOnNonExistingId() throws Exception {
        when(storeSectionRepository.existsById(1L)).thenReturn(false);
        mockMvc.perform(delete("/storeSections/1"))
                .andExpect(status().isNotFound());
    }
}
