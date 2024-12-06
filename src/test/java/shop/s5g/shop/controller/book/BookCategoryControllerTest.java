package shop.s5g.shop.controller.book;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.book.category.impl.BookCategoryServiceImpl;


@WebMvcTest(
    value = BookCategoryController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import(TestSecurityConfig.class)
class BookCategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookCategoryServiceImpl bookCategoryService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 도서카테고리 등록 test
     */
    @Test
    void addBookCategoryTest() throws Exception {
        this.mockMvc
                .perform(post("/api/shop/bookcategory/1/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(bookCategoryService, times(1)).addCategoryInBook(any(), any());
    }

}

