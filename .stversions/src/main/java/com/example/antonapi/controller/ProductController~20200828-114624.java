package com.example.antonapi.controller;

import com.example.antonapi.model.CartItem;
import com.example.antonapi.model.Product;
import com.example.antonapi.repository.ProductRepository;
import com.example.antonapi.service.SmartUnits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.config.Projection;
import org.springframework.data.rest.webmvc.PersistentEntityResource;
import org.springframework.data.rest.webmvc.PersistentEntityResourceAssembler;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RepositoryRestController
@CrossOrigin
public class ProductController {

    ProductRepository productRepository;
    ProjectionFactory projectionFactory

    public ProductController(ProductRepository productRepository, ProjectionFactory)


    @PostMapping(path = "/products")
    public ResponseEntity<EntityModel<PersistentEntityResource>> addProduct(@RequestBody Product requestProduct,
                                                                            PersistentEntityResourceAssembler assembler){
        Product product = productRepository.saveAndFlush(requestProduct);
        System.out.println(product);
        return ResponseEntity.ok(EntityModel.of(assembler.toModel(product)));
    }

}
