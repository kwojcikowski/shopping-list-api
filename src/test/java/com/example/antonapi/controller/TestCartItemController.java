package com.example.antonapi.controller;

import com.example.antonapi.TestModelMapperConfiguration;
import com.example.antonapi.model.*;
import com.example.antonapi.service.CartItemService;
import com.example.antonapi.service.assembler.CartItemModelAssembler;
import com.example.antonapi.service.exception.CartItemException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {CartItemModelAssembler.class})
@WebMvcTest(controllers = CartItemController.class)
@Import({CartItemController.class,
        TestModelMapperConfiguration.class})
@AutoConfigureRestDocs(outputDir = "target/generated-snippets/cart-items")
public class TestCartItemController {

    @MockBean
    private CartItemService cartItemService;

    @Autowired
    private MockMvc mockMvc;

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
        CartItem cartItem1 = CartItem.builder()
                .id(1L)
                .product(milk)
                .unit(liter)
                .quantity(new BigDecimal("2.5"))
                .build();
        when(cartItemService.findCartItem(1L)).thenReturn(cartItem1);
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
        when(cartItemService.findCartItem(1L)).thenThrow(new CartItemException());
        mockMvc.perform(get("/cartItems/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddCartItemSuccessful() throws Exception {
        String postBody =
                "{" +
                "\"product\": {" +
                "   \"id\": 1," +
                "   \"name\": \"Product One\"," +
                "   \"defaultUnit\": {" +
                "       \"id\": 1," +
                "       \"abbreviation\": \"l\"" +
                "       }," +
                "   \"section\": {" +
                "       \"id\": 1," +
                "       \"name\": \"Section One\"" +
                "       }" +
                "   }," +
                "\"unit\": {" +
                "   \"id\": 1," +
                "   \"abbreviation\": \"l\"" +
                "   }," +
                "\"quantity\": \"2.5\"" +
                "}";
        when(cartItemService.addCartItem(any(CartItem.class)))
                .thenAnswer(c -> {
                    CartItem addedCartItem = c.getArgument(0);
                    addedCartItem.setId(1L);
                    addedCartItem.setUnit(liter);
                    return addedCartItem;
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
                                subsectionWithPath("product")
                                        .description("Product that is to be added to a cart."),
                                subsectionWithPath("unit")
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
        String patchBody = "[" +
                "{" +
                "   \"id\": 1," +
                "   \"product\": {" +
                "       \"id\": 1," +
                "       \"name\": \"Product One\"," +
                "       \"defaultUnit\": {" +
                "           \"id\": 1," +
                "           \"abbreviation\": \"l\"" +
                "       }," +
                "       \"section\": {" +
                "           \"id\": 1," +
                "           \"name\": \"Section One\"" +
                "       }" +
                "   }," +
                "   \"unit\": {" +
                "       \"id\": 1," +
                "       \"abbreviation\": \"l\"}," +
                "   \"quantity\": \"2.5\"" +
                "   }," +
                "{" +
                "   \"id\": 2," +
                "   \"product\": {" +
                "       \"id\": 2," +
                "       \"name\": \"Product Two\"," +
                "       \"defaultUnit\": {" +
                "           \"id\": 2," +
                "           \"abbreviation\": \"kg\"" +
                "       }," +
                "       \"section\": {" +
                "           \"id\": 2," +
                "           \"name\": \"Section Two\"" +
                "       }" +
                "   }," +
                "   \"unit\": {" +
                "       \"id\": 1," +
                "       \"abbreviation\": \"g\"" +
                "   }," +
                "   \"quantity\": \"50\"" +
                "}" +
                "]";
        when(cartItemService.updateCartItems(anyList())).thenReturn(anyList());
        mockMvc.perform(patch("/cartItems")
                    .contentType(MediaTypes.HAL_JSON)
                    .content(patchBody))
                .andExpect(status().isNoContent())
                .andDo(document("update-cart-items",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                subsectionWithPath("[].id")
                                        .description("The id of a cart item to update."),
                                subsectionWithPath("[].product")
                                        .description("Cart item product."),
                                subsectionWithPath("[].unit")
                                        .description("Unit of a product."),
                                fieldWithPath("[].quantity")
                                        .description("Quantity of a unit.")
                        )));
    }

    @Test
    public void testUpdateCartItemsReturnNotFoundOnNonExistingId() throws Exception {
        String patchBody = "[" +
                "{" +
                "   \"product\": {" +
                "       \"id\": 1," +
                "       \"name\": \"Product One\"," +
                "       \"defaultUnit\": {" +
                "           \"id\": 1," +
                "           \"abbreviation\": \"l\"" +
                "       }," +
                "       \"section\": {" +
                "           \"id\": 1," +
                "           \"name\": \"Section One\"" +
                "       }" +
                "   }," +
                "   \"unit\": {" +
                "       \"id\": 1," +
                "       \"abbreviation\": \"l\"}," +
                "   \"quantity\": \"2.5\"" +
                "   }," +
                "{" +
                "   \"product\": {" +
                "       \"id\": 2," +
                "       \"name\": \"Product Two\"," +
                "       \"defaultUnit\": {" +
                "           \"id\": 3," +
                "           \"abbreviation\": \"kg\"" +
                "       }," +
                "       \"section\": {" +
                "           \"id\": 2," +
                "           \"name\": \"Section Two\"" +
                "       }" +
                "   }," +
                "   \"unit\": {" +
                "       \"id\": 2," +
                "       \"abbreviation\": \"g\"" +
                "   }," +
                "   \"quantity\": \"50\"" +
                "}" +
                "]";
        when(cartItemService.updateCartItems(anyList())).thenThrow(CartItemException.class);
        mockMvc.perform(patch("/cartItems")
                .contentType(MediaTypes.HAL_JSON)
                .content(patchBody))
                .andExpect(status().isNotFound());
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
    public void testDeleteCartItemReturnNotFoundOnNonExistingId() throws Exception {
        doThrow(CartItemException.class).when(cartItemService).deleteCartItemById(1L);
        mockMvc.perform(delete("/cartItems/{id}", 1))
                .andExpect(status().isNotFound());
    }
}
