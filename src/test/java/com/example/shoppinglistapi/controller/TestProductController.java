package com.example.shoppinglistapi.controller;

import com.example.shoppinglistapi.TestModelMapperConfiguration;
import com.example.shoppinglistapi.model.*;
import com.example.shoppinglistapi.service.ProductService;
import com.example.shoppinglistapi.service.assembler.ProductModelAssembler;
import com.example.shoppinglistapi.dto.ImageDTO;
import com.example.shoppinglistapi.service.exception.ProductException;
import com.example.shoppinglistapi.service.tools.ImagesTools;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ProductModelAssembler.class})
@WebMvcTest(controllers = ProductController.class)
@Import({ProductController.class,
        TestModelMapperConfiguration.class})
@AutoConfigureRestDocs(outputDir = "target/generated-snippets/products")
public class TestProductController {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;

    final Prefix none = Prefix.builder()
            .id(1L)
            .name("NONE")
            .abbreviation("")
            .scale(1.0)
            .build();
    final BaseUnit baseUnitLiter = BaseUnit.builder()
            .id(1L)
            .name("LITER")
            .abbreviation("l")
            .build();
    final Unit liter = Unit.builder()
            .id(1L)
            .baseUnit(baseUnitLiter)
            .prefix(none)
            .build();
    final Section section = Section.builder()
            .id(1L)
            .name("Section One")
            .build();

    @Test
    public void testGetAllProductsSuccessful() throws Exception {
        Product product1 = Product.builder()
                .id(1L)
                .name("Product One")
                .defaultUnit(liter)
                .section(section)
                .build();
        Product product2 = Product.builder()
                .id(2L)
                .name("Product Two")
                .defaultUnit(liter)
                .section(section)
                .build();
        List<Product> products = List.of(product1, product2);
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
        Product product1 = Product.builder()
                .id(1L)
                .name("Product 1")
                .defaultUnit(liter)
                .section(section)
                .build();
        when(productService.findProduct(1L)).thenReturn(product1);
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
        when(productService.findProduct(1L)).thenThrow(new ProductException());
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddProductSuccessful() throws Exception {
        String postBody = "{" +
                "\"name\": \"Product one\"," +
                "\"defaultUnit\": {" +
                "   \"id\": 1," +
                "   \"abbreviation\": \"l\"}," +
                "\"section\": {" +
                "   \"id\": 1," +
                "   \"name\": \"Section one\"}," +
                "\"image\": \"https:\\\\link-to-image.png\"" +
                "}";
        when(productService.registerNewProduct(any(), any()))
                .thenAnswer(p -> Product.builder()
                        .id(1L)
                        .name("Product one")
                        .defaultUnit(liter)
                        .section(Section.builder()
                                .id(1L)
                                .name("Section one").build())
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
                                subsectionWithPath("defaultUnit")
                                        .description("Default unit that is used for a product."),
                                subsectionWithPath("section")
                                        .description("Section that the product is assigned to."),
                                fieldWithPath("image")
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
        doThrow(ProductException.class).when(productService).deleteProductById(1L);
        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetProductImageSuccessful() throws Exception {
        Product product1 = Product.builder()
                .id(1L)
                .name("Product 1")
                .defaultUnit(liter)
                .section(section)
                .image(new File("product1-image"))
                .thumbImage(new File("product1-thumbImage"))
                .build();
        when(productService.findProduct(1L)).thenReturn(product1);
        try(MockedStatic<ImagesTools> mockedImagesTools = Mockito.mockStatic(ImagesTools.class)){
            mockedImagesTools.when(() -> ImagesTools.getImageFromLocalResources(anyString()))
                    .thenAnswer(s -> {
                        byte[] imageContent = new byte[500];
                        new Random().nextBytes(imageContent);
                        return ImageDTO.builder()
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
        when(productService.findProduct(1L)).thenThrow(new ProductException());
        mockMvc.perform(get("/products/1/image"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetProductThumbImageSuccessful() throws Exception {
        Product product1 = Product.builder()
                .id(1L)
                .name("Product 1")
                .defaultUnit(liter)
                .section(section)
                .image(new File("product1-image"))
                .thumbImage(new File("product1-thumbImage"))
                .build();
        when(productService.findProduct(1L)).thenReturn(product1);
        try(MockedStatic<ImagesTools> mockedImagesTools = Mockito.mockStatic(ImagesTools.class)){
            mockedImagesTools.when(() -> ImagesTools.getImageFromLocalResources(anyString()))
                    .thenAnswer(s -> {
                        byte[] imageContent = new byte[100];
                        new Random().nextBytes(imageContent);
                        return ImageDTO.builder()
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
        when(productService.findProduct(1L)).thenThrow(new ProductException());
        mockMvc.perform(get("/products/1/thumbImage"))
                .andExpect(status().isNotFound());
    }
}
