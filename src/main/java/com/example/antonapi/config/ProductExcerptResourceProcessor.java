package com.example.antonapi.config;

import com.example.antonapi.controller.ProductController;
import com.example.antonapi.model.projections.ProductExcerpt;
import lombok.SneakyThrows;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelProcessor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class ProductExcerptResourceProcessor implements RepresentationModelProcessor<EntityModel<ProductExcerpt>> {

    @SneakyThrows
    @Override
    public EntityModel<ProductExcerpt> process(EntityModel<ProductExcerpt> model) {
        model.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ProductController.class)
                        .getImage(model.getContent().getId())).withRel("image"));
        model.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ProductController.class)
                        .getThumbImage(model.getContent().getId())).withRel("thumbImage"));
        return model;
    }
}
