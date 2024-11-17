package shop.s5g.shop.controller.cart;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.DisabledIf;
import org.springframework.test.web.servlet.MockMvc;
import shop.s5g.shop.annotation.WithCustomMockUser;
import shop.s5g.shop.config.RedisConfig;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.controller.CartController;
import shop.s5g.shop.dto.cart.response.CartBooksResponseDto;
import shop.s5g.shop.dto.cart.response.CartDetailInfoResponseDto;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.cart.CartService;

@DisabledIf("#{T(org.springframework.util.StringUtils).hasText(environment['spring.profiles.active']) && environment['spring.profiles.active'].contains('disable-redis')}")
@Import({RedisConfig.class, TestSecurityConfig.class})
@WebMvcTest(
    value = CartController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
class CartControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CartService cartService;

    @Test
    @WithCustomMockUser(loginId = "123", role = "ROLE_MEMBER", customerId = 2L)
    void putBookTest() throws Exception {

        String content = """
            {
                "bookId": 1,
                "quantity": 3
            }
            """;

        mockMvc.perform(post("/api/shop/cart")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
            .andExpect(status().isOk());

        verify(cartService, times(1)).putBook(1l, 3, "123");
    }

    @Test
    @WithCustomMockUser(loginId = "123", role = "ROLE_MEMBER", customerId = 2L)
    void putBookValidationFailTest() throws Exception {
        String content = """
                {
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
    @WithCustomMockUser(loginId = "123", role = "ROLE_MEMBER", customerId = 2L)
    void lookUpAllBooksTest() throws Exception {

        //given
        CartBooksResponseDto cartBooksRes1 = new CartBooksResponseDto(1l, 100L,
            BigDecimal.valueOf(90l), 3, 10, "title1", "image", true);
        CartBooksResponseDto cartBooksRes2 = new CartBooksResponseDto(2l, 200L,
            BigDecimal.valueOf(180l), 3, 10, "title2", "image", true);

        List<CartBooksResponseDto> cartBook = new ArrayList<>();
        cartBook.add(cartBooksRes1);
        cartBook.add(cartBooksRes2);

        CartDetailInfoResponseDto cartDetailInfoRes = new CartDetailInfoResponseDto(
            cartBook.stream().map(CartBooksResponseDto::discountedPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add), 3000, 30000);

        when(cartService.lookUpAllBooks("123")).thenReturn(cartBook);
        when(cartService.getTotalPriceAndDeliverFee(cartBook)).thenReturn(cartDetailInfoRes);

        mockMvc.perform(get("/api/shop/cart"))
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


        verify(cartService, times(1)).lookUpAllBooks("123");
        verify(cartService, times(1)).getTotalPriceAndDeliverFee(cartBook);
    }


    @Test
    @WithCustomMockUser(loginId = "123", role = "ROLE_MEMBER", customerId = 2L)
    void deleteBookInCartTest() throws Exception {
        //given
        String content = """
                {
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
    @WithCustomMockUser(loginId = "123", role = "ROLE_MEMBER", customerId = 2L)
    void deleteBookInCartValidationFailTest() throws Exception {
        //given

        String content = """
                {
                    "bookId": null
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
