package shop.S5G.shop.controller.order;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import shop.S5G.shop.dto.order.OrderCreateResponseDto;
import shop.S5G.shop.dto.order.OrderWithDetailResponseDto;
import shop.S5G.shop.exception.member.CustomerNotFoundException;
import shop.S5G.shop.service.order.OrderDetailService;
import shop.S5G.shop.service.order.OrderService;

@WebMvcTest(OrderController.class)
class OrderControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    OrderService orderService;

    @MockBean
    OrderDetailService orderDetailService;
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void fetchOrdersEmptyTest() throws Exception{
        when(orderService.getAllOrdersWithDetail(anyLong())).thenReturn(List.of());
        mvc.perform(MockMvcRequestBuilders.get("/api/shop/orders")
            .param("customerId", "3")
        )
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("[]")))
            .andDo(print());
        verify(orderService, times(1)).getAllOrdersWithDetail(3L);
    }

    @Test
    void fetchOrdersTest() throws Exception {
        OrderWithDetailResponseDto dto = new OrderWithDetailResponseDto(
            1L, LocalDateTime.now(), 3000L, 5000L, "test title", 3, 4
        );
        List<OrderWithDetailResponseDto> result =  List.of(dto, dto);

        when(orderService.getAllOrdersWithDetail(anyLong())).thenReturn(result);

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/orders")
                .param("customerId", "3")
            )
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("\"representTitle\":\"test title\"")))
            .andDo(print());

        verify(orderService, times(1)).getAllOrdersWithDetail(3L);
    }

    String validatedTestCase = """
        {
            "customerId": 1,
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
            "customerId": 1,
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
    void createNewOrderValidateFailTest() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/api/shop/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(failedTestCase)
            ).andExpect(status().isBadRequest());
        verify(orderService, never()).createOrder(any());
    }

    @Test
    void createNewOrderSuccessTest() throws Exception{
        OrderCreateResponseDto response = new OrderCreateResponseDto(1L);
        when(orderService.createOrder(any())).thenReturn(response);

        mvc.perform(MockMvcRequestBuilders.post("/api/shop/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(validatedTestCase)
        )
            .andExpect(status().isCreated())
            .andExpect(content().string(containsString("\"orderId\":1")))
        ;

        verify(orderService, times(1)).createOrder(any());
    }

    @Test
    void createNewOrderExceptionTest() throws Exception {
        when(orderService.createOrder(any())).thenThrow(CustomerNotFoundException.class);

        mvc.perform(MockMvcRequestBuilders.post("/api/shop/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validatedTestCase)
            )
            .andExpect(status().isNotFound())
        ;

        verify(orderService, times(1)).createOrder(any());
    }
}
