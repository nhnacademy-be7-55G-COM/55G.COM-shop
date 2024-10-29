package shop.S5G.shop.controller.order;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import shop.S5G.shop.dto.order.OrderWithDetailResponseDto;
import shop.S5G.shop.exception.order.OrderDetailsNotExistException;
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

    @Test
    void fetchOrdersEmptyTest() throws Exception{
        when(orderService.queryAllOrdersByCustomerId(anyLong())).thenReturn(List.of());
        mvc.perform(MockMvcRequestBuilders.get("/api/shop/orders")
            .param("customerId", "3")
        )
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("[]")))
            .andDo(print());
        verify(orderService, times(1)).queryAllOrdersByCustomerId(eq(3L));
    }

    @Test
    void fetchOrdersTest() throws Exception {
        OrderWithDetailResponseDto dto = new OrderWithDetailResponseDto(
            1L, LocalDateTime.now(), 3000L, 5000L, "test title", 3, 4
        );
        List<OrderWithDetailResponseDto> result =  List.of(dto, dto);

        when(orderService.queryAllOrdersByCustomerId(anyLong())).thenReturn(result);

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/orders")
                .param("customerId", "3")
            )
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("\"representTitle\":\"test title\"")))
            .andDo(print());

        verify(orderService, times(1)).queryAllOrdersByCustomerId(eq(3L));
    }

    @Test
    void fetchOrderDetailsEmptyTest() throws Exception{
        when(orderDetailService.findOrderDetailsByOrderId(anyLong())).thenReturn(List.of());

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/orders/1"))
            .andExpect(status().isOk())
            .andExpect(content().string(org.hamcrest.Matchers.equalTo("[]")));

        verify(orderDetailService, times(1)).findOrderDetailsByOrderId(eq(1L));
    }

    @Test
    void fetchOrderDetailsErrorTest() throws Exception {
        when(orderDetailService.findOrderDetailsByOrderId(anyLong())).thenThrow(
            new OrderDetailsNotExistException("OrderDetails do not exist")
        );

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/orders/1"))
            .andExpect(status().isNotFound())
            .andExpect(content().string(containsString("not exist")));

        verify(orderDetailService, times(1)).findOrderDetailsByOrderId(eq(1L));
    }
}
