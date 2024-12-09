package shop.s5g.shop.controller.book;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import shop.s5g.shop.advice.AuthenticationAdvice;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.book.BookDocumentResponseDto;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.book.BookDocumentService;

@WebMvcTest(
    value = BookDocumentController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import({TestSecurityConfig.class, AuthenticationAdvice.class})
class BookDocumentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    BookDocumentService bookDocumentService;

    @Test
    @DisplayName("키워드가 없는 경우 모든 책 반환")
    void searchByKeyword_EmptyKeyword_ReturnsAllBooks() throws Exception {
        // given
        String keyword = "";
        Pageable pageable = PageRequest.of(0, 10);
        List<BookDocumentResponseDto> bookList = new ArrayList<>();
        bookList.add(
            new BookDocumentResponseDto(1L, "spring", null, "good", LocalDateTime.now(), "123123",
                15000L, BigDecimal.ZERO, false, 0, 0L, LocalDateTime.now(), LocalDateTime.now(),
                null, null, null, null, null));

        when(bookDocumentService.findAllBooks(any(Pageable.class)))
            .thenReturn(new PageResponseDto<>(bookList, 1, pageable.getPageSize(), 1));

        // when & then
        mockMvc.perform(get("/api/shop/book/list")
                .param("keyword", keyword)
                .param("page", String.valueOf(pageable.getPageNumber()))
                .param("size", String.valueOf(pageable.getPageSize())))
            .andExpect(status().isOk());

        verify(bookDocumentService, times(1)).findAllBooks(pageable);
    }

    @Test
    @DisplayName("키워드가 있는 경우 검색 결과 반환")
    void searchByKeyword_WithKeyword_ReturnsSearchResults() throws Exception {
        // given
        String keyword = "spring";
        Pageable pageable = PageRequest.of(0, 10);
        List<BookDocumentResponseDto> bookList = new ArrayList<>();
        bookList.add(
            new BookDocumentResponseDto(1L, "spring book", null, "good", LocalDateTime.now(),
                "123123", 15000L, BigDecimal.ZERO, false, 0, 0L, LocalDateTime.now(),
                LocalDateTime.now(), null, null, null, null, null));

        when(bookDocumentService.searchByKeyword(anyString(), any(Pageable.class)))
            .thenReturn(new PageResponseDto<>(bookList, 1, pageable.getPageSize(), 1));

        // when & then
        mockMvc.perform(get("/api/shop/book/list")
                .param("keyword", keyword)
                .param("page", String.valueOf(pageable.getPageNumber()))
                .param("size", String.valueOf(pageable.getPageSize())))
            .andExpect(status().isOk());

        verify(bookDocumentService, times(1)).searchByKeyword(keyword, pageable);
    }

    @Test
    @DisplayName("카테고리 검색에서 키워드가 없는 경우")
    void searchByCategoryAndKeyword_EmptyKeyword_ReturnsAllBooks() throws Exception {
        // given
        String categoryName = "프로그래밍";
        String keyword = "";
        Pageable pageable = PageRequest.of(0, 10);
        List<BookDocumentResponseDto> bookList = new ArrayList<>();
        bookList.add(
            new BookDocumentResponseDto(1L, "spring", null, "good", LocalDateTime.now(), "123123",
                15000L, BigDecimal.ZERO, false, 0, 0L, LocalDateTime.now(), LocalDateTime.now(),
                null, new String[]{categoryName}, null, null, null));

        when(bookDocumentService.findAllBooksByCategory(anyString(), any(Pageable.class)))
            .thenReturn(new PageResponseDto<>(bookList, 1, pageable.getPageSize(), 1));

        // when & then
        mockMvc.perform(get("/api/shop/book/list/category")
                .param("name", categoryName)
                .param("keyword", keyword)
                .param("page", String.valueOf(pageable.getPageNumber()))
                .param("size", String.valueOf(pageable.getPageSize())))
            .andExpect(status().isOk());

        verify(bookDocumentService, times(1)).findAllBooksByCategory(categoryName, pageable);
    }

    @Test
    @DisplayName("키워드가 있는 경우 검색 결과 반환")
    void searchByCategoryAndKeyword_WithKeyword_ReturnsSearchResults() throws Exception {
        // given
        String categoryName = "프로그래밍";
        String keyword = "spring";
        Pageable pageable = PageRequest.of(0, 10);
        List<BookDocumentResponseDto> bookList = new ArrayList<>();
        bookList.add(
            new BookDocumentResponseDto(1L, "spring book", null, "good", LocalDateTime.now(),
                "123123", 15000L, BigDecimal.ZERO, false, 0, 0L, LocalDateTime.now(),
                LocalDateTime.now(), null, new String[]{categoryName}, null, null, null));

        when(bookDocumentService.searchByCategoryAndKeyword(anyString(), anyString(),
            any(Pageable.class)))
            .thenReturn(new PageResponseDto<>(bookList, 1, pageable.getPageSize(), 1));

        // when & then
        mockMvc.perform(get("/api/shop/book/list/category")
                .param("name", categoryName)
                .param("keyword", keyword)
                .param("page", String.valueOf(pageable.getPageNumber()))
                .param("size", String.valueOf(pageable.getPageSize())))
            .andExpect(status().isOk());

        verify(bookDocumentService, times(1)).searchByCategoryAndKeyword(categoryName, keyword,
            pageable);
    }
}