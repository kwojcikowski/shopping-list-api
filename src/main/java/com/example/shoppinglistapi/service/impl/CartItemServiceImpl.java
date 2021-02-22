package com.example.shoppinglistapi.service.impl;

import com.example.shoppinglistapi.dto.cartitem.CartItemCreateDto;
import com.example.shoppinglistapi.dto.cartitem.CartItemUpdateDto;
import com.example.shoppinglistapi.model.CartItem;
import com.example.shoppinglistapi.model.Product;
import com.example.shoppinglistapi.model.Unit;
import com.example.shoppinglistapi.repository.CartItemRepository;
import com.example.shoppinglistapi.repository.ProductRepository;
import com.example.shoppinglistapi.repository.UnitRepository;
import com.example.shoppinglistapi.service.CartItemService;
import com.example.shoppinglistapi.service.tools.SmartUnits;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final @NonNull CartItemRepository cartItemRepository;
    private final @NonNull UnitRepository unitRepository;
    private final @NonNull ProductRepository productRepository;

    @Override
    public List<CartItem> getAllCartItems() {
        return cartItemRepository.findAll();
    }

    @Override
    public CartItem findCartItem(Long id) throws EntityNotFoundException {
        return cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Unable to fetch cart item: " +
                        "Cart item with given id does not exist."));
    }

    @Override
    public CartItem addCartItem(CartItemCreateDto createDto) throws EntityNotFoundException {
        Unit selectedUnit = unitRepository.findById(createDto.unitId)
                .orElseThrow(() -> new EntityNotFoundException("Unable to create cart item: " +
                        "Unit with given id does not exist."));

        Product selectedProduct = productRepository.findById(createDto.productId)
                .orElseThrow(() -> new EntityNotFoundException("Unable to create cart item: " +
                        "Product with given id does not exist."));

        CartItem cartItemToCreate = CartItem.builder()
                .product(selectedProduct)
                .unit(selectedUnit)
                .quantity(createDto.quantity)
                .build();

        CartItem similarExistingCartItem = cartItemRepository
                .findByProduct_IdAndUnit_BaseUnit_Id(createDto.productId, selectedUnit.getBaseUnit().getId());
        CartItem itemToSave = similarExistingCartItem != null
                ? SmartUnits.evaluateBestUnit(similarExistingCartItem, cartItemToCreate)
                : SmartUnits.evaluateBestUnit(cartItemToCreate);
        return cartItemRepository.saveAndFlush(itemToSave);
    }

    @Override
    public List<CartItem> updateCartItems(List<CartItemUpdateDto> cartItems) throws EntityNotFoundException {
        List<CartItem> cartItemsToUpdate = new ArrayList<>();
        for (CartItemUpdateDto cartItem : cartItems) {
            try {
                CartItem cartItemToUpdate = findCartItem(cartItem.id);
                Unit selectedUnit = unitRepository.findById(cartItem.unitId)
                        .orElseThrow(() -> new EntityNotFoundException("Unable to create cart item: " +
                                "Unit with given id does not exist."));
                cartItemToUpdate.setUnit(selectedUnit);
                cartItemToUpdate.setQuantity(cartItem.quantity);
                cartItemsToUpdate.add(SmartUnits.evaluateBestUnit(cartItemToUpdate));
            } catch (EntityNotFoundException e) {
                throw new EntityNotFoundException("Unable to update cart item: " + e.getMessage());
            }
        }
        return cartItemRepository.saveAll(cartItemsToUpdate);
    }

    @Override
    public void deleteCartItemById(Long id) throws EntityNotFoundException {
        if (cartItemRepository.existsById(id))
            cartItemRepository.deleteById(id);
        else
            throw new EntityNotFoundException("Unable to delete cart item: Cart item with given id does not exist.");
    }
}
