package com.example.shoppinglistapi.controller;

import com.example.shoppinglistapi.model.CartItem;
import com.example.shoppinglistapi.service.CartItemService;
import com.example.shoppinglistapi.service.assembler.CartItemModelAssembler;
import com.example.shoppinglistapi.dto.CartItemDTO;
import com.example.shoppinglistapi.service.exception.CartItemException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
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
        try {
            return ResponseEntity.ok(cartItemModelAssembler.toModel(cartItemService.findCartItem(id)));
        } catch (CartItemException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<CartItemDTO> addCartItem(@RequestBody CartItemDTO cartItemFromRequest){
        CartItem mappedCartItem = modelMapper.map(cartItemFromRequest, CartItem.class);
        CartItem addedCartItem = cartItemService.addCartItem(mappedCartItem);
        CartItemDTO returnCartItem = cartItemModelAssembler.toModel(addedCartItem);
        return ResponseEntity.created(URI.create(returnCartItem.getLink("self").get().getHref()))
                .body(returnCartItem);
    }

    @PatchMapping
    public ResponseEntity<CartItemDTO> updateCartItems(@RequestBody List<CartItemDTO> cartItemsFromRequest) {
        try {
            cartItemService.updateCartItems(cartItemsFromRequest
                    .stream()
                    .map(cartItemDTO -> modelMapper.map(cartItemDTO, CartItem.class))
                    .collect(Collectors.toList()));
            return ResponseEntity.noContent().build();
        } catch (CartItemException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable("id") Long id) {
        try {
            cartItemService.deleteCartItemById(id);
            return ResponseEntity.noContent().build();
        }catch (CartItemException e){
            return ResponseEntity.notFound().build();
        }
    }
}
