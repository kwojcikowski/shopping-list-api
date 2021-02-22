package com.example.shoppinglistapi.service.impl;

import com.example.shoppinglistapi.dto.cartitem.CartItemCreateDto;
import com.example.shoppinglistapi.dto.cartitem.CartItemUpdateDto;
import com.example.shoppinglistapi.model.CartItem;
import com.example.shoppinglistapi.model.Product;
import com.example.shoppinglistapi.model.unit.Unit;
import com.example.shoppinglistapi.repository.CartItemRepository;
import com.example.shoppinglistapi.service.CartItemService;
import com.example.shoppinglistapi.service.ProductService;
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
    private final @NonNull ProductService productService;

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

        Unit selectedUnit;
        Product selectedProduct;
        try{
            selectedUnit = Unit.fromAbbreviation(createDto.unitAbbreviation);
            selectedProduct = productService.findProduct(createDto.productId);
        }catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Unable to create cart item: " + e.getMessage());
        }

        CartItem cartItemToCreate = CartItem.builder()
                .product(selectedProduct)
                .unit(selectedUnit)
                .quantity(createDto.quantity)
                .build();

        List<CartItem> similarExistingCartItems = cartItemRepository.findAllByProduct_Id(createDto.productId);
        CartItem cartItemSameBaseUnit = similarExistingCartItems.stream()
                .filter(cartItem -> cartItem.getUnit().getBaseUnit().equals(cartItemToCreate.getUnit().getBaseUnit()))
                .findFirst()
                .orElse(null);
        CartItem itemToSave = cartItemSameBaseUnit != null
                ? SmartUnits.evaluateBestUnit(cartItemSameBaseUnit, cartItemToCreate)
                : SmartUnits.evaluateBestUnit(cartItemToCreate);
        return cartItemRepository.saveAndFlush(itemToSave);
    }

    @Override
    public List<CartItem> updateCartItems(List<CartItemUpdateDto> cartItems) throws EntityNotFoundException {
        List<CartItem> cartItemsToUpdate = new ArrayList<>();
        for (CartItemUpdateDto cartItem : cartItems) {
            try {
                CartItem cartItemToUpdate = findCartItem(cartItem.id);
                Unit selectedUnit = Unit.fromAbbreviation(cartItem.unitAbbreviation);
                cartItemToUpdate.setUnit(selectedUnit);
                cartItemToUpdate.setQuantity(cartItem.quantity);
                List<CartItem> similarExistingCartItems = cartItemRepository
                        .findAllByProduct_Id(cartItemToUpdate.getProduct().getId());
                CartItem cartItemSameBaseUnit = similarExistingCartItems.stream()
                        .filter(similarCartItem -> similarCartItem.getUnit().getBaseUnit().equals(cartItemToUpdate.getUnit().getBaseUnit()))
                        .findFirst()
                        .orElse(null);
                cartItemsToUpdate.add(
                        cartItemSameBaseUnit != null
                        ? SmartUnits.evaluateBestUnit(cartItemSameBaseUnit, cartItemToUpdate)
                        : SmartUnits.evaluateBestUnit(cartItemToUpdate));
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
