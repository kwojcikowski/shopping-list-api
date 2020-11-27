package com.example.antonapi.service.impl;

import com.example.antonapi.model.CartItem;
import com.example.antonapi.repository.CartItemRepository;
import com.example.antonapi.service.CartItemService;
import com.example.antonapi.service.exception.CartItemException;
import com.example.antonapi.service.tools.SmartUnits;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final @NonNull CartItemRepository cartItemRepository;

    @Override
    public Iterable<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    @Override
    public CartItem findCartItem(Long id) {
        return cartItemRepository.getOne(id);
    }

    @Override
    public CartItem addCartItem(CartItem cartItem) {
        CartItem existing = cartItemRepository
                .findByProductAndUnit_BaseUnit(cartItem.getProduct(), cartItem.getUnit().getBaseUnit());
        CartItem itemToSave = existing != null
                ? SmartUnits.evaluateBestUnit(existing, cartItem)
                : SmartUnits.evaluateBestUnit(cartItem);
        return cartItemRepository.saveAndFlush(itemToSave);
    }

    @Override
    public List<CartItem> updateCartItems(List<CartItem> cartItems) throws CartItemException {
        List<CartItem> updatedCartItems = new ArrayList<>();
        for(CartItem item: cartItems){
            if(item.getId() == null)
                throw new CartItemException("Unable to update cart item: No ID provided.");
            if(!cartItemRepository.existsById(item.getId()))
                throw new CartItemException("Unable to update cart item: Cart item does not exist.");

            CartItem updatedCartItem = cartItemRepository.saveAndFlush(item);
            updatedCartItems.add(updatedCartItem);
        }
        return updatedCartItems;
    }

    @Override
    public void deleteCartItemById(Long id) throws CartItemException {
        if(cartItemRepository.existsById(id))
            cartItemRepository.deleteById(id);
        else
            throw new CartItemException("Unable to delete cart item: Cart item does not exist.");
    }
}