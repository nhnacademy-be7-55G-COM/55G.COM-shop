package shop.s5g.shop.controller.bookstatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.controller.book.BookStatusController;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.bookstatus.impl.BookStatusServiceImpl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        value = BookStatusController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
        )
)
@Import(TestSecurityConfig.class)
class BookStatusControllerTest {

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
