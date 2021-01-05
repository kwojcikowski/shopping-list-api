package com.example.shoppinglistapi.controller;

import com.example.shoppinglistapi.model.Product;
import com.example.shoppinglistapi.service.ProductService;
import com.example.shoppinglistapi.service.assembler.ProductModelAssembler;
import com.example.shoppinglistapi.dto.ImageDTO;
import com.example.shoppinglistapi.dto.ProductDTO;
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
    public ResponseEntity<CollectionModel<ProductDTO>> getAllProducts() {
        return ResponseEntity.ok(productModelAssembler.toCollectionModel(productService.getAllProducts()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable("id") Long id){
        try {
            return ResponseEntity.ok(productModelAssembler.toModel(productService.findProduct(id)));
        } catch (ProductException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO requestProduct) throws IOException, ProductException {
        String imageUrl = requestProduct.getImageUrl();
        Product product = modelMapper.map(requestProduct, Product.class);
        Product registeredProduct = productService.registerNewProduct(product, imageUrl);
        ProductDTO returnedProduct = productModelAssembler.toModel(registeredProduct);
        return ResponseEntity.created(URI.create(returnedProduct.getLink("self").get().getHref()))
                .body(returnedProduct);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ProductDTO> removeProduct(@PathVariable("id") Long id) {
        try {
            productService.deleteProductById(id);
            return ResponseEntity.noContent().build();
        }catch (ProductException e){
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/{productId}/image")
    public ResponseEntity<ImageDTO> getProductImage(@PathVariable("productId") Long productId) throws IOException {
        try {
            Product product = productService.findProduct(productId);
            ImageDTO imageDTO = ImagesTools.getImageFromLocalResources(product.getImage().getAbsolutePath());
            return ResponseEntity.ok(imageDTO);
        } catch (ProductException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/{productId}/thumbImage")
    public ResponseEntity<ImageDTO> getProductThumbImage(@PathVariable("productId")  Long productId) throws IOException {
        try {
            Product product = productService.findProduct(productId);
            ImageDTO imageDTO = ImagesTools.getImageFromLocalResources(product.getThumbImage().getAbsolutePath());
            return ResponseEntity.ok(imageDTO);
        } catch (ProductException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
