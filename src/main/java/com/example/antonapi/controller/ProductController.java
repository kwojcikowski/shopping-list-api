package com.example.antonapi.controller;

import com.example.antonapi.model.Product;
import com.example.antonapi.repository.ProductRepository;
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
import java.util.HashMap;
import java.util.Map;

@RepositoryRestController
@CrossOrigin
public class ProductController {

    final String NO_IMAGE = "no_image.png";
    final String NO_IMAGE_THUMBNAIL = "no_image_thumbnail.png";

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
    public ResponseEntity<Map<String, Object>> getImage(@PathVariable("productId") Long productId) throws IOException {
        Product product = productRepository.findProductById(productId);
        Map<String, Object> media;
        try {
            media = getImageFromLocalResources(product.getImage().getName());
        }catch (NullPointerException e){
            media = getImageFromLocalResources(NO_IMAGE);
        }
        return ResponseEntity.ok(media);
    }

    @GetMapping(path = "/products/{productId}/thumbImage")
    public ResponseEntity<Map<String, Object>> getThumbImage(@PathVariable("productId")  Long productId) throws IOException {
        Product product = productRepository.findProductById(productId);
        Map<String, Object> media;
        try {
            media = getImageFromLocalResources(product.getThumbImage().getName());
        }catch (NullPointerException e){
            media = getImageFromLocalResources(NO_IMAGE_THUMBNAIL);
        }
        return ResponseEntity.ok(media);
    }

    private Map<String, Object> getImageFromLocalResources(String fileName) throws IOException {
        Map<String, Object> map = new HashMap<>();
        Path noImagePath = FileSystems.getDefault().getPath("src","main", "resources", "img", fileName);
        File f = new File(noImagePath.toString());
        String fileType = f.getName().split("\\.")[1];
        BufferedImage image = ImageIO.read(f);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(image, fileType, bos);
        map.put("width", image.getWidth());
        map.put("height", image.getHeight());
        map.put("image", bos.toByteArray());
        return map;
    }
}
