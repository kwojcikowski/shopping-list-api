package com.example.shoppinglistapi.controller;

import com.example.shoppinglistapi.dto.store.StoreCreateDto;
import com.example.shoppinglistapi.model.Section;
import com.example.shoppinglistapi.model.Store;
import com.example.shoppinglistapi.model.StoreSection;
import com.example.shoppinglistapi.service.StoreService;
import com.example.shoppinglistapi.service.tools.normalizer.Alphabet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureRestDocs(outputDir = "target/generated-snippets/stores")
@SpringBootTest
@AutoConfigureMockMvc
public class TestStoreController {

    @MockBean
    private StoreService storeService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllStoresSuccessful() throws Exception {
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
        List<Store> stores = List.of(store1, store2);
        when(storeService.getAllStores()).thenReturn(stores);
        mockMvc.perform(get("/stores"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.stores", hasSize(stores.size())))
                .andDo(document("get-all-stores",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("_embedded.stores[].id")
                                        .description("A unique identifier for this store."),
                                fieldWithPath("_embedded.stores[].name")
                                        .description("Name of the store."),
                                fieldWithPath("_embedded.stores[].urlFriendlyName")
                                        .description("Name of the store in a URL-friendly form"),
                                subsectionWithPath("_embedded.stores[]._links")
                                        .description("Links to resources.")
                        )));
    }

    @Test
    public void testGetStoreByIdSuccessful() throws Exception {
        Store store1 = Store.builder()
                .id(1L)
                .name("Store 1")
                .urlFriendlyName("Store-1")
                .build();
        when(storeService.getStoreById(1L)).thenReturn(store1);
        mockMvc.perform(get("/stores/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andDo(document("get-store-by-id",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("The id of a store to be fetched.")
                        ),
                        responseFields(
                                fieldWithPath("id")
                                        .description("A unique identifier for this store."),
                                fieldWithPath("name")
                                        .description("Name of the store."),
                                fieldWithPath("urlFriendlyName")
                                        .description("Name of the store in a URL-friendly form"),
                                subsectionWithPath("_links")
                                        .description("Links to resources.")
                        ),
                        links(
                                linkWithRel("self").description("Link with a self reference to a store.")
                        )));
    }

    @Test
    public void testGetStoreByIdReturnNotFoundOnIncorrectId() throws Exception {
        when(storeService.getStoreById(1L)).thenThrow(new EntityNotFoundException("Unable to delete store: " +
                "Store with given id does not exist."));
        mockMvc.perform(get("/stores/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddStoreSuccessful() throws Exception {
        String postBody = "{\"name\": \"Store One\"}";
        when(storeService.createStore(any(StoreCreateDto.class))).thenAnswer(s -> {
            StoreCreateDto createDto = s.getArgument(0, StoreCreateDto.class);
            return Store.builder()
                    .id(1L)
                    .name(createDto.name)
                    .urlFriendlyName("store-one")
                    .build();
        });
        mockMvc.perform(post("/stores")
                .contentType(MediaTypes.HAL_JSON)
                .content(postBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andDo(document("add-store",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name").description("The name of a store.\n" +
                                        "NOTE! The name has to be compliant with an alphabet " +
                                        "(only letters and spaces allowed).\n" +
                                        "Currently supported alphabets: " + Arrays.toString(Alphabet.values()))
                        ),
                        responseFields(
                                fieldWithPath("id").description("The id of a store."),
                                fieldWithPath("name").description("The name of a store."),
                                fieldWithPath("urlFriendlyName").description("Name of a store that could be then used in URLs."),
                                subsectionWithPath("_links").description("Links to resources")
                        ),
                        links(
                                linkWithRel("self").description("Link with a self reference to a store.")
                        )));
    }

    @Test
    public void testAddStoreReturnBadRequestOnStoreNameNotMatchingAlphabet() throws Exception {
        String postBody = "{\"name\": \"Store Ĉ\"}";
        when(storeService.createStore(any(StoreCreateDto.class)))
                .thenThrow(new IllegalArgumentException("Unable to create store: " +
                        "Name 'Store Ĉ' does not match the alphabet POLISH."));
        mockMvc.perform(post("/stores")
                .contentType(MediaTypes.HAL_JSON)
                .characterEncoding("utf-8")
                .content(postBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Unable to create store: " +
                        "Name 'Store Ĉ' does not match the alphabet POLISH.\""));
    }

    @Test
    public void testRemoveStoreSuccessful() throws Exception {
        doNothing().when(storeService).removeStoreById(1L);
        mockMvc.perform(delete("/stores/{id}", 1))
                .andExpect(status().isNoContent())
                .andDo(document("remove-store",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("Id of a store to be removed.")
                        )));
    }

    @Test
    public void testRemoveStoreReturnNotFoundOnNonExistingId() throws Exception {
        doThrow(new EntityNotFoundException("Unable to delete store: " +
                "Store with given id does not exist.")).when(storeService).removeStoreById(1L);
        mockMvc.perform(delete("/stores/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetStoreSections() throws Exception {
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
        List<StoreSection> storeSections = List.of(groceryFruits, groceryVegetables);
        when(storeService.getStoreSectionsByStoreId(1L)).thenReturn(storeSections);
        mockMvc.perform(get("/stores/{storeId}/storeSections", 1))
                .andExpect(status().isOk())
                .andDo(document("get-store-sections",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("_embedded.storeSections[].id").description("The id of a store section."),
                                subsectionWithPath("_embedded.storeSections[].store").description("Store details"),
                                subsectionWithPath("_embedded.storeSections[].section").description("Section details"),
                                fieldWithPath("_embedded.storeSections[].position").description("Position of a section in a store")
                        )));
    }

    @Test
    public void testSetStoreSections() throws Exception {
        String postBody = "[" +
                "{" +
                "\"storeId\": 1," +
                "\"sectionId\": 1," +
                "\"position\": 1" +
                "}," +
                "{" +
                "\"storeId\": 1," +
                "\"sectionId\": 2," +
                "\"position\": 2" +
                "}" +
                "]";
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
        List<StoreSection> storeSections = List.of(groceryFruits, groceryVegetables);
        when(storeService.setStoreSections(anyLong(), anyList())).thenReturn(storeSections);
        mockMvc.perform(post("/stores/{storeId}/storeSections", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(postBody))
                .andExpect(status().isOk())
                .andDo(document("set-store-sections",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("storeId").description("Id of a store")
                        ),
                        responseFields(
                                fieldWithPath("_embedded.storeSections[].id").description("The id of a store section."),
                                subsectionWithPath("_embedded.storeSections[].store").description("Store details"),
                                subsectionWithPath("_embedded.storeSections[].section").description("Section details"),
                                fieldWithPath("_embedded.storeSections[].position").description("Position of a section in a store")
                        )));
    }

    @Test
    public void testSetStoreSectionsReturnBadRequestOnNonUniquePosition() throws Exception {
        String postBody = "[" +
                "{" +
                "\"storeId\": 1," +
                "\"sectionId\": 1," +
                "\"position\": 1" +
                "}," +
                "{" +
                "\"storeId\": 1," +
                "\"sectionId\": 2," +
                "\"position\": 1" +
                "}" +
                "]";
        when(storeService.setStoreSections(anyLong(), anyList()))
                .thenThrow(new DataIntegrityViolationException("Unable to update store sections: " +
                        "Each section of a store must have a unique position."));
        mockMvc.perform(post("/stores/{storeId}/storeSections", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(postBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Unable to update store sections: " +
                        "Each section of a store must have a unique position.\""))
                .andDo(document("set-store-sections-non-unique-position",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("storeId").description("Id of a store")
                        )));
    }

    @Test
    public void testSetStoreSectionsReturnBadRequestOnWrongStoreReference() throws Exception {
        String postBody = "[" +
                "{" +
                "\"storeId\": 1," +
                "\"sectionId\": 1," +
                "\"position\": 1" +
                "}," +
                "{" +
                "\"storeId\": 2," +
                "\"sectionId\": 2," +
                "\"position\": 3" +
                "}" +
                "]";
        when(storeService.setStoreSections(anyLong(), anyList()))
                .thenThrow(new IllegalAccessException("Unable to update store sections: " +
                        "At least one of the given store sections does not refer to selected store."));
        mockMvc.perform(post("/stores/{storeId}/storeSections", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(postBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Unable to update store sections: " +
                        "At least one of the given store sections does not refer to selected store.\""))
                .andDo(document("set-store-sections-wrong-store-ref",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("storeId").description("Id of a store")
                        )));
    }

    @Test
    public void testSetStoreSectionsReturnBadRequestOnNonExistingSection() throws Exception {
        String postBody = "[" +
                "{" +
                "\"storeId\": 1," +
                "\"sectionId\": 1," +
                "\"position\": 1" +
                "}," +
                "{" +
                "\"storeId\": 1," +
                "\"sectionId\": 99," +
                "\"position\": 2" +
                "}" +
                "]";
        when(storeService.setStoreSections(anyLong(), anyList()))
                .thenThrow(new EntityNotFoundException("Unable to update store sections: " +
                        "Section with given id does not exist."));
        mockMvc.perform(post("/stores/{storeId}/storeSections", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(postBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Unable to update store sections: " +
                        "Section with given id does not exist.\""))
                .andDo(document("set-store-sections-non-existing-store",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("storeId").description("Id of a store")
                        )));
    }

    @Test
    public void testRemoveStoreSection() throws Exception {
        doNothing().when(storeService).removeStoreSection(1L, 1L);
        mockMvc.perform(delete("/stores/{storeId}/storeSections/{storeSectionId}", 1, 1))
                .andExpect(status().isNoContent())
                .andDo(document("remove-store-section",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("storeId").description("Id of a store"),
                                parameterWithName("storeSectionId").description("Id of a store section")
                        )));
    }

    @Test
    public void testRemoveStoreSectionReturnBadRequestOnNonExistingStoreSection() throws Exception {
        doThrow(new EntityNotFoundException("Unable to delete store section: " +
                "Store section with given id does not exist."))
                .when(storeService).removeStoreSection(1L, 99L);
        mockMvc.perform(delete("/stores/{storeId}/storeSections/{storeSectionId}", 1, 99))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Unable to delete store section: " +
                        "Store section with given id does not exist.\""))
                .andDo(document("remove-store-section-non-existing-store-section",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("storeId").description("Id of a store"),
                                parameterWithName("storeSectionId").description("Id of a store section")
                        )));
    }

    @Test
    public void testRemoveStoreSectionReturnBadRequestOnNonWrongStoreReference() throws Exception {
        doThrow(new IllegalAccessException("Unable to delete store section: " +
                "Given store section does not refer to this store."))
                .when(storeService).removeStoreSection(99L, 1L);
        mockMvc.perform(delete("/stores/{storeId}/storeSections/{storeSectionId}", 99, 1))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("\"Unable to delete store section: " +
                        "Given store section does not refer to this store.\""))
                .andDo(document("remove-store-section-wrong-store-ref",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("storeId").description("Id of a store"),
                                parameterWithName("storeSectionId").description("Id of a store section")
                        )));
    }
}
