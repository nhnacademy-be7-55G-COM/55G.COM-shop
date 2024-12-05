package shop.s5g.shop.controller.coupon.template;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.controller.coupon.CouponTemplateController;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.coupon.template.impl.CouponTemplateServiceImpl;

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
class CouponTemplateExceptionTest {

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
            + "\"couponDescription\": null}";

        // When
        mockMvc.perform(post("/api/shop/admin/coupons/template")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createTemplate))
            .andExpect(status().isBadRequest())
            .andDo(document("CouponTemplate-Create",
                requestFields(
                    fieldWithPath("couponPolicyId")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰 정책 ID"),
                    fieldWithPath("couponName")
                        .type(JsonFieldType.NULL)
                        .description("쿠폰 이름")
                        .attributes(key("constraints").value("Null, 에러 상황")),
                    fieldWithPath("couponDescription")
                        .type(JsonFieldType.NULL)
                        .description("쿠폰 설명")
                        .attributes(key("constraints").value("Null, 에러 상황"))
                )
            ));
    }

    @Test
    @DisplayName("쿠폰 템플릿 수정 API 에러 테스트")
    void updateCouponTemplateThrowsBadRequestException() throws Exception {
        // Given
        String updateTemplate = "{\"couponTemplateId\":1,"
            + "\"couponName\": null,"
            + "\"couponDescription\": null}";

        // When
        mockMvc.perform(post("/api/shop/admin/coupons/template/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateTemplate))
            .andExpect(status().isBadRequest())
            .andDo(document("CouponTemplate-Create",
                requestFields(
                    fieldWithPath("couponTemplateId")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰 템플릿 ID"),
                    fieldWithPath("couponName")
                        .type(JsonFieldType.NULL)
                        .description("쿠폰 이름")
                        .attributes(key("constraints").value("Null, error status")),
                    fieldWithPath("couponDescription")
                        .type(JsonFieldType.NULL)
                        .description("쿠폰 설명")
                        .attributes(key("constraints").value("Null, error status"))
                ),
                responseFields(
                    fieldWithPath("message")
                        .type(JsonFieldType.STRING)
                        .description("응답 메시지")
                )
            ));
    }

}
