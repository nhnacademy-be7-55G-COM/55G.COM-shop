package shop.S5G.shop.controller.cart;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.DisabledIf;
import org.springframework.test.web.servlet.MockMvc;
import shop.S5G.shop.config.RedisConfig;
import shop.S5G.shop.controller.CartController;
import shop.S5G.shop.dto.cart.response.CartBooksResponseDto;
import shop.S5G.shop.dto.cart.response.CartDetailInfoResponseDto;
import shop.S5G.shop.service.cart.CartService;
import shop.S5G.shop.service.cart.impl.CartServiceImpl;

@DisabledIf("#{T(org.springframework.util.StringUtils).hasText(environment['spring.profiles.active']) && environment['spring.profiles.active'].contains('disable-redis')}")
@WebMvcTest(CartController.class)
@Import(RedisConfig.class)
class CartControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CartService cartService;

    @Test
    void putBookTest() throws Exception {

        String content = """
            {
                "sessionId": "testSessionId",
                "bookId": 1,
                "quantity": 3
            }
            """;
        mockMvc.perform(post("/api/shop/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isCreated());

        verify(cartService, times(1)).putBook(1l, 3, "testSessionId");
    }

    @Test
    void putBookValidationFailTest() throws Exception {
        String content = """
                {
                    "sessionId": "",
                    "bookId": null,
                    "quantity": 3
                }
            """;
        mockMvc.perform(post("/api/shop/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isBadRequest());

    }

    @Test
    void lookUpAllBooksTest() throws Exception {

        //given
        String sessionId = "testSessionId";

        CartBooksResponseDto cartBooksRes1 = new CartBooksResponseDto(1l, 100L,
            BigDecimal.valueOf(90l), 3, 10, "title1");
        CartBooksResponseDto cartBooksRes2 = new CartBooksResponseDto(2l, 200L,
            BigDecimal.valueOf(180l), 3, 10, "title2");

        List<CartBooksResponseDto> cartBook = new ArrayList<>();
        cartBook.add(cartBooksRes1);
        cartBook.add(cartBooksRes2);

        CartDetailInfoResponseDto cartDetailInfoRes = new CartDetailInfoResponseDto(
            cartBook.stream().map(CartBooksResponseDto::discountedPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add), 3000, 30000);

        when(cartService.lookUpAllBooks(sessionId)).thenReturn(cartBook);
        when(cartService.getTotalPriceAndDeliverFee(cartBook)).thenReturn(cartDetailInfoRes);

        mockMvc.perform(get("/api/shop/cart/testSessionId"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$['books'][0].bookId").value(1l))
            .andExpect(jsonPath("$['books'][0].price").value(100l))
            .andExpect(jsonPath("$['books'][0].discountedPrice").value(BigDecimal.valueOf(90)))
            .andExpect(jsonPath("$['books'][0].quantity").value(3))
            .andExpect(jsonPath("$['books'][0].stock").value(10))
            .andExpect(jsonPath("$['books'][0].title").value("title1"))
            .andExpect(jsonPath("$['books'][1].bookId").value(2l))
            .andExpect(jsonPath("$['books'][1].price").value(200l))
            .andExpect(jsonPath("$['books'][1].discountedPrice").value(BigDecimal.valueOf(180)))
            .andExpect(jsonPath("$['books'][1].quantity").value(3))
            .andExpect(jsonPath("$['books'][1].stock").value(10))
            .andExpect(jsonPath("$['books'][1].title").value("title2"))
            .andExpect(jsonPath("$['feeInfo'].totalPrice").value(BigDecimal.valueOf(270)))
            .andExpect(jsonPath("$['feeInfo'].deliveryFee").value(3000))
            .andExpect(jsonPath("$['feeInfo'].freeShippingThreshold").value(30000));


        verify(cartService, times(1)).lookUpAllBooks(sessionId);
    }


    @Test
    void deleteBookInCartTest() throws Exception {
        //given
        String content = """
                {
                    "sessionId": "testSessionId",
                    "bookId": 1
                }
            """;
        //when
        mockMvc.perform(delete("/api/shop/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isOk());
        //then
        verify(cartService, times(1)).deleteBookFromCart(anyLong(), anyString());
    }

    @Test
    void deleteBookInCartValidationFailTest() throws Exception {
        //given

        String content = """
                {
                    "sessionId": "",
                    "bookId": 1
                }
            """;
        //when
        mockMvc.perform(delete("/api/shop/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isBadRequest());
        //then
        verify(cartService, never()).deleteBookFromCart(anyLong(), anyString());

    }
}
