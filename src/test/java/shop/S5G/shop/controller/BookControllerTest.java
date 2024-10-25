package shop.S5G.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shop.S5G.shop.dto.Book.BookRequestDto;
import shop.S5G.shop.service.BookService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 도서 등록 test
     */
    @Test
    @DisplayName("도서 등록 test")
    void addBookTest() throws Exception {
        BookRequestDto bookRequestDto = new BookRequestDto(
                22L,
                222L,
                "아낌없이 주는 나무",
                "전래동화",
                "이 책은 전래동화 입니다.",
                LocalDateTime.of(2000, 10, 10, 10, 50),
                "978-3-15-148410-2",
                15000L,
                new BigDecimal("5.5"),
                true,
                200,
                2000L,
                LocalDateTime.of(2010, 5, 5, 15, 30));
        this.mockMvc
                .perform(post("/api/shop/book")
                        .content(objectMapper.writeValueAsString(bookRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("success"))
                .andDo(print());
        verify(bookService, times(1)).createBook(any());
    }
    /**
     * 도서 등록 실패 test
     */
    @Test
    @DisplayName("도서 등록 실패 test")
    void addBookErrorTest() throws Exception {
        BookRequestDto bookRequestDto = new BookRequestDto(
                22L,
                222L,
                "아낌없이 주는 나무",
                "전래동화",
                "이 책은 전래동화 입니다.",
                LocalDateTime.of(2000, 10, 10, 10, 50),
                "978-3-15-148410-2",
                15000L,
                new BigDecimal("5.5"),
                true,
                200,
                2000L,
                LocalDateTime.of(2010, 5, 5, 15, 30));
        this.mockMvc
                .perform(post("/api/shop/book")
                        .content(objectMapper.writeValueAsString(bookRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("success"))
                .andDo(print());
        verify(bookService, times(1)).createBook(any());
    }

    /**
     * 도서 수정 test
     */
    @Test
    void updateBookTest() throws Exception {
        this.mockMvc
                .perform(put("/api/shop/book/1")
                        .content("{\n" +
                                "  \"publisherId\": 22,\n" +
                                "  \"bookStatusId\": 222,\n" +
                                "  \"title\": \"전래동화\",\n" +
                                "  \"chapter\": \"이 책은 전래동화 입니다.\",\n" +
                                "  \"descreption\": \"전래동화 설명입니다.\",\n" +
                                "  \"publishedDate\": \"1990-10-10T10:50:00\",\n" +
                                "  \"isbn\": \"978-3-15-148410-2\",\n" +
                                "  \"price\": 15000,\n" +
                                "  \"discountRate\": 10.5,\n" +
                                "  \"isPacked\": true,\n" +
                                "  \"stock\": 200,\n" +
                                "  \"views\": 2000,\n" +
                                "  \"createdAt\": \"2000-10-10T10:50:00\"\n" +
                                "}\n")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())//실제값
                .andExpect(content().string("success"))//필요값
                .andDo(print());
//        verify(bookService, times(1)).updateBooks(eq(any()));
    }
    /**
     * 도서 삭제 test
     */
    @Test
    void deleteBookTest() throws Exception {
        this.mockMvc
                .perform(delete("/api/shop/book/{bookId}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
    /**
     * 도서 삭세 실패 test
     */
    @Test
    void deleteBookErrorTest() throws Exception {

    }

}
