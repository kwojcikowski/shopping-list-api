package com.example.shoppinglistapi.controller;

import com.example.shoppinglistapi.model.Product;
import com.example.shoppinglistapi.service.ProductService;
import com.example.shoppinglistapi.service.assembler.ProductModelAssembler;
import com.example.shoppinglistapi.dto.product.ImageReadDto;
import com.example.shoppinglistapi.dto.product.ProductReadDTO;
import com.example.shoppinglistapi.service.exception.ProductException;
import com.example.shoppinglistapi.service.tools.ImagesTools;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.net.URI;

@RepositoryRestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(path = "/products")
public class ProductController {

    private final @NonNull ProductService productService;
    private final @NonNull ProductModelAssembler productModelAssembler;
    private final @NonNull ModelMapper modelMapper;


    @GetMapping
    public ResponseEntity<CollectionModel<ProductReadDTO>> getAllProducts() {
        return ResponseEntity.ok(productModelAssembler.toCollectionModel(productService.getAllProducts()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProductReadDTO> getProductById(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(productModelAssembler.toModel(productService.findProduct(id)));
        } catch (ProductException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ProductReadDTO> addProduct(@RequestBody ProductReadDTO requestProduct) throws IOException, ProductException {
        String imageUrl = requestProduct.getImageUrl();
        Product product = modelMapper.map(requestProduct, Product.class);
        Product registeredProduct = productService.registerNewProduct(product, imageUrl);
        ProductReadDTO returnedProduct = productModelAssembler.toModel(registeredProduct);
        return ResponseEntity.created(URI.create(returnedProduct.getLink("self").get().getHref()))
                .body(returnedProduct);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ProductReadDTO> removeProduct(@PathVariable("id") Long id) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.noContent().build();
        }catch (ProductException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/{productId}/image")
    public ResponseEntity<ImageReadDto> getProductImage(@PathVariable("productId") Long productId) throws IOException {
        try {
            Product product = productService.findProduct(productId);
            ImageReadDto imageReadDto = ImagesTools.getImageFromLocalResources(product.getImage().getAbsolutePath());
            return ResponseEntity.ok(imageReadDto);
        } catch (ProductException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/{productId}/thumbImage")
    public ResponseEntity<ImageReadDto> getProductThumbImage(@PathVariable("productId")  Long productId) throws IOException {
        try {
            Product product = productService.findProduct(productId);
            ImageReadDto imageReadDto = ImagesTools.getImageFromLocalResources(product.getThumbImage().getAbsolutePath());
            return ResponseEntity.ok(imageReadDto);
        } catch (ProductException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
