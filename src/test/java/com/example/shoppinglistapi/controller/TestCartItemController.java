package com.example.shoppinglistapi.controller;

import com.example.shoppinglistapi.dto.cartitem.CartItemCreateDto;
import com.example.shoppinglistapi.model.CartItem;
import com.example.shoppinglistapi.model.Product;
import com.example.shoppinglistapi.model.Section;
import com.example.shoppinglistapi.model.unit.Unit;
import com.example.shoppinglistapi.service.CartItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.MockMvc;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureRestDocs(outputDir = "target/generated-snippets/cart-items")
@SpringBootTest
@AutoConfigureMockMvc
public class TestCartItemController {

    @MockBean
    private CartItemService cartItemService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetAllCartItems() throws Exception {
        Section dairy = Section.builder()
                .id(1L)
                .name("Dairy")
                .build();
        Section fruits = Section.builder()
                .id(2L)
                .name("Fruits")
                .build();
        Product milk = Product.builder()
                .id(1L)
                .name("Milk")
                .defaultUnit(Unit.LITER)
                .section(dairy)
                .build();
        Product banana = Product.builder()
                .id(2L)
                .name("Banana")
                .defaultUnit(Unit.PIECE)
                .section(fruits)
                .build();
        CartItem bananaCartItem = CartItem.builder()
                .id(1L)
                .product(banana)
                .unit(Unit.PIECE)
                .quantity(new BigDecimal("4"))
                .build();
        CartItem milkCartItem = CartItem.builder()
                .id(2L)
                .product(milk)
                .unit(Unit.MILLILITER)
                .quantity(new BigDecimal("250"))
                .build();
        when(cartItemService.getAllCartItems()).thenReturn(List.of(bananaCartItem, milkCartItem));
        mockMvc.perform(get("/cartItems"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andDo(document("get-all-cart-items",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("_embedded.cartItems[].id")
                                        .description("A unique identifier for this cart item."),
                                subsectionWithPath("_embedded.cartItems[].product")
                                        .description("Product that is added to the cart."),
                                subsectionWithPath("_embedded.cartItems[].unit")
                                        .description("Unit of a product"),
                                fieldWithPath("_embedded.cartItems[].quantity")
                                        .description("Quantity of a unit."),
                                subsectionWithPath("_embedded.cartItems[]._links")
                                        .description("Links to resources.")
                        )));
    }

    @Test
    public void testGetCartItemByIdSuccessful() throws Exception {
        Section fruits = Section.builder()
                .id(2L)
                .name("Fruits")
                .build();
        Product banana = Product.builder()
                .id(2L)
                .name("Banana")
                .defaultUnit(Unit.PIECE)
                .section(fruits)
                .build();
        CartItem bananaCartItem = CartItem.builder()
                .id(1L)
                .product(banana)
                .unit(Unit.PIECE)
                .quantity(new BigDecimal("4"))
                .build();
        when(cartItemService.findCartItem(1L)).thenReturn(bananaCartItem);
        mockMvc.perform(get("/cartItems/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andDo(document("get-cart-item-by-id",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id")
                                        .description("The id of a cart item to be fetched.")
                        ),
                        responseFields(
                                fieldWithPath("id")
                                        .description("A unique identifier for this cart item."),
                                subsectionWithPath("product")
                                        .description("Product that is added to the cart."),
                                subsectionWithPath("unit")
                                        .description("Unit of a product"),
                                fieldWithPath("quantity")
                                        .description("Quantity of a unit."),
                                subsectionWithPath("_links")
                                        .description("Links to resources.")
                        ),
                        links(
                                linkWithRel("self")
                                        .description("Self reference to this cart item.")
                        )));
    }

    @Test
    public void testGetCartItemByIdReturnNotFoundOnNonExistingId() throws Exception {
        when(cartItemService.findCartItem(1L)).thenThrow(new EntityNotFoundException());
        mockMvc.perform(get("/cartItems/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddCartItemSuccessful() throws Exception {
        Section fruits = Section.builder()
                .id(1L)
                .name("Fruits")
                .build();
        Product banana = Product.builder()
                .id(1L)
                .name("Banana")
                .defaultUnit(Unit.PIECE)
                .section(fruits)
                .build();
        String postBody =
                "{" +
                        "\"productId\": 1," +
                        "\"unitAbbreviation\": \"pcs\"," +
                        "\"quantity\": 5" +
                        "}";
        when(cartItemService.addCartItem(any(CartItemCreateDto.class)))
                .thenAnswer(c -> {
                    CartItemCreateDto addedCartItem = c.getArgument(0);
                    return CartItem.builder()
                            .id(1L)
                            .product(banana)
                            .unit(Unit.fromAbbreviation(addedCartItem.unitAbbreviation))
                            .quantity(addedCartItem.quantity)
                            .build();
                });
        mockMvc.perform(post("/cartItems")
                .contentType(MediaTypes.HAL_JSON)
                .content(postBody))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaTypes.HAL_JSON))
                .andDo(document("add-cart-item",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                subsectionWithPath("productId")
                                        .description("Id of a product that is to be added to a cart."),
                                subsectionWithPath("unitAbbreviation")
                                        .description("Chosen unit of a product."),
                                fieldWithPath("quantity")
                                        .description("Quantity of a unit.")
                        ),
                        responseFields(
                                fieldWithPath("id")
                                        .description("A unique identifier for this cart item."),
                                subsectionWithPath("product")
                                        .description("Product that is added to the cart."),
                                subsectionWithPath("unit")
                                        .description("Unit of a product"),
                                fieldWithPath("quantity")
                                        .description("Quantity of a unit."),
                                subsectionWithPath("_links")
                                        .description("Links to resources.")
                        ),
                        links(
                                linkWithRel("self")
                                        .description("Self reference to this cart item.")
                        )));
    }

    @Test
    public void testUpdateCartItemsSuccessful() throws Exception {
        Section dairy = Section.builder()
                .id(1L)
                .name("Dairy")
                .build();
        Section fruits = Section.builder()
                .id(2L)
                .name("Fruits")
                .build();
        Product milk = Product.builder()
                .id(1L)
                .name("Milk")
                .defaultUnit(Unit.LITER)
                .section(dairy)
                .build();
        Product banana = Product.builder()
                .id(2L)
                .name("Banana")
                .defaultUnit(Unit.PIECE)
                .section(fruits)
                .build();
        CartItem expectedBananaCartItem = CartItem.builder()
                .id(1L)
                .product(banana)
                .unit(Unit.PIECE)
                .quantity(new BigDecimal("1"))
                .build();
        CartItem expectedMilkCartItem = CartItem.builder()
                .id(2L)
                .product(milk)
                .unit(Unit.LITER)
                .quantity(new BigDecimal("3"))
                .build();

        String patchBody =
                "[" +
                        "{" +
                        "\"id\": 1," +
                        "\"unitAbbreviation\": \"pcs\"," +
                        "\"quantity\": 1" +
                        "}," +
                        "{" +
                        "\"id\": 2," +
                        "\"unitAbbreviation\": \"l\"," +
                        "\"quantity\": 3" +
                        "}" +
                        "]";
        when(cartItemService.updateCartItems(anyList()))
                .thenReturn(List.of(expectedBananaCartItem, expectedMilkCartItem));
        mockMvc.perform(patch("/cartItems")
                .contentType(MediaTypes.HAL_JSON)
                .content(patchBody))
                .andExpect(status().isOk())
                .andDo(document("update-cart-items",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                subsectionWithPath("[].id")
                                        .description("The id of a cart item to update."),
                                fieldWithPath("[].unitAbbreviation")
                                        .description("Unit of a product."),
                                fieldWithPath("[].quantity")
                                        .description("Quantity of a unit.")
                        ),
                        responseFields(
                                fieldWithPath("_embedded.cartItems[].id")
                                        .description("A unique identifier for this cart item."),
                                subsectionWithPath("_embedded.cartItems[].product")
                                        .description("Product that is added to the cart."),
                                subsectionWithPath("_embedded.cartItems[].unit")
                                        .description("Unit of a product"),
                                fieldWithPath("_embedded.cartItems[].quantity")
                                        .description("Quantity of a unit."),
                                subsectionWithPath("_embedded.cartItems[]._links")
                                        .description("Links to resources.")
                        )));
    }

    @Test
    public void testUpdateCartItemsReturnBadRequestOnNonExistingCartItem() throws Exception {
        String patchBody =
                "[" +
                        "{" +
                        "\"id\": 1," +
                        "\"unitAbbreviation\": \"pcs\"," +
                        "\"quantity\": 1" +
                        "}," +
                        "{" +
                        "\"id\": 2," +
                        "\"unitAbbreviation\": \"l\"," +
                        "\"quantity\": 3" +
                        "}" +
                        "]";
        when(cartItemService.updateCartItems(anyList()))
                .thenThrow(new EntityNotFoundException("Unable to update cart item: Unable to fetch cart item: " +
                        "Cart item with given id does not exist."));
        mockMvc.perform(patch("/cartItems")
                .contentType(MediaTypes.HAL_JSON)
                .content(patchBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteCartItemSuccessful() throws Exception {
        doNothing().when(cartItemService).deleteCartItemById(1L);
        mockMvc.perform(delete("/cartItems/{id}", 1))
                .andExpect(status().isNoContent())
                .andDo(document("remove-cart-item",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        pathParameters(
                                parameterWithName("id")
                                        .description("The id of a cart item to be removed.")
                        )));
    }

    @Test
    public void testDeleteCartItemReturnBadRequestOnNonExistingId() throws Exception {
        doThrow(EntityNotFoundException.class).when(cartItemService).deleteCartItemById(1L);
        mockMvc.perform(delete("/cartItems/{id}", 1))
                .andExpect(status().isBadRequest());
    }
}
