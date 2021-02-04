package com.example.shoppinglistapi.service.assembler;

import com.example.shoppinglistapi.controller.CartItemController;
import com.example.shoppinglistapi.model.CartItem;
import com.example.shoppinglistapi.dto.cartitem.CartItemReadDto;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
public class CartItemModelAssembler implements RepresentationModelAssembler<CartItem, CartItemReadDto> {

    private final @NonNull ModelMapper modelMapper;

    @Override
    public CartItemReadDto toModel(CartItem entity) {
        CartItemReadDto cartItemReadDto = modelMapper.map(entity, CartItemReadDto.class);

        Link selfLink = linkTo(methodOn(CartItemController.class).getCartItemById(entity.getId())).withSelfRel();
        cartItemReadDto.add(selfLink);
        return cartItemReadDto;
    }

    @Override
    public CollectionModel<CartItemReadDto> toCollectionModel(Iterable<? extends CartItem> entities) {
        List<CartItemReadDto> cartItemReadDtos = new ArrayList<>();
        for (CartItem entity : entities){
            cartItemReadDtos.add(toModel(entity));
        }
        return CollectionModel.of(cartItemReadDtos);
    }
}
