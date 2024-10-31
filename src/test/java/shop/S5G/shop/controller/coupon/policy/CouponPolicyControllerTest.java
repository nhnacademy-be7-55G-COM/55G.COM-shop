package shop.S5G.shop.controller.coupon.policy;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import shop.S5G.shop.controller.coupon.CouponPolicyController;
import shop.S5G.shop.dto.coupon.policy.CouponPolicyRequestDto;
import shop.S5G.shop.dto.coupon.policy.CouponPolicyResponseDto;
import shop.S5G.shop.service.coupon.policy.impl.CouponPolicyServiceImpl;

@AutoConfigureRestDocs
@ActiveProfiles("local")
@WebMvcTest(CouponPolicyController.class)
class CouponPolicyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponPolicyServiceImpl couponPolicyService;

    @BeforeEach
    void setUp() {
        couponPolicyService.saveCouponPolicy(new CouponPolicyRequestDto(
            new BigDecimal("1.0"),
            20000L,
            2000L,
            30
        ));
    }

    @Test
    @DisplayName("쿠폰 정책 생성 API - POST")
    void createCouponPolicy() throws Exception {
        // Given
        String addCouponPolicy = "{\"discountPrice\":0.5,"
            + "\"condition\":20000,"
            + "\"maxPrice\":2000,"
            + "\"duration\":30}";

        CouponPolicyRequestDto couponPolicyRequestDto = new CouponPolicyRequestDto(
            new BigDecimal("0.5"),
            20000L,
            2000L,
            30
        );

        // When
        mockMvc.perform(post("/api/shop/admin/coupons/policy")
            .contentType(MediaType.APPLICATION_JSON)
            .content(addCouponPolicy))
            .andExpect(status().isCreated());

        // Then
        verify(couponPolicyService, times(1)).saveCouponPolicy(couponPolicyRequestDto);
    }

    @Test
    @DisplayName("쿠폰 정책 수정 API - PATCH")
    void updateCouponPolicy() throws Exception {
        // Given
        String updateCouponPolicy = "{\"discountPrice\":10,"
            + "\"condition\":20000,"
            + "\"maxPrice\":2000,"
            + "\"duration\":30}";

        CouponPolicyRequestDto couponPolicyRequestDto = new CouponPolicyRequestDto(
            new BigDecimal("10"),
            20000L,
            2000L,
            30
        );

        // When
        mockMvc.perform(patch("/api/shop/admin/coupons/policy/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateCouponPolicy))
            .andExpect(status().isOk());

        // Then
        verify(couponPolicyService, times(1)).updateCouponPolicy(1L, couponPolicyRequestDto);
    }

    @Test
    @DisplayName("특정 쿠폰 정책 조회 API - GET")
    void findCouponPolicyById() throws Exception {
        // Given
        Long couponPolicyId = 1L;

        when(couponPolicyService.findByCouponPolicyId(couponPolicyId)).thenReturn(new CouponPolicyResponseDto(
            new BigDecimal("0.5"),
            20000L,
            2000L,
            30
        ));

        // When & Then
        mockMvc.perform(get("/api/shop/admin/coupons/policy/{couponPolicyId}", couponPolicyId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.discountPrice").value(new BigDecimal("0.5")))
            .andExpect(jsonPath("$.condition").value(20000L))
            .andExpect(jsonPath("$.maxPrice").value(2000L))
            .andExpect(jsonPath("$.duration").value(30));
    }

    @Test
    @DisplayName("모든 쿠폰 정책 조회 API - GET")
    void findAllCouponPolices() throws Exception {
        // Given
        couponPolicyService.saveCouponPolicy(
            new CouponPolicyRequestDto(new BigDecimal("10"), 50000L, 2000L, 30)
        );

        List<CouponPolicyResponseDto> couponPolicyResponseDtoList = List.of(
            new CouponPolicyResponseDto(new BigDecimal("0.5"), 20000L, 2000L, 30),
            new CouponPolicyResponseDto(new BigDecimal("10"), 50000L, 2000L, 30)
        );

        when(couponPolicyService.findByAllCouponPolicies()).thenReturn(couponPolicyResponseDtoList);

        // When & Then
        mockMvc.perform(get("/api/shop/admin/coupons/policy"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].discountPrice").value(new BigDecimal("0.5")))
            .andExpect(jsonPath("$[0].condition").value(20000L))
            .andExpect(jsonPath("$[0].maxPrice").value(2000L))
            .andExpect(jsonPath("$[0].duration").value(30))
            .andExpect(jsonPath("$[1].discountPrice").value(new BigDecimal("10")))
            .andExpect(jsonPath("$[1].condition").value(50000L))
            .andExpect(jsonPath("$[1].maxPrice").value(2000L))
            .andExpect(jsonPath("$[1].duration").value(30));
    }
}
