package shop.s5g.shop.controller.coupon.coupon;

import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import shop.s5g.shop.controller.coupon.CouponController;

@AutoConfigureRestDocs
@ActiveProfiles("local")
@WebMvcTest(CouponController.class)
class CouponControllerTest {

    // TODO - Youngho : 쿠폰 테스트 코드 작성 해야함 (repo, service, controller)
}
