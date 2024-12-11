package shop.s5g.shop.controller.point;


import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.controller.advice.RestWebAdvice;
import shop.s5g.shop.dto.point.PointPolicyCreateRequestDto;
import shop.s5g.shop.dto.point.PointPolicyRemoveRequestDto;
import shop.s5g.shop.dto.point.PointPolicyResponseDto;
import shop.s5g.shop.dto.point.PointPolicyUpdateRequestDto;
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

    ObjectMapper objectMapper = new ObjectMapper();

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

    @Test
    void updatePolicyTest() throws Exception {
        PointPolicyUpdateRequestDto pointPolicyUpdateRequestDto = new PointPolicyUpdateRequestDto(
            1l, BigDecimal.valueOf(0.1));
        doNothing().when(pointPolicyService).updatePolicyValue(pointPolicyUpdateRequestDto);

        String requestBody = objectMapper.writeValueAsString(pointPolicyUpdateRequestDto);

        mvc.perform(MockMvcRequestBuilders.post("/api/shop/point/policies/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk());

        verify(pointPolicyService, times(1)).updatePolicyValue(pointPolicyUpdateRequestDto);
    }

    @Test
    void updatePolicyValidationFailTest() throws Exception {
        PointPolicyUpdateRequestDto pointPolicyUpdateRequestDto = new PointPolicyUpdateRequestDto(
            null, BigDecimal.valueOf(0.1));

        String requestBody = objectMapper.writeValueAsString(pointPolicyUpdateRequestDto);

        mvc.perform(MockMvcRequestBuilders.post("/api/shop/point/policies/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());

        verify(pointPolicyService, never()).updatePolicyValue(pointPolicyUpdateRequestDto);

    }

    @Test
    void createPolicyTest() throws Exception {
        PointPolicyCreateRequestDto pointPolicyCreateRequestDto = new PointPolicyCreateRequestDto(
            "testName1", "rate", BigDecimal.valueOf(0.1), 1l);
        String requestBody = objectMapper.writeValueAsString(pointPolicyCreateRequestDto);

        doNothing().when(pointPolicyService).createPointPolicy(pointPolicyCreateRequestDto);

        mvc.perform(MockMvcRequestBuilders.post("/api/shop/point/policies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk());

        verify(pointPolicyService, times(1)).createPointPolicy(pointPolicyCreateRequestDto);
    }

    @Test
    void createPolicyValidationFailTest() throws Exception {
        PointPolicyCreateRequestDto pointPolicyCreateRequestDto = new PointPolicyCreateRequestDto(
            null, "rate", BigDecimal.valueOf(0.1), 1l);
        String requestBody = objectMapper.writeValueAsString(pointPolicyCreateRequestDto);

        mvc.perform(MockMvcRequestBuilders.post("/api/shop/point/policies/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());

        verify(pointPolicyService, never()).createPointPolicy(pointPolicyCreateRequestDto);

    }

    @Test
    void removePolicyTest() throws Exception {
        PointPolicyRemoveRequestDto pointPolicyRemoveRequestDto = new PointPolicyRemoveRequestDto(
            "testName", 1l);
        String requestBody = objectMapper.writeValueAsString(pointPolicyRemoveRequestDto);
        doNothing().when(pointPolicyService).removePointPolicy(pointPolicyRemoveRequestDto);

        mvc.perform(MockMvcRequestBuilders.delete("/api/shop/point/policies/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isOk());

        verify(pointPolicyService, times(1)).removePointPolicy(pointPolicyRemoveRequestDto);
    }

    @Test
    void removePolicyValidationFailTest() throws Exception {
        PointPolicyRemoveRequestDto pointPolicyRemoveRequestDto = new PointPolicyRemoveRequestDto(
            null, 1l);
        String requestBody = objectMapper.writeValueAsString(pointPolicyRemoveRequestDto);

        mvc.perform(MockMvcRequestBuilders.delete("/api/shop/point/policies/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isBadRequest());

        verify(pointPolicyService, never()).removePointPolicy(pointPolicyRemoveRequestDto);
    }





}
