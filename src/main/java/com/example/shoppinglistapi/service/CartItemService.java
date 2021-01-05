package com.example.shoppinglistapi.service;

import com.example.shoppinglistapi.model.CartItem;
import com.example.shoppinglistapi.service.exception.CartItemException;

import java.util.List;

public interface CartItemService {
    Iterable<CartItem> getAllCartItems();
    CartItem findCartItem(Long id) throws CartItemException;
    CartItem addCartItem(CartItem cartItem);
    List<CartItem> updateCartItems(List<CartItem> cartItems) throws CartItemException;
    void deleteCartItemById(Long id) throws CartItemException;
}
