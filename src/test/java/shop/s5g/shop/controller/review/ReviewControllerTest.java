package shop.s5g.shop.controller.review;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import shop.s5g.shop.annotation.WithCustomMockUser;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.review.ReviewService;

@WebMvcTest(
    value = ReviewController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
class ReviewControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReviewService reviewService;

    @Test
    @WithCustomMockUser(loginId = "123", role = "ROLE_MEMBER", customerId = 1L)
    void registerReviewTest_success() throws Exception {
        // given
        String createReviewRequestDtoJson = """
            {
                "bookId": 1L,
                "orderDetailId": 1L,
                "score": 3,
                "content": "This is a review content",
                "imageByteList": null,
                "extensions": null
            }
            """;

        doNothing().when(reviewService).registerReview(any(), anyLong());

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/shop/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createReviewRequestDtoJson)
                .with(csrf()))
            .andExpect(status().isCreated())
            .andExpect(content().string("리뷰가 등록되었습니다."));
    }

    @Test
    void getReviewList() {
    }

    @Test
    void updateReview() {
    }
}