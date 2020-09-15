package com.example.antonapi.controller;

import com.example.antonapi.model.Product;
import com.example.antonapi.repository.ProductRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;

@RepositoryRestController
@CrossOrigin
public class ProductController {

    final String NO_IMAGE = "no_img.png";

    @Autowired
    ProductRepository productRepository;


    @PostMapping(path = "/products")
    public ResponseEntity<EntityModel<PersistentEntityResource>> addProduct(@RequestBody Product requestProduct,
                                                                            PersistentEntityResourceAssembler assembler) throws IOException {
        Product product = productRepository.saveAndFlush(requestProduct);
        PersistentEntityResource resource = assembler.toModel(product);
        resource.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ProductController.class)
                        .getImage(product.getId())).withRel("image"));
        resource.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ProductController.class)
                        .getThumbImage(product.getId())).withRel("thumbImage"));
        return ResponseEntity.ok(EntityModel.of(resource));
    }

    @GetMapping(path = "/products/{productId}/image")
    public ResponseEntity<Map<String, byte[]>> getImage(@PathVariable("productId") Long productId) throws IOException {
        Product product = productRepository.findProductById(productId);
        byte[] media;
        try {
            media = getImageFromLocalResources(product.getImage().getName());
        }catch (NullPointerException e){
            media = getImageFromLocalResources(NO_IMAGE);
        }
        return ResponseEntity.ok(Collections.singletonMap("image", media));
    }

    @GetMapping(path = "/products/{productId}/thumbImage")
    public ResponseEntity<Map<String, byte[]>> getThumbImage(@PathVariable("productId")  Long productId) throws IOException {
        Product product = productRepository.findProductById(productId);
        byte[] media;
        try {
            media = getImageFromLocalResources(product.getThumbImage().getName());
        }catch (NullPointerException e){
            media = getImageFromLocalResources(NO_IMAGE);
        }
        return ResponseEntity.ok(Collections.singletonMap("image", media));
    }

    private byte[] getImageFromLocalResources(String fileName) throws IOException {
        Path noImagePath = FileSystems.getDefault().getPath("src","main", "resources", "img", fileName);
        File f = new File(noImagePath.toString());
        String fileType = f.getName().split("\\.")[1];
        BufferedImage image = ImageIO.read(f);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, fileType, bos);
        return bos.toByteArray();
    }
}
