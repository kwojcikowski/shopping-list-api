package com.example.antonapi.service.impl;


import com.example.antonapi.model.Product;
import com.example.antonapi.model.Section;
import com.example.antonapi.model.Unit;
import com.example.antonapi.repository.ProductRepository;
import com.example.antonapi.service.ProductService;
import com.example.antonapi.service.dto.ProductDTO;
import com.example.antonapi.service.exception.ProductException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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
        when(productRepository.findProductById(1L)).thenReturn(product);
        ProductService service = new ProductServiceImpl(productRepository);

        //When
        Product returnedProduct = service.findProduct(1L);

        //Then
        assertAll(() -> assertThat(returnedProduct.getId()).isEqualTo(1L),
                () -> assertThat(returnedProduct.getName()).isEqualTo("Banana"));
    }

    @Test
    public void testAddProduct(){
        //Given
        ProductRepository productRepository = mock(ProductRepository.class);
        Product product = new Product(1L,"Banana", mock(Unit.class),
                mock(Section.class), mock(File.class), mock(File.class));
        when(productRepository.findProductById(1L)).thenReturn(product);
        when(productRepository.findProductByName("Banana")).thenReturn(null);
        when(productRepository.saveAndFlush(product)).thenAnswer((Answer<Product>) invocationOnMock -> {
            product.setId(1L);
            return product;
        });
        ProductService service = new ProductServiceImpl(productRepository);

        //When
        Product addedProduct = null;
        try {
            addedProduct = service.addProduct(product);

        //Then
        } catch (ProductException e) {
            fail("Exception should not had been thrown");
        }
        assertThat(addedProduct).isNotNull();
        Product finalAddedProduct = addedProduct;
        assertAll(() -> assertThat(finalAddedProduct.getId()).isEqualTo(1L),
                () -> assertThat(finalAddedProduct.getName()).isEqualTo("Banana"));
    }

    @Test
    public void testAddProductThrowExceptionOnExistingProductWithSameName(){
        //Given
        ProductRepository productRepository = mock(ProductRepository.class);
        Product product = new Product(1L,"Banana", mock(Unit.class),
                mock(Section.class), mock(File.class), mock(File.class));
        when(productRepository.findProductByName("Banana")).thenReturn(product);
        ProductService service = new ProductServiceImpl(productRepository);
        Product sameProduct = new Product("Banana", mock(Unit.class),
                mock(Section.class), mock(File.class), mock(File.class));

        //When
        try{
            service.addProduct(sameProduct);

        //Then
            fail("Exception should had been thrown");
        }catch (ProductException e){
            assertThat(e.getMessage()).isEqualTo("Unable to add product: " +
                            "Product with name " + sameProduct.getName() + " already exists.");
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
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);

        //When
        try{
            service.registerNewProduct(productDTO);

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
        ProductDTO productDTO = new ProductDTO();
        productDTO.setName("Banana");
        when(productRepository.findProductByName("Banana")).thenReturn(mock(Product.class));
        ProductService service = new ProductServiceImpl(productRepository);

        //When
        try{
            service.registerNewProduct(productDTO);

            //Then
            fail("Exception should had been thrown.");
        }catch (ProductException | IOException e){
            assertThat(e.getMessage()).isEqualTo("Unable to register product: Product with name " + "Banana"
                    + " already exists.");
        }
    }
}
