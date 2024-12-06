package shop.s5g.shop.controller.coupon.category;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.fasterxml.jackson.databind.ObjectMapper;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.controller.coupon.CouponCategoryController;
import shop.s5g.shop.dto.category.CategoryResponseDto;
import shop.s5g.shop.dto.coupon.category.CouponCategoryDetailsForCategoryDto;
import shop.s5g.shop.dto.coupon.category.CouponCategoryRequestDto;
import shop.s5g.shop.dto.coupon.category.CouponCategoryResponseDto;
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
class CouponCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CouponCategoryServiceImpl couponCategoryService;

    @MockBean
    private CategoryServiceImpl categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("쿠폰 카테고리 생성 - API")
    void createCouponCategoryTest() throws Exception {
        // Given
        CouponCategoryRequestDto create = new CouponCategoryRequestDto(
            1L, 1L
        );

        String json = objectMapper.writeValueAsString(create);

        // When
        mockMvc.perform(post("/api/shop/admin/coupons/category")
            .contentType(MediaType.APPLICATION_JSON)
            .content(json))
            .andExpect(status().isCreated())
            .andDo(document("CouponCategory-Create",
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
                )
            ));

        // Then
        verify(couponCategoryService, times(1)).createCouponCategory(create);
    }

    @Test
    @DisplayName("쿠폰 카테고리 리스트 조회 - API")
    void getAllCouponCategoryTest() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        List<CouponCategoryResponseDto> couponCategoryList = List.of(
            new CouponCategoryResponseDto(1L, 1L, "정치",
                new BigDecimal(3000), 30000L, null, 30, "정치 카테고리 쿠폰", "정치 카테고리 쿠폰입니다."),
            new CouponCategoryResponseDto(2L, 2L, "역사",
                new BigDecimal(3000), 30000L, null, 30, "역사 카테고리 쿠폰", "역사 카테고리 쿠폰입니다.")
        );

        Page<CouponCategoryResponseDto> couponCategoryPage = new PageImpl<>(couponCategoryList, pageable, couponCategoryList.size());

        when(couponCategoryService.getAllCouponCategories(pageable)).thenReturn(couponCategoryPage);

        // When
        mockMvc.perform(get("/api/shop/admin/coupons/categories")
            .param("page", "0")
            .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].categoryName").value(couponCategoryList.getFirst().categoryName()))
            .andExpect(jsonPath("$.content[0].couponName").value(couponCategoryList.getFirst().couponName()))
            .andExpect(jsonPath("$.content[1].categoryName").value(couponCategoryList.getLast().categoryName()))
            .andExpect(jsonPath("$.content[1].couponName").value(couponCategoryList.getLast().couponName()))
            .andDo(document("CouponCategory-GetAllCouponCategory",
                responseFields(
                    fieldWithPath("content[].couponTemplateId")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰 템플릿 ID"),
                    fieldWithPath("content[].categoryId")
                        .type(JsonFieldType.NUMBER)
                        .description("카테고리 ID"),
                    fieldWithPath("content[].categoryName")
                        .type(JsonFieldType.STRING)
                        .description("카테고리 이름"),
                    fieldWithPath("content[].discountPrice")
                        .type(JsonFieldType.NUMBER)
                        .description("할인 가격"),
                    fieldWithPath("content[].condition")
                        .type(JsonFieldType.NUMBER)
                        .description("조건"),
                    fieldWithPath("content[].maxPrice")
                        .type(JsonFieldType.VARIES)
                        .optional()
                        .description("최대 할인 가격"),
                    fieldWithPath("content[].duration")
                        .type(JsonFieldType.NUMBER)
                        .description("기간"),
                    fieldWithPath("content[].couponName")
                        .type(JsonFieldType.STRING)
                        .description("쿠폰 이름"),
                    fieldWithPath("content[].couponDescription")
                        .type(JsonFieldType.STRING)
                        .description("쿠폰 설명"),
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
        verify(couponCategoryService, times(1)).getAllCouponCategories(pageable);
    }

    @Test
    @DisplayName("카테고리 이름 조회 - API")
    void getCategoryNameTest() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        List<CouponCategoryDetailsForCategoryDto> couponCategoryDetailsForCategoryDtoList = List.of(
            new CouponCategoryDetailsForCategoryDto(1L, "정치"),
            new CouponCategoryDetailsForCategoryDto(2L, "역사")
        );

        Page<CouponCategoryDetailsForCategoryDto> categoryNamePage = new PageImpl<>(couponCategoryDetailsForCategoryDtoList, pageable, couponCategoryDetailsForCategoryDtoList.size());

        when(couponCategoryService.getCategoriesByCouponTemplate(pageable)).thenReturn(categoryNamePage);

        // When
        mockMvc.perform(get("/api/shop/admin/coupons/categories/name/templates")
            .param("page", "0")
            .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].categoryName").value(couponCategoryDetailsForCategoryDtoList.getFirst().categoryName()))
            .andExpect(jsonPath("$.content[1].categoryName").value(couponCategoryDetailsForCategoryDtoList.getLast().categoryName()))
            .andDo(document("CouponCategory-GetCategoryName",
                responseFields(
                    fieldWithPath("content[].categoryId")
                        .type(JsonFieldType.NUMBER)
                        .description("카테고리 ID"),
                    fieldWithPath("content[].categoryName")
                        .type(JsonFieldType.STRING)
                        .description("카테고리 이름"),
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
        verify(couponCategoryService, times(1)).getCategoriesByCouponTemplate(pageable);
    }

    @Test
    void getCategoryTest() throws Exception {
        // Given
        Long categoryId = 1L;

        CategoryResponseDto response = new CategoryResponseDto(
            1L,
            null,
            "정치",
            true
        );

        when(categoryService.getCategory(categoryId)).thenReturn(response);

        // When
        mockMvc.perform(get("/api/shop/admin/coupons/category/{categoryId}", categoryId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.categoryName").value(response.categoryName()))
            .andDo(document("CouponCategory-GetCategory",
                pathParameters(
                    parameterWithName("categoryId")
                        .description("카테고리 ID")
                ),
                responseFields(
                    fieldWithPath("categoryId")
                        .type(JsonFieldType.NUMBER)
                        .description("카테고리 ID"),
                    fieldWithPath("parentCategoryId")
                        .type(JsonFieldType.VARIES)
                        .optional()
                        .description("부모 카테고리 ID"),
                    fieldWithPath("categoryName")
                        .type(JsonFieldType.STRING)
                        .description("카테고리 이름"),
                    fieldWithPath("active")
                        .type(JsonFieldType.BOOLEAN)
                        .description("활성화 여부")
                )
            ));
    }
}
