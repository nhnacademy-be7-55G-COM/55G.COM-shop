package shop.s5g.shop.controller.point;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
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
import shop.s5g.shop.dto.point.PointPolicyResponseDto;
import shop.s5g.shop.dto.point.PointPolicyView;
import shop.s5g.shop.exception.EssentialDataNotFoundException;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.point.PointPolicyService;

@WebMvcTest(
    value = PointPolicyController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import(TestSecurityConfig.class)
class PointPolicyControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    PointPolicyService pointPolicyService;

    @SpyBean
    RestWebAdvice advice;

    @Test
    void getPoliciesEmptyTest() throws Exception{
        when(pointPolicyService.getAllPolicies()).thenReturn(List.of());

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/point/policies"))
            .andExpect(content().json("[]"));

        verify(pointPolicyService, times(1)).getAllPolicies();
    }

    PointPolicyResponseDto response = new PointPolicyResponseDto(
        1L, "테스트", "소스", BigDecimal.valueOf(1, 2)
    );

    @Test
    void getPoliciesTest() throws Exception {
        when(pointPolicyService.getAllPolicies()).thenReturn(List.of(response));

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/point/policies"))
            .andExpect(content().json("""
                [
                  {
                    "id": 1,
                    "name": "테스트",
                    "sourceName": "소스",
                    "value": 0.01
                  }
                ]
                """)
            );

        verify(pointPolicyService, times(1)).getAllPolicies();
    }

    @Test
    void getPurchasePointPolicyFailTest() throws Exception{
        EssentialDataNotFoundException ex = new EssentialDataNotFoundException("test");
        when(pointPolicyService.getPolicy(anyString())).thenThrow(ex);

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/point/policies/purchase"))
            .andExpect(status().isInternalServerError());

        verify(advice, times(1)).handleEssentialDataNotFoundException(ex);
    }

    PointPolicyView view = new PointPolicyView() {
        public String getName() { return "구매적립"; }
        public BigDecimal getValue() { return BigDecimal.valueOf(2, 2); }
    };

    @Test
    void getPurchasePointPolicySuccessTest() throws Exception {
        when(pointPolicyService.getPolicy(anyString())).thenReturn(view);

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/point/policies/purchase"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("구매적립"))
            .andExpect(jsonPath("$.value").value(BigDecimal.valueOf(2, 2)));

        verify(pointPolicyService, times(1)).getPolicy(anyString());
    }
}
