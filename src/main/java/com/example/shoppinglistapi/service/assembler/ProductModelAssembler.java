package com.example.shoppinglistapi.service.assembler;

import com.example.shoppinglistapi.controller.ProductController;
import com.example.shoppinglistapi.model.Product;
import com.example.shoppinglistapi.dto.product.ProductReadDto;
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
public class ProductModelAssembler implements RepresentationModelAssembler<Product, ProductReadDto> {

    private final @NonNull UnitModelAssembler unitModelAssembler;
    private final @NonNull SectionModelAssembler sectionModelAssembler;

    @SneakyThrows
    @Override
    public ProductReadDto toModel(Product entity) {
        ProductReadDto productDto = ProductReadDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .defaultUnit(unitModelAssembler.toModel(entity.getDefaultUnit()))
                .section(sectionModelAssembler.toModel(entity.getSection()))
                .build();

        Link selfLink = linkTo(methodOn(ProductController.class).getProductById(entity.getId())).withSelfRel();
        Link imageLink = linkTo(methodOn(ProductController.class).getProductImage(entity.getId())).withRel("image");
        Link thumbImageLink =
                linkTo(methodOn(ProductController.class).getProductThumbImage(entity.getId())).withRel("thumbImage");
        productDto.add(List.of(selfLink, imageLink, thumbImageLink));
        return productDto;
    }

    @SneakyThrows
    @Override
    public CollectionModel<ProductReadDto> toCollectionModel(Iterable<? extends Product> entities) {
        List<ProductReadDto> productDtos = new ArrayList<>();
        for (Product entity : entities){
            productDtos.add(toModel(entity));
        }
        return CollectionModel.of(productDtos);
    }
}
