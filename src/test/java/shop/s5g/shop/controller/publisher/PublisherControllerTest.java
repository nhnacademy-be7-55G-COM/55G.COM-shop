package shop.s5g.shop.controller.publisher;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.given;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.dto.publisher.PublisherResponseDto;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.publisher.PublisherService;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(
    value = PublisherController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import(TestSecurityConfig.class)
class PublisherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublisherService publisherService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 출판사 등록 test
     */
    @Test
    @DisplayName("출판사 등록 test")
    void addPublisherTest() throws Exception {
        this.mockMvc
                .perform(post("/api/shop/publisher")
                        .content("{\"name\": \"한빛\", \"active\": true}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    /**
     * 출판사 등록 실패 test
     */
    @Test
    @DisplayName("출판사 등록 실패 test")
    void addPublisherErrorTest() throws Exception {
        this.mockMvc
                .perform(post("/api/shop/publisher")
                        .content("{\"name\": \"\", \"active\": true}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * 출판사 조회 test
     */
    @Test
    @DisplayName("출판사 조회 test")
    void getPublisherTest() throws Exception {
        this.mockMvc
                .perform(get("/api/shop/publisher/{publisherId}", 1))
                .andExpect(status().isOk())
                .andDo(print());
    }

    /**
     * 출판사 전체 조회 test
     */
    @Test
    @DisplayName("출판사 전체 조회 test")
    void getAllPublisherTest() throws Exception {
        //given
        List<PublisherResponseDto> publishers = new ArrayList<>();

        PublisherResponseDto p1 = new PublisherResponseDto(1L, "창비", true);
        PublisherResponseDto p2 = new PublisherResponseDto(2L, "한빛", true);

        publishers.add(p1);
        publishers.add(p2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<PublisherResponseDto> page = new PageImpl<>(publishers, pageable, publishers.size());

        given(publisherService.getAllPublisher(pageable)).willReturn(page);

        //when
        mockMvc.perform(get("/api/shop/publisher")
                .param("page", "0")
                .param("size", "10")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("창비"))
                .andExpect(jsonPath("$.content[1].name").value("한빛"));

        //then
        verify(publisherService).getAllPublisher(pageable);
    }

    /**
     * 출판사 수정 test
     */
    @Test
    @DisplayName("출판사 수정 test")
    void updatePublisherTest() throws Exception {
        this.mockMvc
                .perform(put("/api/shop/publisher/{publisherId}", 1)
                        .content("{\"name\": \"창비\", \"active\": true}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    /**
     * 출판사 수정 잘못된 입력 실패 test
     */
    @Test
    @DisplayName("출판사 수정 실패 test")
    void updatePublisherErrorTest() throws Exception {
        this.mockMvc
                .perform(put("/api/shop/publisher/{publisherId}", 1)
                        .content("{\"name\": \"\", \"active\": true}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * 출판사 수정 아이디 오류 실패 test
     */
    @Test
    @DisplayName("출판사 수정 실패 test")
    void updatePublisherIdErrorTest() throws Exception {
        this.mockMvc
                .perform(put("/api/shop/publisher/{publisherId}", -1)
                        .content("{\"name\": \"창비\", \"active\": true}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * 출판사 삭제 test
     */
    @Test
    @DisplayName("출판사 삭제 test")
    void deletePublisherTest() throws Exception {
        this.mockMvc
                .perform(delete("/api/shop/publisher/{publisherId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    /**
     * 출판사 삭제 test
     */
    @Test
    @DisplayName("출판사 삭제 test")
    void deletePublisherIdErrorTest() throws Exception {
        this.mockMvc
                .perform(delete("/api/shop/publisher/{publisherId}", -1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}