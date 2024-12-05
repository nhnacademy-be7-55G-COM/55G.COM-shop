package shop.s5g.shop.controller.coupon.coupon;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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
import shop.s5g.shop.config.RedisConfig;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.controller.coupon.CouponController;
import shop.s5g.shop.exception.coupon.CouponBadRequestException;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.coupon.coupon.impl.CouponServiceImpl;
import shop.s5g.shop.service.coupon.coupon.impl.RedisCouponServiceImpl;

@AutoConfigureRestDocs
@ActiveProfiles("local")
@WebMvcTest(value = CouponController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    ))
@Import({TestSecurityConfig.class, RedisConfig.class})
class CouponExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponServiceImpl couponService;

    @MockBean
    private RedisCouponServiceImpl redisCouponService;

    @Test
    @DisplayName("쿠폰 생성 요청 시 유효성 검증 실패 테스트")
    void createCouponWithInvalidData() throws Exception {
        // Given: 유효성 검증 실패를 유도하는 잘못된 데이터
        String invalidRequest = "{\"quantity\": -10, \"couponTemplateId\": null}";

        // When & Then: 요청 실행 및 검증
        mockMvc.perform(post("/api/shop/admin/coupons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest))
            .andExpect(status().isBadRequest())
            .andExpect(result ->
                assertInstanceOf(CouponBadRequestException.class, result.getResolvedException()))
            .andDo(print());
    }
}
