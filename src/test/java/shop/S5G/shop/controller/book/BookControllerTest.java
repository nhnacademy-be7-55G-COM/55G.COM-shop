package shop.S5G.shop.controller.book;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shop.S5G.shop.service.book.impl.BookServiceImpl;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookServiceImpl bookServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 도서 등록 test
     */
    String content = """
            {
                "publisherId": 22,
                "bookStatusId": 222,
                "title": "채식주의자",
                "chapter": "장편소설",
                "description": "리마스터판",
                "publishedDate": "2020-02-03T10:34:34",
                "isbn": "34-54-6467",
                "price": 3564,
                "discountRate": 35.6,
                "isPacked": true,
                "stock": 34,
                "views": 45874,
                "createdAt": "2020-02-03T10:34:34"
            }
            """;

    @Test
    void addBook() throws Exception {
        mockMvc.perform(post("/api/shop/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content))
                .andExpect(status().isOk())
                .andDo(print());
        verify(bookServiceImpl, times(1)).createBook(any());
    }

    /**
     * 도서 등록 실패 test
     */
    @Test
    @DisplayName("도서 등록 실패 test")
    void addBookErrorTest() throws Exception {
        this.mockMvc
                .perform(post("/api/shop/book")
                        //bad request
                        .content("{\n" +
                                "  \"publisherId\": 22,\n" +
                                "  \"bookStatusId\": 222,\n" +
                                "  \"title\": \"\",\n" +
                                "  \"chapter\": 5,\n" +
                                "  \"descreption\": \"\",\n" +
                                "  \"publishedDate\": \"\",\n" +
                                "  \"isbn\": \"978-3-15-148410-2\",\n" +
                                "  \"price\": 15000,\n" +
                                "  \"discountRate\": 10.5,\n" +
                                "  \"isPacked\": true,\n" +
                                "  \"stock\": 200,\n" +
                                "  \"views\": 2000,\n" +
                                "  \"createdAt\": \"2000-10-10T10:50:00\"\n" +
                                "}\n")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * 도서 수정 test
     */
    @Test
    @DisplayName("도서 수정 test")
    void updateBookTest() throws Exception {
        this.mockMvc
                .perform(put("/api/shop/book/{bookId}", 1)
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
                .andDo(print());
    }
    /**
     * 도서 삭제 test
     */
    @Test
    @DisplayName("도서 삭제 test")
    void deleteBookTest() throws Exception {
        this.mockMvc
                .perform(delete("/api/shop/book/{bookId}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
    /**
     * 도서 삭세 실패 test
     */
    @Test
    void deleteBookErrorTest() throws Exception {
        this.mockMvc
                .perform(delete("/api/shop/book/{bookId}", "A")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

}
