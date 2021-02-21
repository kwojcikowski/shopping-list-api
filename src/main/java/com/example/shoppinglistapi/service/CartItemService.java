package com.example.shoppinglistapi.service;

import com.example.shoppinglistapi.dto.cartitem.CartItemCreateDto;
import com.example.shoppinglistapi.dto.cartitem.CartItemUpdateDto;
import com.example.shoppinglistapi.model.CartItem;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface CartItemService {
    List<CartItem> getAllCartItems();
    CartItem findCartItem(Long id) throws EntityNotFoundException;
    CartItem addCartItem(CartItemCreateDto createDto) throws EntityNotFoundException;
    List<CartItem> updateCartItems(List<CartItemUpdateDto> cartItems) throws EntityNotFoundException;
    void deleteCartItemById(Long id) throws EntityNotFoundException;
}
