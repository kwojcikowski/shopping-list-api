package com.example.shoppinglistapi.controller;

import com.example.shoppinglistapi.dto.product.ImageReadDto;
import com.example.shoppinglistapi.dto.product.ProductCreateDto;
import com.example.shoppinglistapi.dto.product.ProductReadDto;
import com.example.shoppinglistapi.model.Product;
import com.example.shoppinglistapi.service.ProductService;
import com.example.shoppinglistapi.service.assembler.ProductModelAssembler;
import com.example.shoppinglistapi.service.tools.ImagesTools;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URI;

@RepositoryRestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(path = "/products")
public class ProductController {

    private final @NonNull ProductService productService;
    private final @NonNull ProductModelAssembler productModelAssembler;

    @GetMapping
    public ResponseEntity<CollectionModel<ProductReadDto>> getAllProducts() {
        return ResponseEntity.ok(productModelAssembler.toCollectionModel(productService.getAllProducts()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProductReadDto> getProductById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(productModelAssembler.toModel(productService.findProduct(id)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> addProduct(@Valid @RequestBody ProductCreateDto createDto) {
        try {
            Product registeredProduct = productService.registerNewProduct(createDto);
            ProductReadDto returnedProduct = productModelAssembler.toModel(registeredProduct);
            return ResponseEntity.created(URI.create(returnedProduct.getLink("self").orElseThrow().getHref()))
                    .body(returnedProduct);
        } catch (DataIntegrityViolationException | EntityNotFoundException | IOException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> removeProduct(@PathVariable("id") Long id) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/{productId}/image")
    public ResponseEntity<ImageReadDto> getProductImage(@PathVariable("productId") Long productId) throws IOException {
        try {
            Product product = productService.findProduct(productId);
            ImageReadDto imageReadDto = ImagesTools.getImageFromLocalResources(product.getImage().getAbsolutePath());
            return ResponseEntity.ok(imageReadDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/{productId}/thumbImage")
    public ResponseEntity<ImageReadDto> getProductThumbImage(@PathVariable("productId") Long productId) throws IOException {
        try {
            Product product = productService.findProduct(productId);
            ImageReadDto imageReadDto = ImagesTools.getImageFromLocalResources(product.getThumbImage().getAbsolutePath());
            return ResponseEntity.ok(imageReadDto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
