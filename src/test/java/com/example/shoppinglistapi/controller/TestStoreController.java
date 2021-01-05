package com.example.shoppinglistapi.controller;

import com.example.shoppinglistapi.TestModelMapperConfiguration;
import com.example.shoppinglistapi.model.Store;
import com.example.shoppinglistapi.repository.StoreRepository;
import com.example.shoppinglistapi.service.assembler.StoreModelAssembler;
import com.example.shoppinglistapi.service.tools.normalizer.Alphabet;
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

import java.util.Arrays;
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
@ContextConfiguration(classes = {StoreModelAssembler.class})
@WebMvcTest(controllers = StoreController.class)
@Import({StoreController.class, TestModelMapperConfiguration.class})
@AutoConfigureRestDocs(outputDir = "target/generated-snippets/stores")
public class TestStoreController {

    @MockBean
    private StoreRepository storeRepository;

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
        when(storeRepository.findAll()).thenReturn(stores);
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
        when(storeRepository.findById(1L)).thenReturn(Optional.ofNullable(store1));
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
        when(storeRepository.findById(1L)).thenReturn(Optional.empty());
        mockMvc.perform(get("/stores/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddStoreSuccessful() throws Exception {
        String postBody = "{\"name\": \"Store One\"}";
        when(storeRepository.saveAndFlush(Mockito.any(Store.class))).thenAnswer(s -> {
            Store addedStore = s.getArgument(0);
            addedStore.setId(1L);
            return addedStore;
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
        when(storeRepository.saveAndFlush(Mockito.any(Store.class))).thenAnswer(s -> {
            Store addedStore = s.getArgument(0);
            addedStore.setId(1L);
            return addedStore;
        });
        mockMvc.perform(post("/stores")
                .contentType(MediaTypes.HAL_JSON)
                .content(postBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Unable to create store: " +
                        "Name 'Store Ĉ' does not match the alphabet POLISH."));
    }

    @Test
    public void testRemoveStoreSuccessful() throws Exception {
        when(storeRepository.existsById(1L)).thenReturn(true);
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
        when(storeRepository.existsById(1L)).thenReturn(false);
        mockMvc.perform(delete("/stores/{id}", 1))
                .andExpect(status().isNotFound());
    }
}
