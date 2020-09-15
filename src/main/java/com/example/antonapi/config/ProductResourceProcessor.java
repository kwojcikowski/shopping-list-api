package com.example.antonapi.config;

import com.example.antonapi.controller.ProductController;
import com.example.antonapi.model.Product;
import lombok.SneakyThrows;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class ProductResourceProcessor implements RepresentationModelProcessor<EntityModel<Product>> {

    @SneakyThrows
    @Override
    public EntityModel<Product> process(EntityModel<Product> model) {
        model.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ProductController.class)
                        .getImage(model.getContent().getId())).withRel("image"));
        model.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ProductController.class)
                        .getThumbImage(model.getContent().getId())).withRel("thumbImage"));
        return model;
    }
}
