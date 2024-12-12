package shop.s5g.shop.controller.like;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.controller.book.LikeController;
import shop.s5g.shop.dto.book.BookLikeResponseDto;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.security.ShopMemberDetail;
import shop.s5g.shop.service.like.LikeService;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        value = LikeController.class,
        excludeFilters = @ComponentScan.Filter(
                type= FilterType.ASSIGNABLE_TYPE,
                classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
        )
)
@Import(TestSecurityConfig.class)
class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikeService likeService;

    @MockBean
    private ShopMemberDetail shopMemberDetail;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 좋아요 등록 test
     */
    @Test
    void addLikeSuccessTest() throws Exception {
        // Given
        Long bookId = 1L;
        ShopMemberDetail shopMemberDetail = new ShopMemberDetail("user", "ROLE_USER", 1L);

        // Mock Security Context
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(shopMemberDetail, null));

        // When & Then
        mockMvc.perform(post("/api/shop/like/{bookId}", bookId)
                        .with(authentication(new UsernamePasswordAuthenticationToken(shopMemberDetail, null)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("좋아요 등록 성공!"))
                .andDo(print());

        verify(likeService, times(1)).addLikeInBook(1L, 1L);
    }

    /**
     * 도서 내 좋아요 삭제 test
     */
    @Test
    @DisplayName("좋아요 삭제 성공 테스트")
    void deleteLikeSuccessTest() throws Exception {
        // Given
        Long bookId = 1L;
        ShopMemberDetail shopMemberDetail = new ShopMemberDetail("user123", "ROLE_USER", 1L);

        // Mock Security Context
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(shopMemberDetail, null));

        // When & Then
        mockMvc.perform(delete("/api/shop/like/{bookId}", bookId)
                        .with(authentication(new UsernamePasswordAuthenticationToken(shopMemberDetail, null)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("좋아요 삭제 성공!"))
                .andDo(print());

        verify(likeService, times(1)).deleteLikeInBook(1L, 1L);
    }

    /**
     * 좋아요 누른 도서 확인 test
     */
    @Test
    @DisplayName("좋아요 조회 성공 테스트")
    void getLikeSuccessTest() throws Exception {
        // Given
        ShopMemberDetail shopMemberDetail = new ShopMemberDetail("user123", "ROLE_USER", 1L);

        List<BookLikeResponseDto> mockBooks = List.of(
                new BookLikeResponseDto(1L, "Book 1", 15000L, "Publisher 1", "Available"),
                new BookLikeResponseDto(2L, "Book 2", 20000L, "Publisher 2", "Sold Out")
        );

        // Mock 서비스 호출
        when(likeService.getLikeBookByCustomerId(1L)).thenReturn(mockBooks);

        // Mock Security Context
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(shopMemberDetail, null));

        // When & Then
        mockMvc.perform(get("/api/shop/like")
                        .with(authentication(new UsernamePasswordAuthenticationToken(shopMemberDetail, null)))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].bookId").value(1L))
                .andExpect(jsonPath("$[0].title").value("Book 1"))
                .andExpect(jsonPath("$[0].price").value(15000L))
                .andExpect(jsonPath("$[0].publisher").value("Publisher 1"))
                .andExpect(jsonPath("$[1].bookId").value(2L))
                .andExpect(jsonPath("$[1].title").value("Book 2"))
                .andExpect(jsonPath("$[1].price").value(20000L))
                .andExpect(jsonPath("$[1].publisher").value("Publisher 2"))
                .andDo(print());

        // Verify
        verify(likeService, times(1)).getLikeBookByCustomerId(1L);
    }
}
