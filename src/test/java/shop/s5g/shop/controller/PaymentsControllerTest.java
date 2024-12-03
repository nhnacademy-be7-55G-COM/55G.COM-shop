package shop.s5g.shop.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.payments.AbstractPaymentManager;

@WebMvcTest(
    value = PaymentsController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import({TestSecurityConfig.class, AuthenticationAdvice.class})
@EnableAspectJAutoProxy
class PaymentsControllerTest {
    @MockBean(name = "tossPaymentsManager")
    AbstractPaymentManager paymentManager;

    @Autowired
    MockMvc mvc;

    String request = """
        {
            "detailId": 1,
            "cancelInfo": {
                "reason": "some reasons..."
            }
        }
        """;

    @Test
    void cancelPaymentTest() throws Exception{
        mvc.perform(MockMvcRequestBuilders.put("/api/shop/payment/cancel")
            .contentType(MediaType.APPLICATION_JSON)
            .content(request))
            .andExpect(jsonPath("$.message").value("canceled"));
    }
}
