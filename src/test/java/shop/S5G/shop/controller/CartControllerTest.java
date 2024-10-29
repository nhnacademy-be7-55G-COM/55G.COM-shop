package shop.S5G.shop.controller;

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
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.IfProfileValue;
import org.springframework.test.context.junit.jupiter.DisabledIf;
import org.springframework.test.web.servlet.MockMvc;
import shop.S5G.shop.config.RedisConfig;
import shop.S5G.shop.dto.cart.response.CartBooksResponseDto;
import shop.S5G.shop.service.cart.impl.CartServiceImpl;

@DisabledIf("#{T(org.springframework.util.StringUtils).hasText(environment['spring.profiles.active']) && environment['spring.profiles.active'].contains('disable-redis')}")
@WebMvcTest(CartController.class)
@Import(RedisConfig.class)
class CartControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CartServiceImpl cartServiceImpl;

    @Test
    void putBookTest() throws Exception {

        String content = "{\"sessionId\":\"testSessionId\",\"bookId\":1,\"quantity\":3}";
        mockMvc.perform(post("/api/shop/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isCreated());

        verify(cartServiceImpl, times(1)).putBook(1l, 3, "testSessionId");
    }

    @Test
    void putBookValidationFailTest() throws Exception {
        String content = "{\"sessionId\":\"\",\"bookId\":null,\"quantity\":3}";
        mockMvc.perform(post("/api/shop/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isBadRequest());

    }

    @Test
    void lookUpAllBooksTest() throws Exception {
        String sessionId = "testSessionId";
        List<CartBooksResponseDto> cartBook = List.of(
            new CartBooksResponseDto(100L, BigDecimal.valueOf(0.1), 3, 10, "title1"),
            new CartBooksResponseDto(200L, BigDecimal.valueOf(0.1), 3, 10, "title2")
        );

        when(cartServiceImpl.lookUpAllBooks(sessionId)).thenReturn(cartBook);

        mockMvc.perform(get("/api/shop/cart/testSessionId"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].price").value(100l))
            .andExpect(jsonPath("$[0].discountedPrice").value(BigDecimal.valueOf(0.1)))
            .andExpect(jsonPath("$[0].quantity").value(3))
            .andExpect(jsonPath("$[0].stock").value(10))
            .andExpect(jsonPath("$[0].title").value("title1"))
            .andExpect(jsonPath("$[1].price").value(200l))
            .andExpect(jsonPath("$[1].discountedPrice").value(BigDecimal.valueOf(0.1)))
            .andExpect(jsonPath("$[1].quantity").value(3))
            .andExpect(jsonPath("$[1].stock").value(10))
            .andExpect(jsonPath("$[1].title").value("title2"));

        verify(cartServiceImpl, times(1)).lookUpAllBooks(sessionId);
    }

    @Test
    void reduceBookQuantityInCartTest() throws Exception {
        //given
        String content = "{\"sessionId\":\"testSessionId\",\"bookId\":1}";

        //when
        mockMvc.perform(patch("/api/shop/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isOk());
        //then
        verify(cartServiceImpl, times(1)).reduceBookQuantity(anyLong(), anyString());

    }

    @Test
    void reduceBookQuantityInCartValidationFailTest() throws Exception {
        //given
        String content = "{\"sessionId\":\"\",\"bookId\":1}";

        //when
        mockMvc.perform(patch("/api/shop/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isBadRequest());
        //then
        verify(cartServiceImpl, never()).reduceBookQuantity(anyLong(), anyString());

    }

    @Test
    void deleteBookInCartTest() throws Exception {
        //given
        String content = "{\"sessionId\":\"testSessionId\",\"bookId\":1}";
        //when
        mockMvc.perform(delete("/api/shop/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isOk());
        //then
        verify(cartServiceImpl, times(1)).deleteBookFromCart(anyLong(), anyString());
    }

    @Test
    void deleteBookInCartValidationFailTest() throws Exception {
        //given
        String content = "{\"sessionId\":\"\",\"bookId\":1}";
        //when
        mockMvc.perform(delete("/api/shop/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isBadRequest());
        //then
        verify(cartServiceImpl, never()).deleteBookFromCart(anyLong(), anyString());

    }
}
