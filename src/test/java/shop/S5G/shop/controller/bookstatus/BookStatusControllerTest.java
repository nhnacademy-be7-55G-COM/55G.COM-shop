package shop.S5G.shop.controller.bookstatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import shop.S5G.shop.controller.book.BookStatusController;
import shop.S5G.shop.service.bookstatus.impl.BookStatusServiceImpl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookStatusController.class)
public class BookStatusControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookStatusServiceImpl bookStatusServiceImpl;

    @Autowired
    ObjectMapper objectMapper;

    /**
     * 도서상태 전체 조회 test
     */
    @Test
    @DisplayName("도서상태 전체 조회 test")
    void getAllBookStatus() throws Exception {
        mockMvc.perform(get("/api/shop/bookstatus"))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
