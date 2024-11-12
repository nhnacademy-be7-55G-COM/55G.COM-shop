package shop.S5G.shop.controller.coupon.coupon;

import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.controller.coupon.CouponController;
import shop.s5g.shop.filter.JwtAuthenticationFilter;

@AutoConfigureRestDocs
@ActiveProfiles("local")
@WebMvcTest(value = CouponController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    ))
@Import(TestSecurityConfig.class)
class CouponControllerTest {

    // TODO - Youngho : 쿠폰 테스트 코드 작성 해야함 (repo, service, controller)
}
