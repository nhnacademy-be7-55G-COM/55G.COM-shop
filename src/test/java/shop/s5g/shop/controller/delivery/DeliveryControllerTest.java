package shop.s5g.shop.controller.delivery;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import shop.s5g.shop.advice.AuthenticationAdvice;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.dto.delivery.DeliveryResponseDto;
import shop.s5g.shop.dto.delivery.DeliveryUpdateRequestDto;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.delivery.DeliveryService;

@WebMvcTest(
    value = DeliveryController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import({TestSecurityConfig.class, AuthenticationAdvice.class})
@EnableAspectJAutoProxy
class DeliveryControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    DeliveryService deliveryService;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    DeliveryUpdateRequestDto update = new DeliveryUpdateRequestDto(
        1L, "주소", null, LocalDate.now().plusDays(1)
    , null, "아무나", "PREPARING");

    String updateRequest = """
        {
            "id": 1,
            "address": "주소",
            "invoiceNumber": null,
            "receivedDate": "2090-10-30",
            "shippingDate": null,
            "receiverName": "아무나",
            "status": "PREPARING"
        }
        """;
    String failRequest = """
        {
            "id": 1,
            "address": "주소",
            "invoiceNumber": null,
            "receivedDate": "1999-10-30",
            "shippingDate": null,
            "receiverName": "아무나",
            "status": "PREPARING"
        }
        """;

    DeliveryResponseDto delivery = new DeliveryResponseDto(
        1L, "주소", LocalDate.of(2090, 10, 30), null, 3000L, null, "아무나", "PREPARING"
    );

    @Test
    void adminUpdateDeliveryTest() throws Exception{
        when(deliveryService.adminUpdateDelivery(any())).thenReturn(delivery);

        mvc.perform(MockMvcRequestBuilders.put("/api/shop/delivery/admin")
            .content(updateRequest)
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        verify(deliveryService, times(1)).adminUpdateDelivery(any());
    }

    @Test
    void adminUpdateDeliveryFailTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/shop/delivery/admin")
                .content(failRequest)
                .contentType(MediaType.APPLICATION_JSON)
            ).andExpect(status().isBadRequest());

        verify(deliveryService, never()).adminUpdateDelivery(any());
    }

    @Test
    void userUpdateDeliveryFailTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/shop/delivery")
            .content(failRequest)
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest());

        verify(deliveryService, never()).userUpdateDelivery(any());
    }

    @Test
    void userUpdateDeliveryTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/shop/delivery")
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateRequest)
        ).andExpect(status().isOk());

        verify(deliveryService, times(1)).userUpdateDelivery(any());
    }

}
