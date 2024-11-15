package shop.s5g.shop.controller.order;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.controller.advice.RestWebAdvice;
import shop.s5g.shop.dto.delivery.DeliveryResponseDto;
import shop.s5g.shop.dto.order.OrderDetailInfoDto;
import shop.s5g.shop.dto.order.OrderDetailWithBookResponseDto;
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

    @SpyBean
    RestWebAdvice advice;

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
    OrderDetailWithBookResponseDto book1 = new OrderDetailWithBookResponseDto(
        1L, "테스트 타이틀1", null, 1L, "CONFIRM", 1, 14000L, 100
    );
    OrderDetailWithBookResponseDto book2 = new OrderDetailWithBookResponseDto(
        2L, "테스트 타이틀2", null, 2L, "CONFIRM", 2, 34000L, 100
    );

    List<OrderDetailWithBookResponseDto> details = List.of(
        book1 ,book2
    );

    DeliveryResponseDto delivery = new DeliveryResponseDto(
        1L, "테스트 주소", LocalDate.of(2024, 10, 30), null, 3000L, null, "아무개", "PREPARING"
    );

    OrderDetailInfoDto example = new OrderDetailInfoDto(
        details, delivery, List.of()
    );

    @Test
    void fetchOrderDetailAllTest() throws Exception{
        when(orderDetailService.getOrderDetailsWithBook(anyLong())).thenReturn(details);
        when(deliveryService.getDelivery(anyLong())).thenReturn(delivery);
        when(refundHistoryService.getRefundHistory(anyLong())).thenReturn(null);

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/orders/1")
                .queryParam("scope", "all")
            ).andExpect(content().json("""
                {
                  "details": [
                    {
                      "bookId": 1,
                      "bookTitle": "테스트 타이틀1",
                      "wrappingPaperName": null,
                      "orderDetailId": 1,
                      "orderDetailType": "CONFIRM",
                      "quantity": 1,
                      "totalPrice": 14000,
                      "accumulationPrice": 100
                    },
                    {
                      "bookId": 2,
                      "bookTitle": "테스트 타이틀2",
                      "wrappingPaperName": null,
                      "orderDetailId": 2,
                      "orderDetailType": "CONFIRM",
                      "quantity": 2,
                      "totalPrice": 34000,
                      "accumulationPrice": 100
                    }
                  ],
                  "delivery": {
                    "id": 1,
                    "address": "테스트 주소",
                    "receivedDate": "2024-10-30",
                    "shippingDate": null,
                    "fee": 3000,
                    "invoiceNumber": null,
                    "receiverName": "아무개",
                    "status": "PREPARING"
                  },
                  "refunds": [ null, null ]
                }
                """))
            .andExpect(status().isOk());

        verify(orderDetailService, times(1)).getOrderDetailsWithBook(anyLong());
        verify(deliveryService, times(1)).getDelivery(anyLong());
        verify(refundHistoryService, times(details.size())).getRefundHistory(anyLong());
    }

    @Test
    void wrongQueryStringTest() throws Exception{
        final String param = "abcd";
        mvc.perform(MockMvcRequestBuilders.get("/api/shop/orders/1")
            .queryParam("scope", param)
        ).andExpect(status().isBadRequest())
            .andExpect(content().string(containsString(param)));

        verify(advice, times(1)).handleBadRequestException(any());
    }
}
