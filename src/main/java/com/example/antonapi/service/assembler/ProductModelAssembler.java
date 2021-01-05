package com.example.antonapi.service.assembler;

import com.example.antonapi.controller.ProductController;
import com.example.antonapi.model.Product;
import com.example.antonapi.dto.ProductDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
@RequiredArgsConstructor
public class ProductModelAssembler implements RepresentationModelAssembler<Product, ProductDTO> {

    private final @NonNull ModelMapper modelMapper;

    @SneakyThrows
    @Override
    public ProductDTO toModel(Product entity) {
        ProductDTO productDTO = modelMapper.map(entity, ProductDTO.class);

        Link selfLink = linkTo(methodOn(ProductController.class).getProductById(entity.getId())).withSelfRel();
        Link imageLink = linkTo(methodOn(ProductController.class).getProductImage(entity.getId())).withRel("image");
        Link thumbImageLink =
                linkTo(methodOn(ProductController.class).getProductThumbImage(entity.getId())).withRel("thumbImage");
        productDTO.add(List.of(selfLink, imageLink, thumbImageLink));
        return productDTO;
    }

    @SneakyThrows
    @Override
    public CollectionModel<ProductDTO> toCollectionModel(Iterable<? extends Product> entities) {
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product entity : entities){
            productDTOS.add(toModel(entity));
        }
        return CollectionModel.of(productDTOS);
    }
}
