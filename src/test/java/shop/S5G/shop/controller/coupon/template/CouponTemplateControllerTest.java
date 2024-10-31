package shop.S5G.shop.controller.coupon.template;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
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
import shop.S5G.shop.controller.coupon.CouponTemplateController;
import shop.S5G.shop.dto.coupon.template.CouponTemplateRequestDto;
import shop.S5G.shop.dto.coupon.template.CouponTemplateResponseDto;
import shop.S5G.shop.entity.coupon.CouponPolicy;
import shop.S5G.shop.service.coupon.template.impl.CouponTemplateServiceImpl;

@AutoConfigureRestDocs
@ActiveProfiles("local")
@WebMvcTest(CouponTemplateController.class)
class CouponTemplateControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
    @DisplayName("쿠폰 템플릿 생성 API")
    void createTemplate() throws Exception {
        // Given
        String jsonTemplate = "{\"couponPolicyId\":1,"
            + "\"couponName\": \"생일 쿠폰\","
            + "\"couponDescription\": \"이 쿠폰은 생일자들을 위한 쿠폰입니다.\"}";

        CouponTemplateRequestDto couponTemplateRequestDto = new CouponTemplateRequestDto(
            1L,
            "생일 쿠폰",
            "이 쿠폰은 생일자들을 위한 쿠폰입니다."
        );

        // When
        mockMvc.perform(post("/api/shop/admin/coupons/template")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonTemplate))
            .andExpect(status().isCreated());

        // Then
        verify(couponTemplateService, times(1)).createCouponTemplate(couponTemplateRequestDto);
    }

    @Test
    @DisplayName("쿠폰 템플릿 조회 API")
    void findTemplate() throws Exception {
        // Given
        Long couponTemplateId = 1L;

        when(couponTemplateService.findCouponTemplate(couponTemplateId)).thenReturn(
            new CouponTemplateResponseDto(
                couponPolicy,
                "할인 쿠폰",
                "이 쿠폰은 회원 가입한 모든 고객에게 전달되는 쿠폰입니다."
            )
        );

        // When & Then
        mockMvc.perform(get("/api/shop/admin/coupons/template/{couponTemplateId}", couponTemplateId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.couponPolicy.maxPrice").value(couponPolicy.getMaxPrice()))
            .andExpect(jsonPath("$.couponPolicy.condition").value(couponPolicy.getCondition()))
            .andExpect(jsonPath("$.couponPolicy.discountPrice").value(couponPolicy.getDiscountPrice()))
            .andExpect(jsonPath("$.couponName").value("할인 쿠폰"))
            .andExpect(jsonPath("$.couponDescription").value("이 쿠폰은 회원 가입한 모든 고객에게 전달되는 쿠폰입니다."));
    }

    @Test
    @DisplayName("쿠폰 템플릿 수정 API")
    void updateTemplate() throws Exception {
        // Given
        Long templateId = 1L;

        String updateTemplate = "{\"couponPolicyId\":1,"
            + "\"couponName\": \"생일 쿠폰\","
            + "\"couponDescription\": \"이 쿠폰은 생일자들을 위한 쿠폰입니다.\"}";

        CouponTemplateRequestDto couponTemplateRequestDto = new CouponTemplateRequestDto(
            1L,
            "생일 쿠폰",
            "이 쿠폰은 생일자들을 위한 쿠폰입니다."
        );

        // When
        mockMvc.perform(patch("/api/shop/admin/coupons/template/{couponTemplateId}", templateId)
            .contentType(MediaType.APPLICATION_JSON)
            .content(updateTemplate))
            .andExpect(status().isOk());

        // Then
        verify(couponTemplateService, times(1)).updateCouponTemplate(1L, couponTemplateRequestDto);
    }

    @Test
    @DisplayName("쿠폰 템플릿 삭제 API")
    void deleteTemplate() throws Exception {
        // Given & When
        Long templateId = 1L;

        mockMvc.perform(delete("/api/shop/admin/coupons/template/{couponTemplateId}", templateId))
            .andExpect(status().is2xxSuccessful());
    }
}
