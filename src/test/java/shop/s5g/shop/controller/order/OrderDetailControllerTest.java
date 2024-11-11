package shop.s5g.shop.controller.order;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.exception.order.OrderDetailsNotExistException;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.delivery.DeliveryService;
import shop.s5g.shop.service.order.OrderDetailService;
import shop.s5g.shop.service.order.RefundHistoryService;

@WebMvcTest(
    value = OrderDetailController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import(TestSecurityConfig.class)
class OrderDetailControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    OrderDetailService orderDetailService;

    @MockBean
    DeliveryService deliveryService;

    @MockBean
    RefundHistoryService refundHistoryService;

    @Test
    void fetchOrderDetailsEmptyTest() throws Exception{
        when(orderDetailService.getOrderDetailsWithBook(anyLong())).thenReturn(List.of());

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/orders/1"))
            .andExpect(status().isOk())
            .andExpect(content().string(org.hamcrest.Matchers.equalTo("[]")));

        verify(orderDetailService, times(1)).getOrderDetailsWithBook(1L);
    }

    @Test
    void fetchOrderDetailsErrorTest() throws Exception {
        when(orderDetailService.getOrderDetailsWithBook(anyLong())).thenThrow(
            new OrderDetailsNotExistException("OrderDetails do not exist")
        );

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/orders/1"))
            .andExpect(status().isNotFound())
            .andExpect(content().string(containsString("not exist")));

        verify(orderDetailService, times(1)).getOrderDetailsWithBook(1L);
    }
}