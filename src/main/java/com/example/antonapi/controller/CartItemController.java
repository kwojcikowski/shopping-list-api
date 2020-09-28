package com.example.antonapi.controller;

import com.example.antonapi.model.CartItem;
import com.example.antonapi.service.CartItemService;
import com.example.antonapi.service.assembler.CartItemModelAssembler;
import com.example.antonapi.service.dto.CartItemDTO;
import com.example.antonapi.service.exception.CartItemException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RepositoryRestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(path = "/cartItems")
public class CartItemController {

    private final @NonNull CartItemModelAssembler cartItemModelAssembler;
    private final @NonNull CartItemService cartItemService;
    private final @NonNull ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<CollectionModel<CartItemDTO>> getAllCartItems(){
        return ResponseEntity.ok(cartItemModelAssembler.toCollectionModel(cartItemService.getAllCartItems()));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CartItemDTO> getCartItemById(@PathVariable("id") Long id){
        return ResponseEntity.ok(cartItemModelAssembler.toModel(cartItemService.findCartItem(id)));
    }

    @PostMapping
    public ResponseEntity<CartItemDTO> addCartItem(@RequestBody CartItem cartItemFromRequest){
        CartItem mappedCartItem = modelMapper.map(cartItemFromRequest, CartItem.class);
        CartItem addedCartItem = cartItemService.addCartItem(mappedCartItem);
        return ResponseEntity.ok(cartItemModelAssembler.toModel(addedCartItem));
    }

    @PatchMapping
    public ResponseEntity<CollectionModel<CartItemDTO>> updateCartItems(@RequestBody List<CartItemDTO> cartItemsFromRequest) throws CartItemException {
        List<CartItem> mappedCartItems = cartItemsFromRequest
                .stream()
                .map(cartItemDTO -> modelMapper.map(cartItemDTO, CartItem.class))
                .collect(Collectors.toList());
        List<CartItem> updatedCartItems = cartItemService.updateCartItems(mappedCartItems);
        return ResponseEntity.ok(cartItemModelAssembler.toCollectionModel(updatedCartItems));
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity.HeadersBuilder<?> deleteCartItem(@PathVariable("id") Long id) throws CartItemException {
        cartItemService.deleteCartItemById(id);
        return ResponseEntity.noContent();
    }
}
