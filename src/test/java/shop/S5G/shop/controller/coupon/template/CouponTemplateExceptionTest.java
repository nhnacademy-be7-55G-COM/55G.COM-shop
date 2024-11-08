package shop.S5G.shop.controller.coupon.template;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import shop.S5G.shop.config.SecurityConfig;
import shop.S5G.shop.config.TestSecurityConfig;
import shop.S5G.shop.controller.coupon.CouponTemplateController;
import shop.S5G.shop.filter.JwtAuthenticationFilter;
import shop.S5G.shop.service.coupon.template.impl.CouponTemplateServiceImpl;

@AutoConfigureRestDocs
@ActiveProfiles("local")
@WebMvcTest(
    value = CouponTemplateController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import(TestSecurityConfig.class)
public class CouponTemplateExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponTemplateServiceImpl couponTemplateService;

    @Test
    @DisplayName("쿠폰 템플릿 생성 API 에러 테스트")
    void createCouponTemplateThrowBadRequestException() throws Exception {
        // Given
        String createTemplate = "{\"couponPolicyId\":1,"
            + "\"couponName\": null,"
            + "\"couponDescription\": \"이 쿠폰은 생일자들을 위한 쿠폰입니다.\"}";

        // When
        mockMvc.perform(post("/api/shop/admin/coupons/template")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createTemplate))
            .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("쿠폰 템플릿 수정 API 에러 테스트")
    void updateCouponTemplateThrowsBadRequestException() throws Exception {
        // Given
        String updateTemplate = "{\"couponPolicyId\":1,"
            + "\"couponName\": null,"
            + "\"couponDescription\": \"이 쿠폰은 생일자들을 위한 쿠폰입니다.\"}";

        // When
        mockMvc.perform(patch("/api/shop/admin/coupons/template/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateTemplate))
            .andExpect(status().isBadRequest());
    }

}
