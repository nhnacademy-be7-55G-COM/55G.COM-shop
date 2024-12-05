package shop.s5g.shop.controller.like;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.controller.book.LikeController;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.security.ShopMemberDetail;
import shop.s5g.shop.service.like.LikeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

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
public class LikeControllerTest {

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
//    @Test
//    @DisplayName("좋아요 등록 test")
//    void addLikeTest() throws Exception {
//        //given
//        Long book_Id = 1L;
//        Long customerId = 15L;
//        String message = "좋아요 등록 성공";
//
//        given(shopMemberDetail.getCustomerId()).willReturn(customerId);
////        willDoNothing().given(likeService).addLikeInBook(customerId, bookId);
//
//        //when&then
//        mockMvc.perform(post("/api/shop/like/{bookId}", 1)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(1L, 15L))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value(message));
//    }
}
