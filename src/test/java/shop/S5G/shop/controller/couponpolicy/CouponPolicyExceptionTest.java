package shop.S5G.shop.controller.couponpolicy;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import shop.S5G.shop.controller.coupon.CouponPolicyController;
import shop.S5G.shop.dto.couponpolicy.CouponPolicyRequestDto;
import shop.S5G.shop.service.coupon.impl.CouponPolicyServiceImpl;

@ActiveProfiles("local")
@WebMvcTest(CouponPolicyController.class)
public class CouponPolicyExceptionTest {

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
        mockMvc.perform(post("/api/admin/coupons/policy")
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
        mockMvc.perform(patch("/api/admin/coupons/policy/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateCouponPolicy))
            .andExpect(status().isBadRequest());
    }
}
