package shop.s5g.shop.controller.coupon.policy;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.controller.coupon.CouponPolicyController;
import shop.s5g.shop.dto.coupon.policy.CouponPolicyRequestDto;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.coupon.policy.impl.CouponPolicyServiceImpl;

@AutoConfigureRestDocs
@ActiveProfiles("local")
@WebMvcTest(
    value = CouponPolicyController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import(TestSecurityConfig.class)
class CouponPolicyExceptionTest {

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
    @DisplayName("쿠폰 정책 생성 API 에러 테스트")
    void createCouponPolicyThrowBadRequestException() throws Exception {
        // Given
        String addCouponPolicy = "{\"discountPrice\":0.5,"
            + "\"condition\":null,"
            + "\"maxPrice\":null,"
            + "\"duration\":30}";

        // When
        mockMvc.perform(post("/api/shop/admin/coupons/policy")
                .contentType(MediaType.APPLICATION_JSON)
                .content(addCouponPolicy))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("쿠폰 정책 수정 API 에러 테스트")
    void updateCouponPolicyThrowsBadRequestException() throws Exception {
        // Given
        String updateCouponPolicy = "{\"discountPrice\":0.5,"
            + "\"condition\":null,"
            + "\"maxPrice\":null,"
            + "\"duration\":30}";

        // When
        mockMvc.perform(patch("/api/shop/admin/coupons/policy/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateCouponPolicy))
            .andExpect(status().isBadRequest());
    }
}
