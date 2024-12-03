package shop.s5g.shop.controller.review;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import shop.s5g.shop.advice.AuthenticationAdvice;
import shop.s5g.shop.annotation.WithCustomMockUser;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.review.CreateReviewRequestDto;
import shop.s5g.shop.dto.review.ReviewResponseDto;
import shop.s5g.shop.dto.review.UpdateReviewRequestDto;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.review.ReviewService;

@WebMvcTest(
    value = ReviewController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import({TestSecurityConfig.class, AuthenticationAdvice.class})
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
                "bookId": 1,
                "orderDetailId": 1,
                "score": 3,
                "content": "This is a review content",
                "imageByteList": null,
                "extensions": null
            }
            """;

        CreateReviewRequestDto createReviewRequestDto = new CreateReviewRequestDto(1L, 1L, 3,
            "This is a review content", null, null);

        doNothing().when(reviewService).registerReview(any(), anyLong());

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/api/shop/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createReviewRequestDtoJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.message").value("리뷰가 등록되었습니다."));

        // then
        verify(reviewService, times(1)).registerReview(createReviewRequestDto, 1L);
    }

    @Test
    @WithCustomMockUser(loginId = "123", role = "ROLE_MEMBER", customerId = 1L)
    void getReviewList() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        List<ReviewResponseDto> reviews = List.of(
            new ReviewResponseDto(1L, "스프링", "123", 1L, 4, "good", LocalDateTime.now(), null),
            new ReviewResponseDto(2L, "스프링2", "123", 2L, 5, "goods", LocalDateTime.now(), null)
        );

        PageResponseDto<ReviewResponseDto> response = new PageResponseDto<>(reviews, 1, 10, 2);

        when(reviewService.getReviewList(1L, pageable)).thenReturn(response);

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/shop/review/list")
                .param("page", "0")
                .param("size", "10"))
            .andExpect(status().isOk());

        // then
        verify(reviewService, times(1)).getReviewList(1L, pageable);
    }

    @Test
    @WithCustomMockUser(loginId = "123", role = "ROLE_MEMBER", customerId = 1L)
    void updateReview() throws Exception {
        // given
        String updateReviewRequestDtoJson = """
            {
                "reviewId": 1,
                "score": 3,
                "content": "hi",
                "imageByteList": null,
                "extensions": null
            }
            """;

        UpdateReviewRequestDto updateReviewRequestDto = new UpdateReviewRequestDto(1L, 3, "hi",
            null, null);

        doNothing().when(reviewService).updateReview(any(), anyLong());

        // when
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/shop/review")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updateReviewRequestDtoJson))
            .andExpect(status().isNoContent())
            .andExpect(jsonPath("$.message").value("리뷰가 수정되었습니다."));

        // then
        verify(reviewService, times(1)).updateReview(updateReviewRequestDto, 1L);
    }
}