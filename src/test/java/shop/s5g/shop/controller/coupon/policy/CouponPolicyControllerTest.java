package shop.s5g.shop.controller.coupon.policy;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.controller.coupon.CouponPolicyController;
import shop.s5g.shop.dto.coupon.policy.CouponPolicyRequestDto;
import shop.s5g.shop.dto.coupon.policy.CouponPolicyResponseDto;
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
class CouponPolicyControllerTest {

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
    @DisplayName("쿠폰 정책 생성 API - POST")
    void createCouponPolicy() throws Exception {
        // Given
        String addCouponPolicy = "{\"discountPrice\":0.5,"
            + "\"condition\":20000,"
            + "\"maxPrice\":2000,"
            + "\"duration\":30}";

        CouponPolicyRequestDto couponPolicyRequestDto = new CouponPolicyRequestDto(
            new BigDecimal("0.5"),
            20000L,
            2000L,
            30
        );

        // When
        mockMvc.perform(post("/api/shop/admin/coupons/policy")
            .contentType(MediaType.APPLICATION_JSON)
            .content(addCouponPolicy))
            .andExpect(status().isCreated())
            .andDo(document("couponPolicy-create",
                requestFields(
                    fieldWithPath("discountPrice")
                        .type(JsonFieldType.NUMBER)
                        .description("할인 비율 ( 예 : 0.5는 50% 할인 )")
                        .attributes(key("constraints").value("0.1 ~ 0.8 or 1000 이상")),
                    fieldWithPath("condition")
                        .type(JsonFieldType.NUMBER)
                        .description("할인이 적용될 최소 금액")
                        .attributes(key("constraints").value("10000 ~ 50000 사이")),
                    fieldWithPath("maxPrice")
                        .type(JsonFieldType.NUMBER)
                        .description("할인의 최대 금액 (최대 할인 금액)")
                        .attributes(key("constraints").value("null or 금액의 80%")),
                    fieldWithPath("duration")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰의 유효 기간(일 단위)")
                        .attributes(key("constraints").value("1 ~ 365 사이"))
                ),
                responseFields(
                    fieldWithPath("message")
                        .type(JsonFieldType.STRING)
                        .description("응답 메시지")
                )
            ));

        // Then
        verify(couponPolicyService, times(1)).saveCouponPolicy(couponPolicyRequestDto);
    }

    @Test
    @DisplayName("쿠폰 정책 수정 API - POST")
    void updateCouponPolicy() throws Exception {
        // Given
        String updateCouponPolicy = "{\"discountPrice\":10,"
            + "\"condition\":20000,"
            + "\"maxPrice\":2000,"
            + "\"duration\":30}";

        CouponPolicyRequestDto couponPolicyRequestDto = new CouponPolicyRequestDto(
            new BigDecimal("10"),
            20000L,
            2000L,
            30
        );

        // When
        mockMvc.perform(post("/api/shop/admin/coupons/policy/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateCouponPolicy))
            .andExpect(status().isOk())
            .andDo(document("couponPolicy-create",
                requestFields(
                    fieldWithPath("discountPrice")
                        .type(JsonFieldType.NUMBER)
                        .description("할인 비율 ( 예 : 0.5는 50% 할인 )")
                        .attributes(key("constraints").value("NotNull, 0.1 ~ 0.8 or 1000 이상")),
                    fieldWithPath("condition")
                        .type(JsonFieldType.NUMBER)
                        .description("할인이 적용될 최소 금액")
                        .attributes(key("constraints").value("NotNull, 10000 ~ 50000 사이")),
                    fieldWithPath("maxPrice")
                        .type(JsonFieldType.NUMBER)
                        .description("할인의 최대 금액 (최대 할인 금액)")
                        .attributes(key("constraints").value("null, 금액의 80%")),
                    fieldWithPath("duration")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰의 유효 기간(일 단위)")
                        .attributes(key("constraints").value("NotNull, 1 ~ 365 사이"))
                ),
                responseFields(
                    fieldWithPath("message")
                        .type(JsonFieldType.STRING)
                        .description("응답 메시지")
                )
            ));

        // Then
        verify(couponPolicyService, times(1)).updateCouponPolicy(1L, couponPolicyRequestDto);
    }

    @Test
    @DisplayName("특정 쿠폰 정책 조회 API - GET")
    void findCouponPolicyById() throws Exception {
        // Given
        Long couponPolicyId = 1L;

        when(couponPolicyService.getByCouponPolicyId(couponPolicyId)).thenReturn(new CouponPolicyResponseDto(
            couponPolicyId,
            new BigDecimal("0.5"),
            20000L,
            2000L,
            30
        ));

        // When & Then
        mockMvc.perform(get("/api/shop/admin/coupons/policy/{couponPolicyId}", couponPolicyId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.discountPrice").value(new BigDecimal("0.5")))
            .andExpect(jsonPath("$.condition").value(20000L))
            .andExpect(jsonPath("$.maxPrice").value(2000L))
            .andExpect(jsonPath("$.duration").value(30))
            .andDo(document("couponPolicy-get",
                pathParameters(
                    parameterWithName("couponPolicyId")
                        .description("조회할 쿠폰 정책 ID")
                ),
                responseFields(
                    fieldWithPath("couponPolicyId")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰 정책 ID")
                        .attributes(key("constraints").value("NotNull, Long")),
                    fieldWithPath("discountPrice")
                        .type(JsonFieldType.NUMBER)
                        .description("할인 비율 (예 : 0.5 는 50% 할인)")
                        .attributes(key("constraints").value("NotNull, 0.1 ~ 0.8")),
                    fieldWithPath("condition")
                        .type(JsonFieldType.NUMBER)
                        .description("할인 받기 위한 최소 금액")
                        .attributes(key("constraints").value("NotNull, 10000 ~ 50000 사이")),
                    fieldWithPath("maxPrice")
                        .type(JsonFieldType.NUMBER)
                        .description("할인 받을 수 있는 최대 금액")
                        .attributes(key("constraints").value("Null, 금액의 80% 까지")),
                    fieldWithPath("duration")
                        .type(JsonFieldType.NUMBER)
                        .description("정책의 유효 기간")
                        .attributes(key("constraints").value("NotNull, 1 ~ 365 사이"))
                )
            ));
    }

    @Test
    @DisplayName("모든 쿠폰 정책 조회 성공")
    void findAllCouponPolicy() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        CouponPolicyResponseDto couponPolicyResponseDto = new CouponPolicyResponseDto(
            1L,
            new BigDecimal("0.5"),
            20000L,
            2000L,
            30
        );
        Page<CouponPolicyResponseDto> couponPolicyPage = new PageImpl<>(List.of(couponPolicyResponseDto), pageable, 1);

        when(couponPolicyService.getAllCouponPolices(pageable)).thenReturn(couponPolicyPage);

        // When & Then
        mockMvc.perform(get("/api/shop/admin/coupons/policy")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].discountPrice").value(new BigDecimal("0.5")))
            .andExpect(jsonPath("$.content[0].condition").value(20000L))
            .andExpect(jsonPath("$.content[0].maxPrice").value(2000L))
            .andExpect(jsonPath("$.content[0].duration").value(30))
            .andDo(document("couponPolicy-getAll",
                responseFields(
                    fieldWithPath("content[].couponPolicyId")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰 정책 ID"),
                    fieldWithPath("content[].discountPrice")
                        .type(JsonFieldType.NUMBER)
                        .description("할인 비율 (예: 0.5는 50% 할인)"),
                    fieldWithPath("content[].condition")
                        .type(JsonFieldType.NUMBER)
                        .description("할인이 적용될 최소 결제 금액"),
                    fieldWithPath("content[].maxPrice")
                        .type(JsonFieldType.NUMBER)
                        .description("할인의 최대 금액 (최대 할인 가능 금액)"),
                    fieldWithPath("content[].duration")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰의 유효 기간 (일 단위)"),
                    fieldWithPath("totalPage")
                        .type(JsonFieldType.NUMBER)
                        .description("전체 페이지 수"),
                    fieldWithPath("pageSize")
                        .type(JsonFieldType.NUMBER)
                        .description("요청한 페이지 크기"),
                    fieldWithPath("totalElements")
                        .type(JsonFieldType.NUMBER)
                        .description("전체 항목 수")
                )
            ));
    }
}
