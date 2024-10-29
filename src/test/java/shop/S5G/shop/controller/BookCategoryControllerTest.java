package shop.S5G.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import shop.S5G.shop.controller.book.BookCategoryController;
import shop.S5G.shop.service.BookCategory.impl.BookCategoryServiceImpl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookCategoryController.class)
public class BookCategoryControllerTest {
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
                .andExpect(status().isOk())
                .andExpect(content().string("등록 성공"))
                .andDo(print());
        verify(bookCategoryService, times(1)).addCategoryInBook(any(), any());
    }

    /**
     * 도서카테고리 조회 test
     */
    @Test
    void getBookCategoryTest() throws Exception {
        this.mockMvc
                .perform(get("/api/shop/bookcategory/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
        verify(bookCategoryService, times(1)).getCategoryInBook(any());

    }


    /**
     * 도서카테고리 삭제 test
     */
    @Test
    void deleteBookCategoryTest() throws Exception {
    this.mockMvc
            .perform(delete("/api/shop/bookcategory/1/1"))
            .andExpect(status().isOk())
            .andDo(print());
    verify(bookCategoryService, times(1)).deleteCategory(any(), any());
    }
}

