package com.example.antonapi.service.impl;

import com.example.antonapi.model.CartItem;
import com.example.antonapi.model.Product;
import com.example.antonapi.model.Unit;
import com.example.antonapi.repository.CartItemRepository;
import com.example.antonapi.service.CartItemService;
import com.example.antonapi.service.exception.CartItemException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestCartItemServiceImpl {

    @Test
    public void testUpdateCartItemsNoExceptionOnCorrectCartItems(){
        //Given
        CartItemRepository cartItemRepository = mock(CartItemRepository.class);
        Product mockProduct = mock(Product.class);
        Unit mockUnit = mock(Unit.class);
        BigDecimal mockQuantity = mock(BigDecimal.class);
        CartItem correctCartItem = new CartItem(1L, mockProduct, mockUnit, mockQuantity);
        CartItem correctCartItem1 = new CartItem(2L, mockProduct, mockUnit, mockQuantity);
        CartItem correctCartItem2 = new CartItem(3L, mockProduct, mockUnit, mockQuantity);
        List<CartItem> testedCartItems = List.of(correctCartItem, correctCartItem1, correctCartItem2);
        when(cartItemRepository.existsById(1L)).thenReturn(true);
        when(cartItemRepository.existsById(2L)).thenReturn(true);
        when(cartItemRepository.existsById(3L)).thenReturn(true);
        when(cartItemRepository.saveAndFlush(correctCartItem)).thenReturn(correctCartItem);
        when(cartItemRepository.saveAndFlush(correctCartItem1)).thenReturn(correctCartItem1);
        when(cartItemRepository.saveAndFlush(correctCartItem2)).thenReturn(correctCartItem2);
        CartItemService service = new CartItemServiceImpl(cartItemRepository);

        //When
        try {
            List<CartItem> items = service.updateCartItems(testedCartItems);

        //Then
            assertThat(items).isEqualTo(testedCartItems);
        } catch (CartItemException e) {
            fail("Exception should not had been thrown.");
        }
    }

    @Test
    public void testUpdateCartItemsThrowExceptionOnNullCartItemId(){
        //Given
        CartItemRepository cartItemRepository = mock(CartItemRepository.class);
        Product mockProduct = mock(Product.class);
        Unit mockUnit = mock(Unit.class);
        BigDecimal mockQuantity = mock(BigDecimal.class);
        CartItem correctCartItem = new CartItem(1L, mockProduct, mockUnit, mockQuantity);
        CartItem incorrectCartItem = new CartItem(null, mockProduct, mockUnit, mockQuantity);
        CartItem correctCartItem1 = new CartItem(2L, mockProduct, mockUnit, mockQuantity);
        List<CartItem> testedCartItems = List.of(correctCartItem, incorrectCartItem, correctCartItem1);
        when(cartItemRepository.existsById(1L)).thenReturn(true);
        when(cartItemRepository.existsById(2L)).thenReturn(true);
        when(cartItemRepository.saveAndFlush(correctCartItem)).thenReturn(correctCartItem);
        when(cartItemRepository.saveAndFlush(correctCartItem1)).thenReturn(correctCartItem1);
        CartItemService service = new CartItemServiceImpl(cartItemRepository);

        //When
        try {
            service.updateCartItems(testedCartItems);

        //Then
            fail("Exception should had been thrown.");
        } catch (CartItemException e) {
            assertThat(e.getMessage()).isEqualTo("Unable to update cart item: No ID provided.");
        }
    }

    @Test
    public void testUpdateCartItemsThrowExceptionOnNonExistingCartItem(){
        //Given
        CartItemRepository cartItemRepository = mock(CartItemRepository.class);
        Product mockProduct = mock(Product.class);
        Unit mockUnit = mock(Unit.class);
        BigDecimal mockQuantity = mock(BigDecimal.class);
        CartItem correctCartItem = new CartItem(1L, mockProduct, mockUnit, mockQuantity);
        CartItem correctCartItem1 = new CartItem(2L, mockProduct, mockUnit, mockQuantity);
        CartItem correctCartItem2 = new CartItem(3L, mockProduct, mockUnit, mockQuantity);
        List<CartItem> testedCartItems = List.of(correctCartItem, correctCartItem1, correctCartItem2);
        when(cartItemRepository.existsById(1L)).thenReturn(true);
        when(cartItemRepository.existsById(2L)).thenReturn(true);
        when(cartItemRepository.existsById(3L)).thenReturn(false);
        when(cartItemRepository.saveAndFlush(correctCartItem)).thenReturn(correctCartItem);
        when(cartItemRepository.saveAndFlush(correctCartItem1)).thenReturn(correctCartItem1);
        when(cartItemRepository.saveAndFlush(correctCartItem2)).thenReturn(correctCartItem2);
        CartItemService service = new CartItemServiceImpl(cartItemRepository);

        //When
        try {
            service.updateCartItems(testedCartItems);

        //Then
            fail("Exception should had been thrown.");
        } catch (CartItemException e) {
            assertThat(e.getMessage()).isEqualTo("Unable to update cart item: Cart item does not exist.");
        }
    }

    @Test
    public void testDeleteCartItemByIdNoExceptionOnCorrectCartItem() {
        //Given
        CartItemRepository cartItemRepository = mock(CartItemRepository.class);
        when(cartItemRepository.existsById(1L)).thenReturn(true);
        CartItemService service = new CartItemServiceImpl(cartItemRepository);

        //When
        try {
            service.deleteCartItemById(1L);

        //Then
        } catch (CartItemException e) {
            fail("Exception should not had been thrown.");
        }
    }

    @Test
    public void testDeleteCartItemByIdThrowExceptionOnNonExistingCartItem(){
        //Given
        CartItemRepository cartItemRepository = mock(CartItemRepository.class);
        when(cartItemRepository.existsById(1L)).thenReturn(false);
        CartItemService service = new CartItemServiceImpl(cartItemRepository);

        //When
        try {
            service.deleteCartItemById(1L);
            fail("Exception should had been thrown.");
            //Then
        } catch (CartItemException e) {
            assertThat(e.getMessage()).isEqualTo("Unable to delete cart item: Cart item does not exist.");
        }
    }
}