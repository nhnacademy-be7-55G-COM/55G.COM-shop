package shop.s5g.shop.controller.order;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import shop.s5g.shop.annotation.WithCustomMockUser;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.dto.order.OrderCreateResponseDto;
import shop.s5g.shop.dto.order.OrderWithDetailResponseDto;
import shop.s5g.shop.exception.member.CustomerNotFoundException;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.order.OrderDetailService;
import shop.s5g.shop.service.order.OrderService;

@WebMvcTest(
    value = OrderController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import(TestSecurityConfig.class)
//@AutoConfigureMockMvc(addFilters = false)
class OrderControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    OrderService orderService;

    @MockBean
    OrderDetailService orderDetailService;
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @WithCustomMockUser(loginId = "123", customerId = 1L, role = "ROLE_MEMBER")
    void fetchOrdersEmptyTest() throws Exception{
        when(orderService.getAllOrdersWithDetail(anyLong())).thenReturn(List.of());
        mvc.perform(MockMvcRequestBuilders.get("/api/shop/orders"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("[]")))
            .andDo(print());
        verify(orderService, times(1)).getAllOrdersWithDetail(1L);
    }

    @Test
    @WithCustomMockUser(loginId = "123", customerId = 1L, role = "ROLE_MEMBER")
    void fetchOrdersTest() throws Exception {
        OrderWithDetailResponseDto dto = new OrderWithDetailResponseDto(
            1L, LocalDateTime.now(), 3000L, 5000L, "test title", 3, 4
        );
        List<OrderWithDetailResponseDto> result =  List.of(dto, dto);

        when(orderService.getAllOrdersWithDetail(anyLong())).thenReturn(result);

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/orders"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("\"representTitle\":\"test title\"")))
            .andDo(print());

        verify(orderService, times(1)).getAllOrdersWithDetail(1L);
    }

    String validatedTestCase = """
        {
            "delivery": {
                "address": "테스트 주소",
                "deliveryFeeId": 1,
                "receivedDate": "2024-10-30"
            },
            "cartList": [
                {
                    "bookId": 1,
                    "wrappingPaperId": 1,
                    "quantity": 1,
                    "totalPrice": 10000,
                    "accumulationPrice": 200
                }
            ],
            "netPrice": 99800,
            "totalPrice": 10000
        }
        """;
    // 비어있는 장바구니
    String failedTestCase = """
        {
            "delivery": {
                "address": "테스트 주소",
                "deliveryFeeId": 1,
                "receivedDate": "2024-10-30"
            },
            "cartList": [],
            "netPrice": 99800,
            "totalPrice": 10000
        }
        """;
    @Test
    @WithCustomMockUser(loginId = "123", customerId = 1L, role = "ROLE_MEMBER")
    void createNewOrderValidateFailTest() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/api/shop/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(failedTestCase)
            ).andExpect(status().isBadRequest());
        verify(orderService, never()).createOrder(eq(1L), any());
    }

    @Test
    @WithCustomMockUser(loginId = "123", customerId = 1L, role = "ROLE_MEMBER")
    void createNewOrderSuccessTest() throws Exception{
        OrderCreateResponseDto response = new OrderCreateResponseDto(1L);
        when(orderService.createOrder(anyLong(), any())).thenReturn(response);

        mvc.perform(MockMvcRequestBuilders.post("/api/shop/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(validatedTestCase)
        )
            .andExpect(status().isCreated())
            .andExpect(content().string(containsString("\"orderId\":1")))
        ;

        verify(orderService, times(1)).createOrder(eq(1L), any());
    }

    @Test
    @WithCustomMockUser(loginId = "123", customerId = 1L, role = "ROLE_MEMBER")
    void createNewOrderExceptionTest() throws Exception {
        when(orderService.createOrder(anyLong(), any())).thenThrow(CustomerNotFoundException.class);

        mvc.perform(MockMvcRequestBuilders.post("/api/shop/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validatedTestCase)
            )
            .andExpect(status().isNotFound())
        ;

        verify(orderService, times(1)).createOrder(eq(1L), any());
    }
}
