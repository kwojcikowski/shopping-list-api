package com.example.shoppinglistapi.service.impl;

import com.example.shoppinglistapi.model.CartItem;
import com.example.shoppinglistapi.repository.CartItemRepository;
import com.example.shoppinglistapi.service.CartItemService;
import com.example.shoppinglistapi.service.exception.CartItemException;
import com.example.shoppinglistapi.service.tools.SmartUnits;
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
    public CartItem findCartItem(Long id) throws CartItemException{
        if(cartItemRepository.existsById(id))
            return cartItemRepository.findById(id).get();
        throw new CartItemException("Unable to fetch cart item: Cart item with id " + id + " does not exist.");
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
                throw new CartItemException("Unable to update cart item: Cart item with id "
                        + item.getId() + " does not exist.");

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
            throw new CartItemException("Unable to delete cart item: Cart item with id " + id + " does not exist.");
    }
}
