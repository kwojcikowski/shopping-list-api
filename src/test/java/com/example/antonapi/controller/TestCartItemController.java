package com.example.antonapi.controller;

import com.example.antonapi.TestModelMapperConfiguration;
import com.example.antonapi.model.*;
import com.example.antonapi.service.CartItemService;
import com.example.antonapi.service.assembler.CartItemModelAssembler;
import com.example.antonapi.service.dto.CartItemDTO;
import com.example.antonapi.service.exception.CartItemException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {CartItemModelAssembler.class})
@WebMvcTest(controllers = CartItemController.class)
@Import({CartItemController.class,
        TestModelMapperConfiguration.class})
public class TestCartItemController {

    @MockBean
    private CartItemService cartItemService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ModelMapper modelMapper;

    Section dairy = Section.builder()
            .id(1L)
            .name("Dairy")
            .build();
    Prefix none = Prefix.builder()
            .id(1L)
            .name("NONE")
            .abbreviation("")
            .scale(1.0)
            .build();
    BaseUnit baseUnitLiter = BaseUnit.builder()
            .id(1L)
            .name("LITER")
            .abbreviation("l")
            .build();
    Unit liter = Unit.builder()
            .id(1L)
            .baseUnit(baseUnitLiter)
            .prefix(none)
            .build();
    Product milk = Product.builder()
            .id(1L)
            .name("Milk")
            .defaultUnit(liter)
            .section(dairy)
            .build();

    @Test
    public void testGetAllCartItems() throws Exception {
        CartItem cartItem1 = CartItem.builder()
                .id(1L)
                .product(milk)
                .unit(liter)
                .quantity(new BigDecimal("2.5"))
                .build();
        CartItem cartItem2 = CartItem.builder()
                .id(2L)
                .product(milk)
                .unit(liter)
                .quantity(new BigDecimal("4"))
                .build();
        when(cartItemService.getAllCartItems()).thenReturn(List.of(cartItem1, cartItem2));
        mockMvc.perform(get("/cartItems"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andExpect(jsonPath("$._embedded.cartItems", hasSize(2)));
    }

    @Test
    public void testGetCartItemByIdSuccessful() throws Exception {
        CartItem cartItem1 = CartItem.builder()
                .id(1L)
                .product(milk)
                .unit(liter)
                .quantity(new BigDecimal("2.5"))
                .build();
        when(cartItemService.findCartItem(1L)).thenReturn(cartItem1);
        mockMvc.perform(get("/cartItems/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON));
    }

    @Test
    public void testGetCartItemByIdReturnNotFoundOnNonExistingId() throws Exception {
        when(cartItemService.findCartItem(1L)).thenThrow(new CartItemException());
        mockMvc.perform(get("/cartItems/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddCartItemSuccessful() throws Exception {
        CartItem cartItem1 = CartItem.builder()
                .product(milk)
                .unit(liter)
                .quantity(new BigDecimal("2.5"))
                .build();
        CartItemDTO cartItemDTO = modelMapper.map(cartItem1, CartItemDTO.class);
        String postBody = new ObjectMapper().writeValueAsString(cartItemDTO);
        when(cartItemService.addCartItem(any(CartItem.class)))
                .thenAnswer(c -> {
                    CartItem addedCartItem = c.getArgument(0);
                    addedCartItem.setId(1L);
                    return addedCartItem;
                });
        mockMvc.perform(post("/cartItems")
                    .contentType(MediaTypes.HAL_JSON)
                    .content(postBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON));
    }

    @Test
    public void testUpdateCartItemsSuccessful() throws Exception {
        CartItem cartItem1 = CartItem.builder()
                .id(1L)
                .product(milk)
                .unit(liter)
                .quantity(new BigDecimal("2.5"))
                .build();
        CartItem cartItem2 = CartItem.builder()
                .id(2L)
                .product(milk)
                .unit(liter)
                .quantity(new BigDecimal("4"))
                .build();
        List<CartItem> cartItems = List.of(cartItem1, cartItem2);
        List<CartItemDTO> cartItemDTOs = cartItems
                .stream()
                .map(c -> modelMapper.map(c, CartItemDTO.class))
                .collect(Collectors.toList());
        String patchBody = new ObjectMapper().writeValueAsString(cartItemDTOs);
        when(cartItemService.updateCartItems(anyList())).thenReturn(cartItems);
        mockMvc.perform(patch("/cartItems")
                    .contentType(MediaTypes.HAL_JSON)
                    .content(patchBody))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUpdateCartItemsReturnNotFoundOnNonExistingId() throws Exception {
        CartItem cartItem1 = CartItem.builder()
                .id(1L)
                .product(milk)
                .unit(liter)
                .quantity(new BigDecimal("2.5"))
                .build();
        CartItem cartItem2 = CartItem.builder()
                .id(2L)
                .product(milk)
                .unit(liter)
                .quantity(new BigDecimal("4"))
                .build();
        List<CartItem> cartItems = List.of(cartItem1, cartItem2);
        List<CartItemDTO> cartItemDTOs = cartItems
                .stream()
                .map(c -> modelMapper.map(c, CartItemDTO.class))
                .collect(Collectors.toList());
        String patchBody = new ObjectMapper().writeValueAsString(cartItemDTOs);
        when(cartItemService.updateCartItems(anyList())).thenThrow(CartItemException.class);
        mockMvc.perform(patch("/cartItems")
                .contentType(MediaTypes.HAL_JSON)
                .content(patchBody))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteCartItemSuccessful() throws Exception {
        doNothing().when(cartItemService).deleteCartItemById(1L);
        mockMvc.perform(delete("/cartItems/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testDeleteCartItemReturnNotFoundOnNonExistingId() throws Exception {
        doThrow(CartItemException.class).when(cartItemService).deleteCartItemById(1L);
        mockMvc.perform(delete("/cartItems/1"))
                .andExpect(status().isNotFound());
    }
}
