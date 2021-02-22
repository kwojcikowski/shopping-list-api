package com.example.shoppinglistapi.service.impl;

import com.example.shoppinglistapi.dto.cartitem.CartItemUpdateDto;
import com.example.shoppinglistapi.model.CartItem;
import com.example.shoppinglistapi.model.Product;
import com.example.shoppinglistapi.model.unit.Unit;
import com.example.shoppinglistapi.repository.CartItemRepository;
import com.example.shoppinglistapi.service.CartItemService;
import com.example.shoppinglistapi.service.ProductService;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.*;

public class TestCartItemServiceImpl {

    private final CartItemRepository cartItemRepository = mock(CartItemRepository.class);
    private final ProductService productService = mock(ProductService.class);

    private final CartItemService cartItemService =
            new CartItemServiceImpl(cartItemRepository, productService);

    @Test
    public void testFindAllCartItems() {
        CartItem cartItem = CartItem.builder()
                .id(1L)
                .product(mock(Product.class))
                .unit(mock(Unit.class))
                .quantity(BigDecimal.ONE)
                .build();
        CartItem cartItem1 = CartItem.builder()
                .id(2L)
                .product(mock(Product.class))
                .unit(mock(Unit.class))
                .quantity(BigDecimal.TEN)
                .build();
        List<CartItem> expectedCartItems = List.of(cartItem, cartItem1);
        when(cartItemRepository.findAll()).thenReturn(expectedCartItems);

        List<CartItem> actualCartItems = assertDoesNotThrow(
                cartItemService::getAllCartItems,
                "No exception should be thrown on fetching all cart items."
        );

        assertThat(actualCartItems).isEqualTo(expectedCartItems);
    }

    @Test
    public void testFindCartItem() {
        CartItem expectedCartItem = CartItem.builder()
                .id(1L)
                .product(mock(Product.class))
                .unit(mock(Unit.class))
                .quantity(BigDecimal.ONE)
                .build();
        when(cartItemRepository.findById(1L)).thenReturn(Optional.ofNullable(expectedCartItem));
        CartItem actualCartItem = assertDoesNotThrow(
                () -> cartItemService.findCartItem(1L),
                "No exception should be thrown on fetching existing cart item."
        );
        assertThat(actualCartItem).isEqualTo(expectedCartItem);
    }

    @Test
    public void testFindCartItemByIdThrowExceptionOnNonExistingId() {

        when(cartItemRepository.findById(1L)).thenReturn(Optional.empty());

        Exception thrownException = assertThrows(
                EntityNotFoundException.class,
                () -> cartItemService.findCartItem(1L),
                "EntityNotFoundException should be thrown on fetching non existing cart item."
        );

        assertThat(thrownException.getMessage()).isEqualTo("Unable to fetch cart item: " +
                "Cart item with given id does not exist.");
    }

    @Test
    public void testUpdateCartItems() {
        Product mockProduct = mock(Product.class);
        CartItem cartItem = CartItem.builder()
                .id(1L)
                .product(mockProduct)
                .unit(Unit.PIECE)
                .quantity(BigDecimal.ONE)
                .build();
        CartItem cartItem1 = CartItem.builder()
                .id(2L)
                .product(mockProduct)
                .unit(Unit.KILOGRAM)
                .quantity(BigDecimal.TEN)
                .build();

        CartItemUpdateDto cartItemUpdateDto = CartItemUpdateDto.builder()
                .id(1L)
                .unitAbbreviation("g")
                .quantity(new BigDecimal("30"))
                .build();
        CartItemUpdateDto cartItemUpdateDto1 = CartItemUpdateDto.builder()
                .id(2L)
                .unitAbbreviation("kg")
                .quantity(new BigDecimal("5"))
                .build();

        when(cartItemRepository.findById(1L)).thenReturn(Optional.ofNullable(cartItem));
        when(cartItemRepository.findById(2L)).thenReturn(Optional.ofNullable(cartItem1));
        when(cartItemRepository.saveAll(anyIterable())).thenAnswer(p -> p.getArgument(0));

        CartItem expectedCartItem = CartItem.builder()
                .id(1L)
                .product(mockProduct)
                .unit(Unit.GRAM)
                .quantity(new BigDecimal("30"))
                .build();
        CartItem expectedCartItem1 = CartItem.builder()
                .id(2L)
                .product(mockProduct)
                .unit(Unit.KILOGRAM)
                .quantity(new BigDecimal("5"))
                .build();
        List<CartItem> expectedCartItems = List.of(expectedCartItem, expectedCartItem1);

        List<CartItem> actualCartItems = assertDoesNotThrow(
                () -> cartItemService.updateCartItems(List.of(cartItemUpdateDto, cartItemUpdateDto1)),
                "No exception should be thrown on correct cart items update."
        );

        assertThat(actualCartItems).isEqualTo(expectedCartItems);
    }

    @Test
    public void testUpdateCartItemsThrowExceptionOnNonExistingCartItem() {
        CartItemUpdateDto cartItemUpdateDto = CartItemUpdateDto.builder()
                .id(1L)
                .unitAbbreviation("ml")
                .quantity(new BigDecimal("30"))
                .build();
        CartItemUpdateDto cartItemUpdateDto1 = CartItemUpdateDto.builder()
                .id(2L)
                .unitAbbreviation("kg")
                .quantity(new BigDecimal("5"))
                .build();

        when(cartItemRepository.findById(1L)).thenReturn(Optional.empty());

        Exception expectedException = assertThrows(
                EntityNotFoundException.class,
                () -> cartItemService.updateCartItems(List.of(cartItemUpdateDto, cartItemUpdateDto1)),
                "EntityNotFoundException should be thrown on updating non existing cart item."
        );

        assertThat(expectedException.getMessage()).isEqualTo("Unable to update cart item: " +
                "Unable to fetch cart item: Cart item with given id does not exist.");
    }

    @Test
    public void testUpdateCartItemsThrowExceptionOnNonExistingUnit() {
        CartItemUpdateDto cartItemUpdateDto = CartItemUpdateDto.builder()
                .id(1L)
                .unitAbbreviation("g")
                .quantity(new BigDecimal("30"))
                .build();
        CartItemUpdateDto cartItemUpdateDto1 = CartItemUpdateDto.builder()
                .id(2L)
                .unitAbbreviation("non-existing-unit")
                .quantity(new BigDecimal("5"))
                .build();

        CartItem existingCartItem = CartItem.builder()
                .id(1L)
                .product(mock(Product.class))
                .unit(Unit.KILOGRAM)
                .quantity(new BigDecimal("3"))
                .build();
        CartItem existingCartItem1 = CartItem.builder()
                .id(2L)
                .product(mock(Product.class))
                .unit(Unit.GRAM)
                .quantity(new BigDecimal("3"))
                .build();

        when(cartItemRepository.findById(1L)).thenReturn(Optional.ofNullable(existingCartItem));
        when(cartItemRepository.findById(2L)).thenReturn(Optional.ofNullable(existingCartItem1));

        Exception expectedException = assertThrows(
                EntityNotFoundException.class,
                () -> cartItemService.updateCartItems(List.of(cartItemUpdateDto, cartItemUpdateDto1)),
                "EntityNotFoundException should be thrown on non existing unit while updating cart item."
        );

        assertThat(expectedException.getMessage()).isEqualTo("Unable to update cart item: " +
                "Unable to find unit with provided abbreviation.");
    }

    @Test
    public void testDeleteCartItem() {
        when(cartItemRepository.existsById(1L)).thenReturn(true);
        doNothing().when(cartItemRepository).deleteById(1L);

        assertDoesNotThrow(
                () -> cartItemService.deleteCartItemById(1L),
                "No exception should be thrown on correct cart item delete."
        );
    }

    @Test
    public void testDeleteCartItemByIdThrowExceptionOnNonExistingCartItem() {
        when(cartItemRepository.existsById(1L)).thenReturn(false);

        Exception expectedException = assertThrows(
                EntityNotFoundException.class,
                () -> cartItemService.deleteCartItemById(1L),
                "EntityNotFoundException should be thrown on deleting non existing cart item."
        );

        assertThat(expectedException.getMessage()).isEqualTo("Unable to delete cart item: " +
                "Cart item with given id does not exist.");
    }
}
