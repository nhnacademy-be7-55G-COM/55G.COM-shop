package shop.s5g.shop.controller.coupon.book;

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
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.controller.coupon.CouponBookController;
import shop.s5g.shop.dto.coupon.book.CouponBookDetailsForBookDto;
import shop.s5g.shop.dto.coupon.book.CouponBookRequestDto;
import shop.s5g.shop.dto.coupon.book.CouponBookResponseDto;
import shop.s5g.shop.dto.coupon.template.CouponTemplateResponseDto;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.coupon.book.impl.CouponBookServiceImpl;

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
            .andExpect(status().isCreated())
            .andDo(document("CouponBook-Create",
                requestFields(
                    fieldWithPath("couponTemplateId")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰 템플릿 ID"),
                    fieldWithPath("bookId")
                        .type(JsonFieldType.NUMBER)
                        .description("책 ID")
                ),
                responseFields(
                    fieldWithPath("message")
                        .type(JsonFieldType.STRING)
                        .description("응답 메시지")
                )
            ));

        // Then
        verify(couponBookService, times(1)).createCouponBook(couponBookRequestDto);
    }

    @Test
    @DisplayName("모든 책에 적용된 쿠폰 찾기 - API")
    void getAllCouponBooks() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        List<CouponBookResponseDto> bookList = List.of(
            new CouponBookResponseDto(1L, 1L, "테스트 책", new BigDecimal(10000), 30000L, null, 30, "책 쿠폰", "책 쿠폰 테스트"),
            new CouponBookResponseDto(2L, 2L, "시험 책", new BigDecimal(10000), 30000L, null, 30, "책 쿠폰", "책 쿠폰 테스트")
        );
        Page<CouponBookResponseDto> couponBookResponseDtoPage = new PageImpl<>(bookList, pageable, bookList.size());

        when(couponBookService.getCouponBooks(pageable)).thenReturn(couponBookResponseDtoPage);

        // When
        mockMvc.perform(get("/api/shop/admin/coupons/books")
            .param("page", "0")
            .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].title").value(bookList.getFirst().title()))
            .andExpect(jsonPath("$.content[0].condition").value(bookList.getFirst().condition()))
            .andExpect(jsonPath("$.content[0].maxPrice").value(bookList.getFirst().maxPrice()))
            .andExpect(jsonPath("$.content[1].title").value(bookList.getLast().title()))
            .andExpect(jsonPath("$.content[1].condition").value(bookList.getLast().condition()))
            .andExpect(jsonPath("$.content[1].maxPrice").value(bookList.getLast().maxPrice()))
            .andDo(document("CouponBook-GetAllCouponBook",
                responseFields(
                    fieldWithPath("content[].couponTemplateId")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰 템플릿 ID"),
                    fieldWithPath("content[].bookId")
                        .type(JsonFieldType.NUMBER)
                        .description("책 ID"),
                    fieldWithPath("content[].title")
                        .type(JsonFieldType.STRING)
                        .description("책 제목"),
                    fieldWithPath("content[].discountPrice")
                        .type(JsonFieldType.NUMBER)
                        .description("할인 가격"),
                    fieldWithPath("content[].condition")
                        .type(JsonFieldType.NUMBER)
                        .description("할인 조건"),
                    fieldWithPath("content[].maxPrice")
                        .type(JsonFieldType.NULL)
                        .description("할인 최대 가격"),
                    fieldWithPath("content[].duration")
                        .type(JsonFieldType.NUMBER)
                        .description("할인 기간"),
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
        verify(couponBookService, times(1)).getCouponBooks(pageable);
    }

    @Test
    @DisplayName("특정 책에 적용된 쿠폰 템플릿 조회 테스트 - API")
    void getCouponBookTemplateTest() throws Exception {
        // Given
        Long bookId = 1L;

        Pageable pageable = PageRequest.of(0, 10);

        List<CouponTemplateResponseDto> templateList = List.of(
            new CouponTemplateResponseDto(1L, new BigDecimal(5000), 30000L, null, 30, "테스트 쿠폰", "테스트 쿠폰입니다."),
            new CouponTemplateResponseDto(2L, new BigDecimal("0.5"), 50000L, 3000L, 15, "책 쿠폰", "책 쿠폰입니다.")
        );

        Page<CouponTemplateResponseDto> templatePage = new PageImpl<>(templateList, pageable, templateList.size());

        when(couponBookService.getCouponBooksByBookId(bookId, pageable)).thenReturn(templatePage);

        // When
        mockMvc.perform(get("/api/shop/admin/coupons/books/{bookId}", bookId)
            .param("page", "0")
            .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].maxPrice").value(templateList.getFirst().maxPrice()))
            .andExpect(jsonPath("$.content[0].condition").value(templateList.getFirst().condition()))
            .andExpect(jsonPath("$.content[1].maxPrice").value(templateList.getLast().maxPrice()))
            .andExpect(jsonPath("$.content[1].condition").value(templateList.getLast().condition()))
            .andDo(document("CouponBook-GetTemplateList",
                pathParameters(
                    parameterWithName("bookId")
                        .description("책 ID")
                ),
                responseFields(
                    fieldWithPath("content[].couponTemplateId")
                        .type(JsonFieldType.NUMBER)
                        .description("쿠폰 템플릿 ID"),
                    fieldWithPath("content[].discountPrice")
                        .type(JsonFieldType.NUMBER)
                        .description("할인 가격"),
                    fieldWithPath("content[].condition")
                        .type(JsonFieldType.NUMBER)
                        .description("할인 조건"),
                    fieldWithPath("content[].maxPrice")
                        .type(JsonFieldType.VARIES)
                        .optional()
                        .description("최대 할인 가격"),
                    fieldWithPath("content[].duration")
                        .type(JsonFieldType.NUMBER)
                        .description("할인 기간"),
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
        verify(couponBookService, times(1)).getCouponBooksByBookId(bookId, pageable);
    }

    @Test
    @DisplayName("쿠폰이 적용된 책 조회 - API")
    void getBookListTest() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10);

        List<CouponBookDetailsForBookDto> bookList = List.of(
            new CouponBookDetailsForBookDto(1L, "테스트 책"),
            new CouponBookDetailsForBookDto(2L, "Java Programming")
        );

        Page<CouponBookDetailsForBookDto> bookPage = new PageImpl<>(bookList, pageable, bookList.size());

        when(couponBookService.getCouponBooksByTemplateId(pageable)).thenReturn(bookPage);

        // When
        mockMvc.perform(get("/api/shop/admin/coupons/books/templates")
            .param("page", "0")
            .param("size", "10"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content[0].title").value(bookList.getFirst().title()))
            .andExpect(jsonPath("$.content[1].title").value(bookList.getLast().title()))
            .andDo(document("CouponBook-GetBookList",
                responseFields(
                    fieldWithPath("content[].bookId")
                        .type(JsonFieldType.NUMBER)
                        .description("책 ID"),
                    fieldWithPath("content[].title")
                        .type(JsonFieldType.STRING)
                        .description("책 제목"),
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
        verify(couponBookService, times(1)).getCouponBooksByTemplateId(pageable);
    }
}
