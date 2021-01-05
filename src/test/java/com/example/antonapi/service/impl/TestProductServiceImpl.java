package com.example.antonapi.service.impl;


import com.example.antonapi.model.Product;
import com.example.antonapi.model.Section;
import com.example.antonapi.model.Unit;
import com.example.antonapi.repository.ProductRepository;
import com.example.antonapi.service.ProductService;
import com.example.antonapi.service.exception.ProductException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

public class TestProductServiceImpl {

    @Test
    public void testFindProduct(){
        //Given
        ProductRepository productRepository = mock(ProductRepository.class);
        Product product = new Product(1L,"Banana", mock(Unit.class),
                mock(Section.class), mock(File.class), mock(File.class));
        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findProductById(1L)).thenReturn(product);
        ProductService service = new ProductServiceImpl(productRepository);

        //When
        Product returnedProduct = null;
        try {
            returnedProduct = service.findProduct(1L);
            //Then
            Product finalReturnedProduct = returnedProduct;
            assertAll(() -> assertThat(finalReturnedProduct.getId()).isEqualTo(1L),
                    () -> assertThat(finalReturnedProduct.getName()).isEqualTo("Banana"));
        } catch (ProductException e) {
            fail("Exception should not had been thrown.");
        }
    }

    @Test
    public void testFindProductThrowExceptionOnNonExistingId() {
        //Given
        ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.findProductById(1L)).thenReturn(null);
        ProductService service = new ProductServiceImpl(productRepository);

        //When
        try {
            service.findProduct(1L);
            //Then
            fail("Exception should not had been thrown.");
        } catch (ProductException e) {
            assertThat(e.getMessage()).isEqualTo("Unable to fetch product: Product with id 1 does not exist.");
        }
    }

    @Test
    public void testDeleteProductByIdThrowExceptionOnNonExistingProductId(){
        //Given
        ProductRepository productRepository = mock(ProductRepository.class);
        when(productRepository.existsById(1L)).thenReturn(false);
        ProductService service = new ProductServiceImpl(productRepository);

        //When
        try {
            service.deleteProductById(1L);

        //Then
            fail("Exception should had been thrown.");
        } catch (ProductException e) {
            assertThat(e.getMessage()).isEqualTo("Unable to delete product: Product with ID " + 1L + " does not exist.");
        }
    }

    @Test
    public void testRegisterNewProductThrowExceptionOnIdNonNull(){
        //Given
        ProductRepository productRepository = mock(ProductRepository.class);
        ProductService service = new ProductServiceImpl(productRepository);
        Product product = new Product();
        product.setId(1L);

        //When
        try{
            service.registerNewProduct(product, "");

        //Then
            fail("Exception should had been thrown.");
        }catch (ProductException | IOException e){
            assertThat(e.getMessage()).isEqualTo("Unable to register product: Product must not have an ID.");
        }
    }

    @Test
    public void testRegisterNewProductThrowExceptionOnProductNameDuplicate(){
        //Given
        ProductRepository productRepository = mock(ProductRepository.class);
        Product product = new Product();
        product.setName("Banana");
        when(productRepository.findProductByName("Banana")).thenReturn(mock(Product.class));
        ProductService service = new ProductServiceImpl(productRepository);

        //When
        try{
            service.registerNewProduct(product, "");

            //Then
            fail("Exception should had been thrown.");
        }catch (ProductException | IOException e){
            assertThat(e.getMessage()).isEqualTo("Unable to register product: Product with name " + "Banana"
                    + " already exists.");
        }
    }
}
