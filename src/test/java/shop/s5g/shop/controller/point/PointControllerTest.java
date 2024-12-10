package shop.s5g.shop.controller.point;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static shop.s5g.shop.test.domain.TestConstants.TEST_AUTHORIZATION;
import static shop.s5g.shop.test.utils.RestDocsHelper.SNIPPET_AUTH_HEADER;
import static shop.s5g.shop.test.utils.RestDocsHelper.customDocs;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import shop.s5g.shop.advice.AuthenticationAdvice;
import shop.s5g.shop.annotation.WithCustomMockUser;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.point.PointHistoryCreateResponseDto;
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
@AutoConfigureRestDocs
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
            .header("Authorization", TEST_AUTHORIZATION)
            .queryParam("size", "5")
            .queryParam("page", "0")
        )
            .andExpect(jsonPath("$.content[0].point").value(1000))
            .andExpect(jsonPath("$.totalElements").value(1))
            .andDo(customDocs("point-history-fetch", SNIPPET_AUTH_HEADER, queryParameters(
                parameterWithName("size").description("가져올 페이지 사이즈"),
                parameterWithName("page").description("가져올 페이지 번호 (0부터 시작)")
            ), responseFields(
                fieldWithPath("content").description("포인트 기록 리스트"),
                fieldWithPath("content[].id").description("포인트 기록 고유 ID"),
                fieldWithPath("content[].pointSource").description("포인트 타입(구매, 환불, ..)"),
                fieldWithPath("content[].point").description("포인트 변동사항 (음수 가능)"),
                fieldWithPath("content[].remainingPoint").description("해당 포인트 기록 후 당시 잔여 포인트"),
                fieldWithPath("content[].createdAt").description("포인트 기록 시간 (yyyy-MM-dd'T'HH:mm:ss"),
                fieldWithPath("totalPage").description("총 페이지 개수"),
                fieldWithPath("pageSize").description("페이지 사이즈"),
                fieldWithPath("totalElements").description("총 요소 개수")
            )))
            ;

        verify(pointHistoryService, times(1)).getPointHistoryPage(eq(1L), any());
    }

    // update, PUT 메소드 이지만 실상은 새로운 포인트 내역을 생성하는것..
    @Test
    @WithCustomMockUser(loginId = "123", role = "ROLE_MEMBER", customerId = 1L)
    void updatePointHistoryTest() throws Exception{
        when(pointHistoryService.createPointHistory(anyLong(), any())).thenReturn(new PointHistoryCreateResponseDto(1L, 5000L));

        mvc.perform(MockMvcRequestBuilders.put("/api/shop/point")
                .header(HttpHeaders.AUTHORIZATION, TEST_AUTHORIZATION)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                      "pointSourceName": "회원가입",
                      "pointOffset": 5000
                    }
                    """)
            )
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.remainingPoint").value(5000L))
            .andDo(customDocs("point-history-create", SNIPPET_AUTH_HEADER, requestFields(
                fieldWithPath("pointSourceName").description("포인트 출처 이름. 반드시 DB에 정의되어 있는 이름이어야 함."),
                fieldWithPath("pointOffset").description("포인트 변동 내역. 음수도 가능")
            ), responseFields(
                fieldWithPath("customerId").description("포인트가 변동된 고객의 고유 ID"),
                fieldWithPath("remainingPoint").description("포인트가 변동된 이후 현재 고객이 보유하고 있는 포인트")
            )));

        verify(pointHistoryService, times(1)).createPointHistory(eq(1L), any());
    }
}
