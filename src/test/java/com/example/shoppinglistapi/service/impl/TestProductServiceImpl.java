package com.example.shoppinglistapi.service.impl;


import com.example.shoppinglistapi.dto.product.ProductCreateDto;
import com.example.shoppinglistapi.model.Product;
import com.example.shoppinglistapi.model.Section;
import com.example.shoppinglistapi.model.unit.Unit;
import com.example.shoppinglistapi.repository.ProductRepository;
import com.example.shoppinglistapi.repository.SectionRepository;
import com.example.shoppinglistapi.service.ProductService;
import com.example.shoppinglistapi.service.tools.ImagesTools;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TestProductServiceImpl {

    private final ProductRepository productRepository = mock(ProductRepository.class);
    private final SectionRepository sectionRepository = mock(SectionRepository.class);

    private final ProductService productService =
            new ProductServiceImpl(productRepository, sectionRepository);


    @Test
    public void testFindProduct() {
        Product product = Product.builder()
                .id(1L)
                .name("Banana")
                .defaultUnit(Unit.PIECE)
                .section(mock(Section.class))
                .build();
        when(productRepository.findById(1L)).thenReturn(Optional.ofNullable(product));

        Product returnedProduct = assertDoesNotThrow(
                () -> productService.findProduct(1L),
                "No exception should be thrown on fetching an existing product."
        );

        assertAll(() -> assertThat(returnedProduct.getId()).isEqualTo(1L),
                () -> assertThat(returnedProduct.getName()).isEqualTo("Banana"),
                () -> assertThat(returnedProduct.getDefaultUnit()).isNotNull(),
                () -> assertThat(returnedProduct.getSection()).isNotNull());
    }

    @Test
    public void testFindProductThrowExceptionOnNonExistingId() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> productService.findProduct(1L),
                "Expected to throw EntityNotFoundException on attempting to delete non existing product."
        );
        assertThat(exception.getMessage()).isEqualTo("Unable to fetch product: Product with given id does not exist.");
    }

    @Test
    public void testDeleteProductById() {
        when(productRepository.existsById(1L)).thenReturn(true);
        doNothing().when(productRepository).deleteById(1L);
        assertDoesNotThrow(
                () -> productService.deleteProductById(1L),
                "No exception should be thrown on deleting an existing product."
        );
    }

    @Test
    public void testDeleteProductByIdThrowExceptionOnNonExistingProductId() {
        when(productRepository.existsById(1L)).thenReturn(false);
        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> productService.deleteProductById(1L),
                "Expected to throw EntityNotFoundException on attempting to delete non existing product."
        );
        assertThat(exception.getMessage()).isEqualTo("Unable to delete product: Product with given id does not exist.");
    }

    @Test
    public void testRegisterNewProduct() {
        Section section = Section.builder()
                .id(1L)
                .name("Fruits")
                .build();
        ProductCreateDto createDto = ProductCreateDto.builder()
                .name("Banana")
                .defaultUnitAbbreviation("pcs")
                .sectionId(1L)
                .imageUrl("https://link-to-image.jpg")
                .build();

        Product expectedProduct = Product.builder()
                .id(1L)
                .name(createDto.name)
                .defaultUnit(Unit.PIECE)
                .section(section)
                .image(mock(File.class))
                .thumbImage(mock(File.class))
                .build();

        when(productRepository.existsByName("Banana")).thenReturn(false);
        when(sectionRepository.findById(1L)).thenReturn(Optional.ofNullable(section));
        when(productRepository.saveAndFlush(any(Product.class))).thenReturn(expectedProduct);

        MockedStatic<ImagesTools> imagesToolsMockedStatic = mockStatic(ImagesTools.class);
        imagesToolsMockedStatic.when(() -> ImagesTools.saveImageFromURL(anyString(), anyString())).thenReturn(mock(File.class));
        imagesToolsMockedStatic.when(() -> ImagesTools.generateImageThumbnail(any(File.class))).thenReturn(mock(File.class));

        Product actualProduct = assertDoesNotThrow(
                () -> productService.registerNewProduct(createDto),
                "No exception should be thrown on registering a valid product."
        );

        assertAll(
                () -> assertThat(actualProduct.getId()).isEqualTo(expectedProduct.getId()),
                () -> assertThat(actualProduct.getName()).isEqualTo(expectedProduct.getName()),
                () -> assertThat(actualProduct.getDefaultUnit()).isEqualTo(expectedProduct.getDefaultUnit()),
                () -> assertThat(actualProduct.getSection()).isEqualTo(expectedProduct.getSection()),
                () -> assertThat(actualProduct.getImage()).isNotNull(),
                () -> assertThat(actualProduct.getThumbImage()).isNotNull()
        );
    }

    @Test
    public void testRegisterNewProductThrowExceptionOnProductNameDuplicate() {
        ProductCreateDto createDto = ProductCreateDto.builder()
                .name("Banana")
                .defaultUnitAbbreviation("pcs")
                .sectionId(1L)
                .imageUrl("")
                .build();

        when(productRepository.existsByName("Banana")).thenReturn(true);

        Exception exception = assertThrows(
                DataIntegrityViolationException.class,
                () -> productService.registerNewProduct(createDto),
                "Expected to throw DataIntegrityViolationException when " +
                        "registering product with already existing name"
        );
        assertThat(exception.getMessage()).isEqualTo("Unable to register product: " +
                "Product with name Banana already exists.");
    }

    @Test
    public void testRegisterNewProductThrowExceptionOnNonExistingUnit() {
        ProductCreateDto createDto = ProductCreateDto.builder()
                .name("Banana")
                .defaultUnitAbbreviation("non-existing-unit")
                .sectionId(1L)
                .imageUrl("")
                .build();

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> productService.registerNewProduct(createDto),
                "Expected to throw EntityNotFoundException when " +
                        "registering product with non existing unit"
        );
        assertThat(exception.getMessage()).isEqualTo("Unable to register product: Unable to find unit with provided abbreviation.");
    }

    @Test
    public void testRegisterNewProductThrowExceptionOnNonExistingSection() {
        ProductCreateDto createDto = ProductCreateDto.builder()
                .name("Banana")
                .defaultUnitAbbreviation("pcs")
                .sectionId(1L)
                .imageUrl("")
                .build();

        when(sectionRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                EntityNotFoundException.class,
                () -> productService.registerNewProduct(createDto),
                "Expected to throw EntityNotFoundException when " +
                        "registering product with non existing section"
        );
        assertThat(exception.getMessage()).isEqualTo("Unable to register product: Provided section does not exist.");
    }
}
