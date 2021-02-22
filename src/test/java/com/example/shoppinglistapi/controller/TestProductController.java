package com.example.shoppinglistapi.controller;

import com.example.shoppinglistapi.dto.product.ImageReadDto;
import com.example.shoppinglistapi.model.Product;
import com.example.shoppinglistapi.model.Section;
import com.example.shoppinglistapi.model.unit.Unit;
import com.example.shoppinglistapi.service.ProductService;
import com.example.shoppinglistapi.service.tools.ImagesTools;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.util.List;
import java.util.Random;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureRestDocs(outputDir = "target/generated-snippets/products")
@SpringBootTest
@AutoConfigureMockMvc
public class TestProductController {

    @MockBean
    private ProductService productService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllProductsSuccessful() throws Exception {
        Section dairy = Section.builder()
                .id(1L)
                .name("Dairy")
                .build();
        Section fruits = Section.builder()
                .id(2L)
                .name("Fruits")
                .build();
        Product milk = Product.builder()
                .id(1L)
                .name("Milk")
                .defaultUnit(Unit.LITER)
                .section(dairy)
                .build();
        Product banana = Product.builder()
                .id(2L)
                .name("Banana")
                .defaultUnit(Unit.PIECE)
                .section(fruits)
                .build();
        List<Product> products = List.of(milk, banana);
        when(productService.getAllProducts()).thenReturn(products);
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andDo(document("get-all-products",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("_embedded.products[].id")
                                        .description("A unique identifier for this product."),
                                fieldWithPath("_embedded.products[].name")
                                        .description("Name of a product."),
                                subsectionWithPath("_embedded.products[].defaultUnit")
                                        .description("Default unit that is used for a product."),
                                subsectionWithPath("_embedded.products[].section")
                                        .description("Section that the product is assigned to."),
                                subsectionWithPath("_embedded.products[]._links")
                                        .description("Links to resources.")
                        )));
    }

    @Test
    public void testGetProductByIdSuccessful() throws Exception {
        Section fruits = Section.builder()
                .id(1L)
                .name("Fruits")
                .build();
        Product banana = Product.builder()
                .id(1L)
                .name("Banana")
                .defaultUnit(Unit.PIECE)
                .section(fruits)
                .build();
        when(productService.findProduct(1L)).thenReturn(banana);
        mockMvc.perform(get("/products/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andDo(document("get-product-by-id",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id").description("The id of a product to be fetched.")
                        ),
                        responseFields(
                                fieldWithPath("id")
                                        .description("A unique identifier for this product."),
                                fieldWithPath("name")
                                        .description("Name of a product."),
                                subsectionWithPath("defaultUnit")
                                        .description("Default unit that is used for a product."),
                                subsectionWithPath("section")
                                        .description("Section that the product is assigned to."),
                                subsectionWithPath("_links")
                                        .description("Links to resources")
                        ),
                        links(
                                linkWithRel("self")
                                        .description("Self reference to this product."),
                                linkWithRel("image")
                                        .description("Link to a full resolution image of a product."),
                                linkWithRel("thumbImage")
                                        .description("Link to a thumbnail (low-resolution) image of a product.")
                        )));
    }

    @Test
    public void testGetProductByIdReturnNotFoundOnNonExistingId() throws Exception {
        when(productService.findProduct(1L)).thenThrow(new EntityNotFoundException());
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddProductSuccessful() throws Exception {
        String postBody = "{" +
                "\"name\": \"Milk\"," +
                "\"defaultUnitAbbreviation\": \"l\"," +
                "\"sectionId\": 1," +
                "\"imageUrl\": \"https:\\\\link-to-image.png\"" +
                "}";
        when(productService.registerNewProduct(any()))
                .thenAnswer(p -> Product.builder()
                        .id(1L)
                        .name("Product one")
                        .defaultUnit(Unit.LITER)
                        .section(Section.builder()
                                .id(1L)
                                .name("Dairy")
                                .build())
                        .image(mock(File.class))
                        .thumbImage(mock(File.class))
                        .build());
        mockMvc.perform(post("/products")
                .contentType(MediaTypes.HAL_JSON)
                .content(postBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andDo(document("add-product",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("name")
                                        .description("Name of a product"),
                                subsectionWithPath("defaultUnitAbbreviation")
                                        .description("Default unit that is used for a product."),
                                subsectionWithPath("sectionId")
                                        .description("Section that the product is assigned to."),
                                fieldWithPath("imageUrl")
                                        .description("Link to an image that represents a product best.")
                        ),
                        responseFields(
                                fieldWithPath("id")
                                        .description("A unique identifier of a product"),
                                fieldWithPath("name")
                                        .description("Name of a product"),
                                subsectionWithPath("defaultUnit")
                                        .description("Default unit that is used for a product."),
                                subsectionWithPath("section")
                                        .description("Section that the product is assigned to."),
                                subsectionWithPath("_links")
                                        .description("Links to resources")
                        ),
                        links(
                                linkWithRel("self")
                                        .description("Self reference to this product."),
                                linkWithRel("image")
                                        .description("Link to a full resolution image of a product."),
                                linkWithRel("thumbImage")
                                        .description("Link to a thumbnail (low-resolution) image of a product.")
                        )));
    }

    @Test
    public void testRemoveProductSuccessful() throws Exception {
        doNothing().when(productService).deleteProductById(1L);
        mockMvc.perform(delete("/products/{id}", 1))
                .andExpect(status().isNoContent())
                .andDo(document("remove-product",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id")
                                        .description("The id of a product to be removed.")
                        )));
    }

    @Test
    public void testRemoveProductReturnNotFoundOnNonExistingId() throws Exception {
        doThrow(EntityNotFoundException.class).when(productService).deleteProductById(1L);
        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetProductImageSuccessful() throws Exception {
        Section fruits = Section.builder()
                .id(1L)
                .name("Fruits")
                .build();
        Product banana = Product.builder()
                .id(1L)
                .name("Banana")
                .defaultUnit(Unit.PIECE)
                .section(fruits)
                .image(new File("path-to-image"))
                .build();
        when(productService.findProduct(1L)).thenReturn(banana);
        try (MockedStatic<ImagesTools> mockedImagesTools = Mockito.mockStatic(ImagesTools.class)) {
            mockedImagesTools.when(() -> ImagesTools.getImageFromLocalResources(anyString()))
                    .thenAnswer(s -> {
                        byte[] imageContent = new byte[500];
                        new Random().nextBytes(imageContent);
                        return ImageReadDto.builder()
                                .width(800)
                                .height(800)
                                .image(imageContent)
                                .build();
                    });
            mockMvc.perform(get("/products/{id}/image", 1))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(document("get-product-image",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("id")
                                            .description("The id of a product that image is to be fetched.")
                            ),
                            responseFields(
                                    fieldWithPath("width")
                                            .description("Width of an image expressed in pixels."),
                                    fieldWithPath("height")
                                            .description("Width of an image expressed in pixels."),
                                    fieldWithPath("image")
                                            .description("Content of an image as byte array.")
                            )));
        }
    }

    @Test
    public void testGetProductImageReturnNotFoundOnNonExistingId() throws Exception {
        when(productService.findProduct(1L)).thenThrow(new EntityNotFoundException());
        mockMvc.perform(get("/products/1/image"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetProductThumbImageSuccessful() throws Exception {
        Section fruits = Section.builder()
                .id(1L)
                .name("Fruits")
                .build();
        Product banana = Product.builder()
                .id(1L)
                .name("Banana")
                .defaultUnit(Unit.PIECE)
                .section(fruits)
                .thumbImage(new File("path-to-thumb-image"))
                .build();
        when(productService.findProduct(1L)).thenReturn(banana);
        try (MockedStatic<ImagesTools> mockedImagesTools = Mockito.mockStatic(ImagesTools.class)) {
            mockedImagesTools.when(() -> ImagesTools.getImageFromLocalResources(anyString()))
                    .thenAnswer(s -> {
                        byte[] imageContent = new byte[100];
                        new Random().nextBytes(imageContent);
                        return ImageReadDto.builder()
                                .width(50)
                                .height(50)
                                .image(imageContent)
                                .build();
                    });
            mockMvc.perform(get("/products/{id}/thumbImage", 1))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andDo(document("get-product-thumb-image",
                            preprocessRequest(prettyPrint()),
                            preprocessResponse(prettyPrint()),
                            pathParameters(
                                    parameterWithName("id")
                                            .description("The id of a product that thumbnail image is to be fetched.")
                            ),
                            responseFields(
                                    fieldWithPath("width")
                                            .description("Width of a thumbnail image expressed in pixels."),
                                    fieldWithPath("height")
                                            .description("Width of a thumbnail image expressed in pixels."),
                                    fieldWithPath("image")
                                            .description("Content of a thumbnail image as byte array.")
                            )));
        }
    }

    @Test
    public void testGetProductThumbImageReturnNotFoundOnNonExistingId() throws Exception {
        when(productService.findProduct(1L)).thenThrow(new EntityNotFoundException());
        mockMvc.perform(get("/products/1/thumbImage"))
                .andExpect(status().isNotFound());
    }
}
