package shop.s5g.shop.controller.book;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.dto.book.BookDetailResponseDto;
import shop.s5g.shop.dto.book.BookPageableResponseDto;
import shop.s5g.shop.dto.book.author.BookAuthorResponseDto;
import shop.s5g.shop.dto.book.category.BookDetailCategoryResponseDto;
import shop.s5g.shop.dto.tag.TagResponseDto;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.book.BookService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AutoConfigureRestDocs
@WebMvcTest(
        value = BookController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
        )
)
@Import(TestSecurityConfig.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    String content = """
            {
                "publisherId": 22,
                "bookStatusId": 222,
                "categoryId": 232,
                "title": "채식주의자",
                "chapter": "장편소설",
                "description": "리마스터판",
                "publishedDate": "2020-02-03T10:34:34",
                "isbn": "34-54-6467",
                "price": 3564,
                "discountRate": 35.6,
                "isPacked": true,
                "stock": 34,
                "tagIdList": [1, 3]
            }
            """;

    /**
     * 도서 등록 test
     */
    @Test
    void addBook() throws Exception {
        mockMvc.perform(post("/api/shop/book")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andDo(print())
                .andDo(document("add-book", // REST Docs 문서 생성
                        requestFields(
                                fieldWithPath("publisherId").description("출판사 ID"),
                                fieldWithPath("bookStatusId").description("도서 상태 ID"),
                                fieldWithPath("categoryId").description("카테고리 ID"),
                                fieldWithPath("title").description("도서 제목"),
                                fieldWithPath("chapter").description("도서 장르"),
                                fieldWithPath("description").description("도서 설명"),
                                fieldWithPath("publishedDate").description("출판일"),
                                fieldWithPath("isbn").description("ISBN 번호"),
                                fieldWithPath("price").description("가격"),
                                fieldWithPath("discountRate").description("할인율"),
                                fieldWithPath("isPacked").description("포장 여부"),
                                fieldWithPath("stock").description("재고"),
                                fieldWithPath("tagIdList").description("태그 ID 리스트")
                        ),
                        responseFields(
                                fieldWithPath("message").description("도서 등록 성공 메시지")
                        )
                ));
        verify(bookService, times(1)).createBook(any());
    }

    /**
     * 도서 등록 실패 test
     */
    @Test
    @DisplayName("도서 등록 실패 test")
    void addBookErrorTest() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(post("/api/shop/books")
                        .content("""
                                {
                                  "publisherId": null,
                                  "bookStatusId": 222,
                                  "title": "",
                                  "chapter": 5,
                                  "descreption": "",
                                  "publishedDate": "",
                                  "isbn": "978-3-15-148410-2",
                                  "price": 15000,
                                  "discountRate": 10.5,
                                  "isPacked": true,
                                  "stock": 200,
                                  "views": 2000,
                                  "createdAt": "2000-10-10T10:50:00"
                                }
                                """)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn().getResponse();

        if (response.getContentAsString().isEmpty()) {
            System.out.println("Response body is empty; skipping responseFields documentation.");
        } else {
            mockMvc.perform(post("/api/shop/books")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(response.getContentAsString()))
                    .andDo(document("add-book-error",
                            responseFields(
                                    fieldWithPath("error").description("오류 메시지"),
                                    fieldWithPath("timestamp").description("오류 발생 시간"),
                                    fieldWithPath("status").description("HTTP 상태 코드"),
                                    fieldWithPath("path").description("요청 경로")
                            )
                    ));
        }
    }


    /**
     * 도서 목록 조회 test
     */
    @Test
    @DisplayName("도서 목록 조회 test")
    void getAllBooksPageableTest() throws Exception {
        List<BookPageableResponseDto> books = new ArrayList<>();

        BookPageableResponseDto b1 = new BookPageableResponseDto(
                1L,                         // bookId
                1L,                         // publisherId
                1L,                         // bookStatusId
                "도서1",                // title
                "챕터1",                // chapter
                "도서1의 설명 입니다.", // description
                LocalDate.of(2023, 10, 1),      // publishedDate
                "978-3-16-148410-0",            // isbn
                15000L,                         // price
                new BigDecimal("0.15"),         // discountRate
                true,                           // isPacked
                50,                             // stock
                1000L,                          // views
                LocalDateTime.of(2023, 10, 15, 12, 34, 56), // createdAt
                "sample-image1.jpg"              // imageName
        );

        BookPageableResponseDto b2 = new BookPageableResponseDto(
                2L,                         // bookId
                2L,                         // publisherId
                2L,                         // bookStatusId
                "도서2",                // title
                "챕터2",                // chapter
                "도서2의 설명 입니다.", // description
                LocalDate.of(2023, 10, 2),      // publishedDate
                "978-3-16-148410-1",            // isbn
                15000L,                         // price
                new BigDecimal("0.15"),         // discountRate
                true,                           // isPacked
                50,                             // stock
                1000L,                          // views
                LocalDateTime.of(2023, 10, 15, 12, 34, 56), // createdAt
                "sample-image2.jpg"              // imageName
        );

        books.add(b1);
        books.add(b2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<BookPageableResponseDto> page = new PageImpl<>(books, pageable, books.size());

        given(bookService.allBookPageable(any())).willReturn(page);

        mockMvc.perform(get("/api/shop/books/pageable")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("도서1"))
                .andExpect(jsonPath("$.content[1].title").value("도서2"));

        verify(bookService).allBookPageable(any());

    }

    /**
     * 도서 상세 조회 test
     */
    @Test
    @DisplayName("도서 상세 조회 test")
    void getBookByIdTest() throws Exception {
        BookDetailResponseDto mockResponse = new BookDetailResponseDto(
                1L,
                "Sample Publisher",
                "Sample Type",
                "Sample Title",
                "Sample Chapter",
                "Sample Description",
                LocalDate.of(2023, 10, 1),
                "978-3-16-148410-0",
                15000L,
                new BigDecimal("0.15"),
                true,
                50,
                1000L,
                LocalDateTime.of(2023, 10, 15, 12, 34, 56),
                LocalDateTime.of(2023, 10, 20, 14, 0, 0),
                "path/to/image.jpg",
                List.of(new BookAuthorResponseDto(1L, "Author Name", 1L, "AUTHOR")),
                List.of(new BookDetailCategoryResponseDto(1L, "Category Name")),
                List.of(new TagResponseDto(1L, "Tag Name", true)),
                5L
        );

        when(bookService.getBookById(any())).thenReturn(mockResponse);

        mockMvc.perform(get("/api/shop/book/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Sample Title"));
    }

    /**
     * 도서 상세조회 실패 test
     */
    @Test
    @DisplayName("도서 상세조회 실패 test")
    void getBookByIdErrorTest() throws Exception {
        BookDetailResponseDto mockResponse = new BookDetailResponseDto(
                1L,
                "Sample Publisher",
                "Sample Type",
                "Sample Title",
                "Sample Chapter",
                "Sample Description",
                LocalDate.of(2023, 10, 1),
                "978-3-16-148410-0",
                15000L,
                new BigDecimal("0.15"),
                true,
                50,
                1000L,
                LocalDateTime.of(2023, 10, 15, 12, 34, 56),
                LocalDateTime.of(2023, 10, 20, 14, 0, 0),
                "path/to/image.jpg",
                List.of(new BookAuthorResponseDto(1L, "Author Name", 1L, "AUTHOR")),
                List.of(new BookDetailCategoryResponseDto(1L, "Category Name")),
                List.of(new TagResponseDto(1L, "Tag Name", true)),
                5L
        );
        when(bookService.getBookById(any())).thenReturn(mockResponse);

        mockMvc.perform(get("/api/shop/book/-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * 도서 수정 test
     */
    @Test
    @DisplayName("도서 수정 test")
    void updateBookTest() throws Exception {
        this.mockMvc
                .perform(put("/api/shop/books/{bookId}", 1)
                        .content("""
                                {
                                  "publisherId": 2,
                                  "bookStatusId": 3,
                                  "categoryId": 222,
                                  "title": "전래동화",
                                  "chapter": "이 책은 전래동화 입니다.",
                                  "description": "전래동화 설명입니다.",
                                  "publishedDate": "1990-10-10",
                                  "isbn": "978-3-15-148410-2",
                                  "price": 15000,
                                  "discountRate": 10.5,
                                  "isPacked": true,
                                  "stock": 200,
                                  "thumbnailPath": "1.png",
                                  "tagIdList": [1, 4]
                                }
                                """)

                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())//실제값
                .andDo(print());
    }

    /**
     * 도서 수정 (bookId < 1) 실패
     */
    @Test
    @DisplayName("도서 수정 bookId<1 실패")
    void updateBookIdTest() throws Exception {
        this.mockMvc
                .perform(put("/api/shop/books/{bookId}", -1)
                        .content("""
                                {
                                  "publisherId": 2,
                                  "bookStatusId": 3,
                                  "categoryId": 222,
                                  "title": "전래동화",
                                  "chapter": "이 책은 전래동화 입니다.",
                                  "description": "전래동화 설명입니다.",
                                  "publishedDate": "1990-10-10",
                                  "isbn": "978-3-15-148410-2",
                                  "price": 15000,
                                  "discountRate": 10.5,
                                  "isPacked": true,
                                  "stock": 200,
                                  "thumbnailPath": "1.png",
                                  "tagIdList": [1, 4]
                                }
                                """)

                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())//실제값
                .andDo(print());
    }

    /**
     * 도서 수정 (bindingResult) 실패
     */
    @Test
    @DisplayName("도서 수정 bindingResult 실패")
    void updateBookErrorTest() throws Exception {
        this.mockMvc
                .perform(put("/api/shop/books/{bookId}", 1)
                        .content("""
                                {
                                  "bookStatusId": 3,
                                  "categoryId": 222,
                                  "title": "전래동화",
                                  "chapter": "이 책은 전래동화 입니다.",
                                  "description": "전래동화 설명입니다.",
                                  "publishedDate": "1990-10-10",
                                  "isbn": "978-3-15-148410-2",
                                  "price": 15000,
                                  "discountRate": 10.5,
                                  "isPacked": true,
                                  "stock": 200,
                                  "thumbnailPath": "1.png",
                                  "tagIdList": [1, 4]
                                }
                                """)

                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())//실제값
                .andDo(print());
    }

    /**
     * 도서 삭제 test
     */
    @Test
    @DisplayName("도서 삭제 test")
    void deleteBookTest() throws Exception {
        this.mockMvc
                .perform(delete("/api/shop/books/{bookId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("도서 삭제 성공"));

        Mockito.verify(bookService).deleteBooks(any());
    }

    /**
     * 도서 삭세 bookId 실패 test
     */
    @Test
    void deleteBookIdErrorTest() throws Exception {
        this.mockMvc
                .perform(delete("/api/shop/books/{bookId}", "A")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

}
