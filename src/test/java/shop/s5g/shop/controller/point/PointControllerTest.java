package shop.s5g.shop.controller.point;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import shop.s5g.shop.advice.AuthenticationAdvice;
import shop.s5g.shop.annotation.WithCustomMockUser;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.point.PointHistoryResponseDto;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.point.PointHistoryService;

@WebMvcTest(
    value = PointController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import({TestSecurityConfig.class, AuthenticationAdvice.class})
@EnableAspectJAutoProxy
class PointControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    PointHistoryService pointHistoryService;

    PointHistoryResponseDto response = new PointHistoryResponseDto(
        1L ,"구매적립", 1000L, 1000L, LocalDateTime.now()
    );

    @Test
    @WithCustomMockUser(loginId = "123", role = "ROLE_MEMBER", customerId = 1L)
    void fetchPointHistoryTest() throws Exception {
        PageResponseDto<PointHistoryResponseDto> page = new PageResponseDto<>(
            List.of(response), 1, 5, 1
        );
        when(pointHistoryService.getPointHistoryPage(anyLong(), any())).thenReturn(page);

        mvc.perform(MockMvcRequestBuilders.get("/api/shop/point/history")
            .queryParam("size", "5")
            .queryParam("page", "0")
        )
            .andExpect(jsonPath("$.content[0].point").value(1000))
            .andExpect(jsonPath("$.totalElements").value(1));

        verify(pointHistoryService, times(1)).getPointHistoryPage(anyLong(), any());
    }
}
