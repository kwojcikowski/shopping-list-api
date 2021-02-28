package com.example.shoppinglistapi.service;

import com.example.shoppinglistapi.dto.cartitem.CartItemCreateDto;
import com.example.shoppinglistapi.dto.cartitem.CartItemUpdateDto;
import com.example.shoppinglistapi.model.CartItem;
import com.example.shoppinglistapi.model.Section;

import javax.persistence.EntityNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface CartItemService {
    Map<Section, List<CartItem>> getAllCartItems();
    LinkedHashMap<Section, List<CartItem>> getSortedCartItems(Long storeId);
    CartItem findCartItem(Long id) throws EntityNotFoundException;
    CartItem addCartItem(CartItemCreateDto createDto) throws EntityNotFoundException;
    List<CartItem> updateCartItems(List<CartItemUpdateDto> cartItems) throws EntityNotFoundException;
    void deleteCartItemById(Long id) throws EntityNotFoundException;
}
