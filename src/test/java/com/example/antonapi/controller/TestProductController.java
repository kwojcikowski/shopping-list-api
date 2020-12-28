package com.example.antonapi.controller;

import com.example.antonapi.TestModelMapperConfiguration;
import com.example.antonapi.model.*;
import com.example.antonapi.service.ProductService;
import com.example.antonapi.service.assembler.ProductModelAssembler;
import com.example.antonapi.service.dto.ImageDTO;
import com.example.antonapi.service.dto.ProductDTO;
import com.example.antonapi.service.exception.ProductException;
import com.example.antonapi.service.tools.ImagesTools;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ProductModelAssembler.class})
@WebMvcTest(controllers = ProductController.class)
@Import({ProductController.class,
        TestModelMapperConfiguration.class})
public class TestProductController {

    @MockBean
    private ProductService productService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ModelMapper modelMapper;

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
            .name("Section 1")
            .build();
    final ImageDTO mockImage = new ImageDTO(100, 100, new byte[100]);

    @Test
    public void testGetAllProductsSuccessful() throws Exception {
        Product product1 = Product.builder()
                .id(1L)
                .name("Product 1")
                .defaultUnit(liter)
                .section(section)
                .build();
        Product product2 = Product.builder()
                .id(2L)
                .name("Product 2")
                .defaultUnit(liter)
                .section(section)
                .build();
        List<Product> products = List.of(product1, product2);
        when(productService.getAllProducts()).thenReturn(products);
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.products", hasSize(products.size())));
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
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON));
    }

    @Test
    public void testGetProductByIdReturnNotFoundOnNonExistingId() throws Exception {
        when(productService.findProduct(1L)).thenThrow(new ProductException());
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddProductSuccessful() throws Exception {
        Product product1 = Product.builder()
                .name("Product 1")
                .defaultUnit(liter)
                .section(section)
                .build();
        ProductDTO productDTO = modelMapper.map(product1, ProductDTO.class);
        productDTO.setImage("https://link-to-image.png");
        String postBody = new ObjectMapper().writeValueAsString(productDTO);
        when(productService.registerNewProduct(Mockito.any(Product.class), Mockito.anyString()))
                .thenAnswer(p -> {
                    Product addedProduct = p.getArgument(0);
                    addedProduct.setId(1L);
                    addedProduct.setImage(mock(File.class));
                    addedProduct.setThumbImage(mock(File.class));
                    return addedProduct;
                });
        mockMvc.perform(post("/products")
                    .contentType(MediaTypes.HAL_JSON)
                    .content(postBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON));
    }

    @Test
    public void testRemoveProductSuccessful() throws Exception {
        doNothing().when(productService).deleteProductById(1L);
        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isNoContent());
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
                    .thenReturn(mockImage);
            mockMvc.perform(get("/products/1/image"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
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
                    .thenReturn(mockImage);
            mockMvc.perform(get("/products/1/thumbImage"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }
    }

    @Test
    public void testGetProductThumbImageReturnNotFoundOnNonExistingId() throws Exception {
        when(productService.findProduct(1L)).thenThrow(new ProductException());
        mockMvc.perform(get("/products/1/thumbImage"))
                .andExpect(status().isNotFound());
    }
}
