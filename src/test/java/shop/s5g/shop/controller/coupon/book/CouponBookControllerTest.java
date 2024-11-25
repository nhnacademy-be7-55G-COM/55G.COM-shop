package shop.s5g.shop.controller.coupon.book;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
import shop.s5g.shop.controller.coupon.CouponBookController;
import shop.s5g.shop.dto.coupon.book.CouponBookRequestDto;
import shop.s5g.shop.dto.coupon.template.CouponTemplateRequestDto;
import shop.s5g.shop.entity.coupon.CouponPolicy;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.coupon.book.impl.CouponBookServiceImpl;
import shop.s5g.shop.service.coupon.template.impl.CouponTemplateServiceImpl;

@AutoConfigureRestDocs
@ActiveProfiles("local")
@WebMvcTest(
    value = CouponBookController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import(TestSecurityConfig.class)
class CouponBookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponBookServiceImpl couponBookService;
    @MockBean
    private CouponTemplateServiceImpl couponTemplateService;

    private CouponPolicy couponPolicy;

    @BeforeEach
    void setUp() {

        couponPolicy = CouponPolicy.builder()
            .discountPrice(new BigDecimal("0.1"))
            .maxPrice(50000L)
            .condition(2000L)
            .duration(30)
            .build();

        couponTemplateService.createCouponTemplate(
            new CouponTemplateRequestDto(
                1L,
                "할인 쿠폰",
                "이 쿠폰은 회원 가입한 모든 고객에게 전달되는 쿠폰입니다."
            )
        );

    }

    @Test
    @DisplayName("쿠폰 책 생성 - API")
    void createCouponBook() throws Exception {
        // Given
        String jsonTemplate = "{\"couponTemplateId\":1,"
            + "\"bookId\": 1}";

        CouponBookRequestDto couponBookRequestDto = new CouponBookRequestDto(
            1L,
            1L
        );

        // When
        mockMvc.perform(post("/api/shop/admin/coupons/book")
            .contentType(MediaType.APPLICATION_JSON)
            .content(jsonTemplate))
            .andExpect(status().isCreated());

        // Then
        verify(couponBookService, times(1)).createCouponBook(couponBookRequestDto);
    }
}
