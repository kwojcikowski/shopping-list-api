package com.example.shoppinglistapi.controller;

import com.example.shoppinglistapi.dto.cartitem.CartItemCreateDto;
import com.example.shoppinglistapi.dto.cartitem.CartItemReadDto;
import com.example.shoppinglistapi.dto.cartitem.CartItemUpdateDto;
import com.example.shoppinglistapi.model.CartItem;
import com.example.shoppinglistapi.service.CartItemService;
import com.example.shoppinglistapi.service.assembler.CartItemModelAssembler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RepositoryRestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping(path = "/cartItems")
public class CartItemController {

    private final @NonNull CartItemModelAssembler cartItemModelAssembler;
    private final @NonNull CartItemService cartItemService;

    @GetMapping
    public ResponseEntity<?> getAllCartItems() {
        return ResponseEntity.ok(cartItemModelAssembler.toMapModel(cartItemService.getAllCartItems()));
    }

    @GetMapping("/sorted")
    public ResponseEntity<?> getSortedCartItems(@RequestParam("sortingStore") Long storeId) {
        try {
            return ResponseEntity.ok(cartItemModelAssembler.toMapModel(cartItemService.getSortedCartItems(storeId)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CartItemReadDto> getCartItemById(@PathVariable("id") Long id) {
        try {
            return ResponseEntity.ok(cartItemModelAssembler.toModel(cartItemService.findCartItem(id)));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> addCartItem(@RequestBody @Valid CartItemCreateDto createDto) {
        CartItem addedCartItem = cartItemService.addCartItem(createDto);
        try {
            CartItemReadDto returnCartItem = cartItemModelAssembler.toModel(addedCartItem);
            URI cartItemUri = URI.create(returnCartItem.getLinks().getRequiredLink("self").getHref());
            return ResponseEntity.created(cartItemUri).body(returnCartItem);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PatchMapping
    public ResponseEntity<?> updateCartItems(@RequestBody @Valid List<CartItemUpdateDto> requestCart) {
        try {
            List<CartItem> updatedCartItems = cartItemService.updateCartItems(requestCart);
            return ResponseEntity.ok(cartItemModelAssembler.toCollectionModel(updatedCartItems));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable("id") Long id) {
        try {
            cartItemService.deleteCartItemById(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
