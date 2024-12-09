package shop.s5g.shop.controller.coupon.coupon;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
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
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import shop.s5g.shop.config.RedisConfig;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.controller.coupon.CouponController;
import shop.s5g.shop.dto.coupon.coupon.AvailableCouponResponseDto;
import shop.s5g.shop.dto.coupon.coupon.CouponRequestDto;
import shop.s5g.shop.dto.coupon.coupon.CouponResponseDto;
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
class CouponControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CouponServiceImpl couponService;

    @MockBean
    private RedisCouponServiceImpl redisCouponService;

    @Test
    @DisplayName("쿠폰 생성 성공")
    void createCoupon() throws Exception {
        // Given
        CouponRequestDto couponRequestDto = new CouponRequestDto(
            100,
            1L
        );

        String addCoupon = objectMapper.writeValueAsString(couponRequestDto);

        // When
        mockMvc.perform(post("/api/shop/admin/coupons")
            .contentType(MediaType.APPLICATION_JSON)
            .content(addCoupon))
            .andDo(print())
            .andExpect(status().isCreated())
            .andDo(document("Coupon-Create",
                requestFields(
                    fieldWithPath("quantity")
                        .type(JsonFieldType.NUMBER)
                        .description("발급할 쿠폰 수량")
                        .attributes(key("constraints").value("Not Null, up to 1")),
                    fieldWithPath("couponTemplateId")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰 템플릿 ID")
                        .attributes(key("constraints").value("Not Null, up to 1 Long"))
                ),
                responseFields(
                    fieldWithPath("message")
                        .type(JsonFieldType.STRING)
                        .description("응답 메시지")
                )
            ));

        // Then
        verify(redisCouponService, times(1)).createCoupon(couponRequestDto);
    }

    @Test
    @DisplayName("발급 가능한 쿠폰 리스트 성공")
    void getAvailableCouponsTest() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        AvailableCouponResponseDto availableCoupons = new AvailableCouponResponseDto(
            1L,
            1L,
            "테스트 쿠폰",
            20000L,
            null,
            new BigDecimal("0.5"),
            20L
        );
        Page<AvailableCouponResponseDto> couponPage = new PageImpl<>(List.of(availableCoupons), pageable, 1);

        when(couponService.getAvailableCoupons(pageable)).thenReturn(couponPage);

        // When
        mockMvc.perform(get("/api/shop/admin/coupons/available")
                .param("page", "0")
                .param("size", "10")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].couponId").value(availableCoupons.couponId()))
            .andExpect(jsonPath("$.content[0].couponTemplateId").value(availableCoupons.couponTemplateId()))
            .andExpect(jsonPath("$.content[0].couponName").value(availableCoupons.couponName()))
            .andDo(document("Coupon-GetAvailableList",
                responseFields(
                    fieldWithPath("content[].couponId")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰 ID"),
                    fieldWithPath("content[].couponTemplateId")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰 템플릿 ID"),
                    fieldWithPath("content[].couponName")
                        .type(JsonFieldType.STRING)
                        .description("쿠폰 이름"),
                    fieldWithPath("content[].condition")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰 사용 조건 금액"),
                    fieldWithPath("content[].maxPrice")
                        .type(JsonFieldType.NULL)
                        .description("쿠폰 사용 시 최대 할인 금액")
                        .attributes(key("constraints").value("Nullable, 조건의 80% 이하까지")),
                    fieldWithPath("content[].discountPrice")
                        .type(JsonFieldType.NUMBER)
                        .description("할인율"),
                    fieldWithPath("content[].count")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰 남은 수량"),
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
            )
        );

        // Then
        verify(couponService, times(1)).getAvailableCoupons(pageable);
    }

    @Test
    @DisplayName("쿠폰 삭제 성공")
    void deleteCouponTest() throws Exception {
        // Given & When
        Long couponId = 1L;

        mockMvc.perform(delete("/api/shop/admin/coupons/{couponId}", couponId))
            .andExpect(status().is2xxSuccessful())
            .andDo(document("Coupon-Delete",
                pathParameters(
                    parameterWithName("couponId")
                        .description("삭제할 쿠폰 ID")
                ),
                responseFields(
                    fieldWithPath("message")
                        .type(JsonFieldType.STRING)
                        .description("응답 메시지")
                )
            ));

        // Then
        verify(couponService, times(1)).deleteCoupon(couponId);
    }

    @Test
    @DisplayName("발급한 모든 쿠폰 리스트 성공")
    void getAllCouponsTest() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        CouponResponseDto couponResponseDto = new CouponResponseDto(
            1L,
            1L,
            "aAxXzZcCtT"
        );

        Page<CouponResponseDto> couponList = new PageImpl<>(List.of(couponResponseDto), pageable, 1);

        when(couponService.getAllCouponList(pageable)).thenReturn(couponList);

        // When
        mockMvc.perform(get("/api/shop/admin/coupons")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].couponId").value(couponResponseDto.couponId()))
            .andExpect(jsonPath("$.content[0].couponTemplateId").value(couponResponseDto.couponTemplateId()))
            .andExpect(jsonPath("$.content[0].couponCode").value(couponResponseDto.couponCode()))
            .andDo(document("Coupon-Get-All-CouponList",
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
        verify(couponService, times(1)).getAllCouponList(pageable);
    }

    @Test
    @DisplayName("특정 쿠폰 조회 성공")
    void getCouponTest() throws Exception {
        // Given
        Long couponId = 1L;

        CouponResponseDto couponResponseDto = new CouponResponseDto(
            1L,
            1L,
            "aAxXzZcCtT"
        );

        when(couponService.getCoupon(couponId)).thenReturn(couponResponseDto);

        // When
        mockMvc.perform(get("/api/shop/admin/coupons/{couponId}", couponId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.couponId").value(couponResponseDto.couponId()))
            .andExpect(jsonPath("$.couponTemplateId").value(couponResponseDto.couponTemplateId()))
            .andExpect(jsonPath("$.couponCode").value(couponResponseDto.couponCode()))
            .andDo(document("Coupon-Get-Coupon",
                pathParameters(
                    parameterWithName("couponId")
                        .description("조회할 쿠폰 ID")
                ),
                responseFields(
                    fieldWithPath("couponId")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰 ID"),
                    fieldWithPath("couponTemplateId")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰 템플릿 ID"),
                    fieldWithPath("couponCode")
                        .type(JsonFieldType.STRING)
                        .description("쿠폰 코드")
                        .attributes(key("constraints").value("NotBlank, 고유한 쿠폰 코드"))
                )
            ));

        // Then
        verify(couponService, times(1)).getCoupon(couponId);
    }
}
