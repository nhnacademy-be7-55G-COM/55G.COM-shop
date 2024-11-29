package shop.s5g.shop.controller.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
//        Long bookId = 1L;
//        Long customerId = 15L;
//        String message = "좋아요 등록 성공";
//
////        given(shopMemberDetail.getCustomerId()).willReturn(customerId);
//        willDoNothing().given(likeService).addLikeInBook(customerId, bookId);
//
//        //when&then
//        mockMvc.perform(post("/api/shop/like/{bookId}", bookId)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value(message));
//    }
}
