package com.example.antonapi.service;

import com.example.antonapi.model.CartItem;
import com.example.antonapi.service.exception.CartItemException;

import java.util.List;

public interface CartItemService {
    Iterable<CartItem> getAllCartItems();
    CartItem findCartItem(Long id) throws CartItemException;
    CartItem addCartItem(CartItem cartItem);
    List<CartItem> updateCartItems(List<CartItem> cartItems) throws CartItemException;
    void deleteCartItemById(Long id) throws CartItemException;
}
