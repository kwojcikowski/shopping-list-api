package com.example.antonapi.controller;

import com.example.antonapi.model.Product;
import com.example.antonapi.service.ProductService;
import com.example.antonapi.service.assembler.ProductModelAssembler;
import com.example.antonapi.service.dto.ImageDTO;
import com.example.antonapi.service.dto.ProductDTO;
import com.example.antonapi.service.exception.ProductException;
import com.example.antonapi.service.tools.ImagesTools;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.*;

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
        return ResponseEntity.ok(productModelAssembler.toModel(productService.findProduct(id)));
    }

    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(@RequestBody ProductDTO requestProduct) throws IOException, ProductException {
        Product registeredProduct = productService.registerNewProduct(requestProduct);
        Product addedProduct = productService.addProduct(registeredProduct);
        return ResponseEntity.ok(productModelAssembler.toModel(addedProduct));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity.HeadersBuilder<?> removeProduct(@PathVariable("id") Long id) throws ProductException {
        productService.deleteProductById(id);
        return ResponseEntity.noContent();
    }

    @GetMapping(path = "/{productId}/image")
    public ResponseEntity<ImageDTO> getProductImage(@PathVariable("productId") Long productId) throws IOException {
        Product product = productService.findProduct(productId);
        ImageDTO imageDTO = ImagesTools.getImageFromLocalResources(product.getImage().getAbsolutePath());
        return ResponseEntity.ok(imageDTO);
    }

    @GetMapping(path = "/{productId}/thumbImage")
    public ResponseEntity<ImageDTO> getProductThumbImage(@PathVariable("productId")  Long productId) throws IOException {
        Product product = productService.findProduct(productId);
        ImageDTO imageDTO = ImagesTools.getImageFromLocalResources(product.getThumbImage().getAbsolutePath());
        return ResponseEntity.ok(imageDTO);
    }
}
