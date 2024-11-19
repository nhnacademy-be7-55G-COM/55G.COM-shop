package shop.s5g.shop.controller.delivery;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
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
import shop.s5g.shop.controller.advice.RestWebAdvice;
import shop.s5g.shop.dto.delivery.DeliveryFeeResponseDto;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.delivery.DeliveryFeeService;

@WebMvcTest(
    value = DeliveryFeeController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import({TestSecurityConfig.class, AuthenticationAdvice.class})
@EnableAspectJAutoProxy
class DeliveryFeeControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    DeliveryFeeService deliveryFeeService;

    @SpyBean
    RestWebAdvice advice;

    String successUpdate = """
        {
            "id": 1,
            "fee": 3000,
            "condition": 0,
            "refundFee": 4000,
            "name": "배송비"
        }
        """;
    String failUpdate= """
        {
            "id": 1,
            "fee": -3000,
            "condition": 0,
            "refundFee": 4000,
            "name": "배송비"
        }
        """;

    DeliveryFeeResponseDto response = new DeliveryFeeResponseDto(
        1L, 3000L, 0, 3000, "배달"
    );

    @Test
    void updateDeliveryFeeFailTest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.put("/api/shop/delivery/fee")
            .contentType(MediaType.APPLICATION_JSON)
            .content(failUpdate)
        ).andExpect(status().isBadRequest());

        verify(deliveryFeeService, never()).updateFee(any());
        verify(advice, times(1)).handleBadRequestException(any());
    }

    @Test
    void updateDeliveryFeeSuccessTest() throws Exception {
        when(deliveryFeeService.updateFee(any())).thenReturn(response);

        mvc.perform(MockMvcRequestBuilders.put("/api/shop/delivery/fee")
            .contentType(MediaType.APPLICATION_JSON)
            .content(successUpdate)
        ).andExpect(status().isOk());

        verify(deliveryFeeService, times(1)).updateFee(any());
    }

    @Test
    void fetchDeliveryFees() throws Exception {
        when(deliveryFeeService.getAllDeliveryFees()).thenReturn(List.of(response));

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/delivery/fee"))
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0].fee").value(3000));
    }

    String create = """
        {
            "fee": 5000,
            "condition": 0,
            "refundFee": 7000,
            "name": "두번째 배송" 
        }
        """;
    String failCreate = """
        {
            "fee": -5000,
            "condition": 0,
            "refundFee": 7000,
            "name": "두번째 배송"
        }
        """;

    @Test
    void createDeliveryFeeFailTest() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/api/shop/delivery/fee")
            .contentType(MediaType.APPLICATION_JSON)
            .content(failCreate)
        ).andExpect(status().isBadRequest());

        verify(advice, times(1)).handleBadRequestException(any());
        verify(deliveryFeeService, never()).createFee(any());
    }
    @Test
    void createDeliveryFeeSuccessTest() throws Exception{
        mvc.perform(MockMvcRequestBuilders.post("/api/shop/delivery/fee")
            .contentType(MediaType.APPLICATION_JSON)
            .content(create)
        ).andExpect(status().isCreated());

        verify(deliveryFeeService, times(1)).createFee(any());
    }
}
