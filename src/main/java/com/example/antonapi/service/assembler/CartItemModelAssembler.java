package com.example.antonapi.service.assembler;

import com.example.antonapi.controller.CartItemController;
import com.example.antonapi.model.CartItem;
import com.example.antonapi.service.dto.CartItemDTO;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.dom4j.rule.Mode;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CartItemModelAssembler implements RepresentationModelAssembler<CartItem, CartItemDTO> {

    private final @NonNull ModelMapper modelMapper;

    @Override
    public CartItemDTO toModel(CartItem entity) {
        CartItemDTO cartItemDTO = modelMapper.map(entity, CartItemDTO.class);

        Link selfLink = linkTo(methodOn(CartItemController.class).getCartItemById(entity.getId())).withSelfRel();
        cartItemDTO.add(selfLink);
        return cartItemDTO;
    }

    @Override
    public CollectionModel<CartItemDTO> toCollectionModel(Iterable<? extends CartItem> entities) {
        List<CartItemDTO> cartItemDTOS = new ArrayList<>();
        for (CartItem entity : entities){
            cartItemDTOS.add(toModel(entity));
        }
        return CollectionModel.of(cartItemDTOS);
    }
}
