package shop.S5G.shop.controller;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shop.S5G.shop.dto.cart.request.CartPutRequestDto;
import shop.S5G.shop.dto.cart.response.CartBooksResponseDto;
import shop.S5G.shop.service.CartService;

@WebMvcTest(CartController.class)
public class CartControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CartService cartService;

    @Test
    void putBookTest() throws Exception {

        String content = "{\"sessionId\":\"testSessionId\",\"bookId\":1,\"quantity\":3}";
        mockMvc.perform(post("/api/shop/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isCreated());

        verify(cartService,times(1)).putBook(1l,3,"testSessionId");
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

        when(cartService.lookUpAllBooks(sessionId)).thenReturn(cartBook);

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

        verify(cartService, times(1)).lookUpAllBooks(sessionId);
    }
}
