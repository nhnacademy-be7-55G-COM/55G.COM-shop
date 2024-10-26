package shop.S5G.shop.controller.order;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import shop.S5G.shop.service.order.OrderService;

@WebMvcTest(OrderController.class)
class OrderControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    OrderService orderService;

    @Test
    void fetchOrdersEmptyTest() throws Exception{
        when(orderService.queryAllOrdersByCustomerId(anyLong(), any())).thenReturn(Page.empty());
        mvc.perform(MockMvcRequestBuilders.get("/api/shop/orders")
            .param("customerId", "3")
        )
            .andExpect(status().isOk())
            .andExpect(content().string(equalTo("[]")));
        verify(orderService, times(1)).queryAllOrdersByCustomerId(eq(3L), any());
    }
}
