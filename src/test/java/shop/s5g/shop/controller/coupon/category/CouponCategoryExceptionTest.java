package shop.s5g.shop.controller.coupon.category;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.snippet.Attributes.key;
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
import shop.s5g.shop.controller.coupon.CouponCategoryController;
import shop.s5g.shop.exception.BadRequestException;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.category.impl.CategoryServiceImpl;
import shop.s5g.shop.service.coupon.category.impl.CouponCategoryServiceImpl;

@AutoConfigureRestDocs
@ActiveProfiles("local")
@WebMvcTest(
    value = CouponCategoryController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import(TestSecurityConfig.class)
class CouponCategoryExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponCategoryServiceImpl couponCategoryService;

    @MockBean
    private CategoryServiceImpl categoryService;

    @Test
    @DisplayName("쿠폰 카테고리 유효성 실패 테스트")
    void createCouponCategoryInvalidExceptionTest() throws Exception {
        // Given
        String invalidRequest = "{\"couponTemplateId\":-10, \"categoryId\":-10}";

        // When & Then
        mockMvc.perform(post("/api/shop/admin/coupons/category")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest))
            .andExpect(status().isBadRequest())
            .andExpect(result ->
                assertInstanceOf(BadRequestException.class, result.getResolvedException()))
            .andDo(document("CouponCategory-Create-Bad-Request",
                requestFields(
                    fieldWithPath("couponTemplateId")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰 템플릿 ID")
                        .attributes(key("constraints").value("NotNull")),
                    fieldWithPath("categoryId")
                        .type(JsonFieldType.NUMBER)
                        .description("카테고리 ID")
                ),
                responseFields(
                    fieldWithPath("message")
                        .type(JsonFieldType.STRING)
                        .description("응답 메시지")
                )));
    }
}
