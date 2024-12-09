package shop.s5g.shop.controller.coupon.user;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
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
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import shop.s5g.shop.config.RedisConfig;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.controller.coupon.UserCouponController;
import shop.s5g.shop.dto.coupon.user.InValidUsedCouponResponseDto;
import shop.s5g.shop.dto.coupon.user.ValidUserCouponResponseDto;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.coupon.user.impl.UserCouponServiceImpl;

@AutoConfigureRestDocs
@ActiveProfiles("local")
@WebMvcTest(
    value = UserCouponController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import({TestSecurityConfig.class, RedisConfig.class})
class UserCouponControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserCouponServiceImpl userCouponService;

    @Test
    @DisplayName("특정 유저의 사용 가능한 모든 쿠폰 조회")
    void getUserCouponTest() throws Exception {
        // Given
        Long memberId = 1L;

        Pageable pageable = PageRequest.of(0, 10);

        LocalDateTime now = LocalDateTime.now();

        List<ValidUserCouponResponseDto> userCouponList = List.of(
            new ValidUserCouponResponseDto(1L, 1L, "aAzZxXcCvV", "테스트 쿠폰", "테스트 쿠폰입니다.", 50000L, new BigDecimal(3000), null, now, now.plusDays(30)),
            new ValidUserCouponResponseDto(2L, 2L, "aAzZxXcCqQ", "Test Coupon", "This is TestCoupon", 30000L, new BigDecimal(3000), null, now, now.plusDays(15))
        );

        Page<ValidUserCouponResponseDto> userCouponPage = new PageImpl<>(userCouponList, pageable, userCouponList.size());

        when(userCouponService.getUnusedCoupons(memberId, pageable)).thenReturn(userCouponPage);

        // When
        mvc.perform(get("/api/shop/member/coupons/{memberId}", memberId)
            .param("page","0")
            .param("size","10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].couponCode").value(userCouponList.getFirst().couponCode()))
            .andExpect(jsonPath("$.content[1].couponCode").value(userCouponList.getLast().couponCode()))
            .andDo(document("UserCoupon-Get-Coupon-List",
                pathParameters(
                    parameterWithName("memberId")
                        .description("멤버 ID")
                ),
                responseFields(
                    fieldWithPath("content[].couponId")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰 ID"),
                    fieldWithPath("content[].couponTemplateId")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰 템플릿 ID"),
                    fieldWithPath("content[].couponCode")
                        .type(JsonFieldType.STRING)
                        .description("쿠폰 코드"),
                    fieldWithPath("content[].couponName")
                        .type(JsonFieldType.STRING)
                        .description("쿠폰 이름"),
                    fieldWithPath("content[].couponDescription")
                        .type(JsonFieldType.STRING)
                        .description("쿠폰 설명"),
                    fieldWithPath("content[].condition")
                        .type(JsonFieldType.NUMBER)
                        .description("할인 조건"),
                    fieldWithPath("content[].discountPrice")
                        .type(JsonFieldType.NUMBER)
                        .description("할인률"),
                    fieldWithPath("content[].maxPrice")
                        .type(JsonFieldType.VARIES)
                        .optional()
                        .description("최대 할인 가격"),
                    fieldWithPath("content[].createdAt")
                        .type(JsonFieldType.STRING)
                        .description("쿠폰 발급 날짜"),
                    fieldWithPath("content[].expiredAt")
                        .type(JsonFieldType.STRING)
                        .description("쿠폰 만료 날짜"),
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

        // Then
        verify(userCouponService, times(1)).getUnusedCoupons(memberId, pageable);
    }

    @Test
    @DisplayName("특정 유저가 사용한 모든 쿠폰 조회")
    void getUsedCouponTest() throws Exception {
        // Given
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        List<InValidUsedCouponResponseDto> usedCouponList = List.of(
            new InValidUsedCouponResponseDto(1L, "aAzZxXcCvV", "테스트 쿠폰", "테스트 쿠폰입니다.", 30000L, new BigDecimal(3000), null),
            new InValidUsedCouponResponseDto(2L, "aAzZxXcCdD", "사용한 쿠폰", "사용한 쿠폰입니다.", 30000L, new BigDecimal("0.5"), 2000L)
        );

        Page<InValidUsedCouponResponseDto> usedCouponPage = new PageImpl<>(usedCouponList, pageable, usedCouponList.size());

        when(userCouponService.getUsedCoupons(memberId, pageable)).thenReturn(usedCouponPage);

        // When
        mvc.perform(get("/api/shop/member/coupons/used/{memberId}", memberId)
            .param("page", "0")
            .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].couponCode").value(usedCouponList.getFirst().couponCode()))
            .andExpect(jsonPath("$.content[1].couponCode").value(usedCouponList.getLast().couponCode()))
            .andDo(document("UserCoupon-Get-Used-CouponList",
                pathParameters(
                    parameterWithName("memberId")
                        .description("멤버 ID")
                ),
                responseFields(
                    fieldWithPath("content[].couponId")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰 ID"),
                    fieldWithPath("content[].couponCode")
                        .type(JsonFieldType.STRING)
                        .description("쿠폰 코드"),
                    fieldWithPath("content[].couponName")
                        .type(JsonFieldType.STRING)
                        .description("쿠폰 이름"),
                    fieldWithPath("content[].couponDescription")
                        .type(JsonFieldType.STRING)
                        .description("쿠폰 설명"),
                    fieldWithPath("content[].condition")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰 사용 조건 가격"),
                    fieldWithPath("content[].discountPrice")
                        .type(JsonFieldType.NUMBER)
                        .description("할인율"),
                    fieldWithPath("content[].maxPrice")
                        .type(JsonFieldType.VARIES)
                        .optional()
                        .description("최대 할인 가격"),
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

        // Then
        verify(userCouponService, times(1)).getUsedCoupons(memberId, pageable);
    }

    @Test
    @DisplayName("사용하지는 않았으나 만료된 쿠폰 조회")
    void getExpiredCouponTest() throws Exception {
        // Given
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        List<InValidUsedCouponResponseDto> expiredCouponList = List.of(
            new InValidUsedCouponResponseDto(1L, "aAzZxXcCvV", "테스트 쿠폰", "테스트 쿠폰입니다.", 30000L, new BigDecimal(3000), null),
            new InValidUsedCouponResponseDto(2L, "aAzZxXcCdD", "사용한 쿠폰", "사용한 쿠폰입니다.", 30000L, new BigDecimal("0.5"), 2000L)
        );

        Page<InValidUsedCouponResponseDto> invalidCouponPage = new PageImpl<>(expiredCouponList, pageable, expiredCouponList.size());

        when(userCouponService.getExpiredCoupons(memberId, pageable)).thenReturn(invalidCouponPage);

        // When
        mvc.perform(get("/api/shop/member/coupons/expired/{memberId}", memberId)
            .param("page", "0")
            .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].couponCode").value(expiredCouponList.getFirst().couponCode()))
            .andExpect(jsonPath("$.content[1].couponCode").value(expiredCouponList.getLast().couponCode()))
            .andDo(document("UserCoupon-Get-Expired-CouponList",
                pathParameters(
                    parameterWithName("memberId")
                        .description("멤버 ID")
                ),
                responseFields(
                    fieldWithPath("content[].couponId")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰 ID"),
                    fieldWithPath("content[].couponCode")
                        .type(JsonFieldType.STRING)
                        .description("쿠폰 코드"),
                    fieldWithPath("content[].couponName")
                        .type(JsonFieldType.STRING)
                        .description("쿠폰 이름"),
                    fieldWithPath("content[].couponDescription")
                        .type(JsonFieldType.STRING)
                        .description("쿠폰 설명"),
                    fieldWithPath("content[].condition")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰 사용 조건 가격"),
                    fieldWithPath("content[].discountPrice")
                        .type(JsonFieldType.NUMBER)
                        .description("할인율"),
                    fieldWithPath("content[].maxPrice")
                        .type(JsonFieldType.VARIES)
                        .optional()
                        .description("최대 할인 가격"),
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

        // Then
        verify(userCouponService, times(1)).getExpiredCoupons(memberId, pageable);
    }

    @Test
    @DisplayName("사용했거나, 기간이 만료된 쿠폰 조회")
    void getInvalidCouponListTest() throws Exception {
        // Given
        Long memberId = 1L;
        Pageable pageable = PageRequest.of(0, 10);

        List<InValidUsedCouponResponseDto> invalidCouponList = List.of(
            new InValidUsedCouponResponseDto(1L, "aAzZxXcCvV", "테스트 쿠폰", "테스트 쿠폰입니다.", 30000L, new BigDecimal(3000), null),
            new InValidUsedCouponResponseDto(2L, "aAzZxXcCdD", "사용한 쿠폰", "사용한 쿠폰입니다.", 30000L, new BigDecimal("0.5"), 2000L)
        );

        Page<InValidUsedCouponResponseDto> invalidPage = new PageImpl<>(invalidCouponList, pageable, invalidCouponList.size());

        when(userCouponService.getInValidCoupons(memberId, pageable)).thenReturn(invalidPage);

        // When
        mvc.perform(get("/api/shop/member/coupons/invalid/{memberId}", memberId)
            .param("page", "0")
            .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].couponCode").value(invalidCouponList.getFirst().couponCode()))
            .andExpect(jsonPath("$.content[1].couponCode").value(invalidCouponList.getLast().couponCode()))
            .andDo(document("UserCoupon-Get-Invalid-CouponList",
                pathParameters(
                    parameterWithName("memberId")
                        .description("멤버 ID")
                ),
                responseFields(
                    fieldWithPath("content[].couponId")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰 ID"),
                    fieldWithPath("content[].couponCode")
                        .type(JsonFieldType.STRING)
                        .description("쿠폰 코드"),
                    fieldWithPath("content[].couponName")
                        .type(JsonFieldType.STRING)
                        .description("쿠폰 이름"),
                    fieldWithPath("content[].couponDescription")
                        .type(JsonFieldType.STRING)
                        .description("쿠폰 설명"),
                    fieldWithPath("content[].condition")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰 사용 조건 가격"),
                    fieldWithPath("content[].discountPrice")
                        .type(JsonFieldType.NUMBER)
                        .description("할인율"),
                    fieldWithPath("content[].maxPrice")
                        .type(JsonFieldType.VARIES)
                        .optional()
                        .description("최대 할인 가격"),
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

        // Then
        verify(userCouponService, times(1)).getInValidCoupons(memberId, pageable);
    }
}
