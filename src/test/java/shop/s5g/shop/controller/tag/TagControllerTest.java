package shop.s5g.shop.controller.tag;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import shop.s5g.shop.controller.book.TagController;
import shop.s5g.shop.dto.tag.TagRequestDto;
import shop.s5g.shop.dto.tag.TagResponseDto;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.tag.TagService;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(
    value = TagController.class,
    excludeFilters = @ComponentScan.Filter(
        type= FilterType.ASSIGNABLE_TYPE,
        classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
    )
)
@Import(TestSecurityConfig.class)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagService tagService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 태그 등록 test
     */
    @Test
    @DisplayName("태그 등록 test")
    void testGetTags() throws Exception {
        TagRequestDto tagRequestDto = new TagRequestDto("서울 출판사");

        this.mockMvc
                .perform(post("/api/shop/tag")
                        .content(objectMapper.writeValueAsString(tagRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
        verify(tagService, times(1)).createtag(any());
    }

    /**
     * 태그 등록 실패 test
     * @throws Exception
     */
    @Test
    void testGetTagsError() throws Exception {
        TagRequestDto tagRequestDto = new TagRequestDto("베스트셀러");

        this.mockMvc
                .perform(post("/api/shop/tag")
                        .content("{ \"tagName\":\"\", \"active\":true}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * 태그 목록 조회 test
     */
    @Test
    void getTagListTest() throws Exception {
        List<TagResponseDto> tags = new ArrayList<>();

        TagResponseDto t1 = new TagResponseDto(1L, "베스트셀러", true);
        TagResponseDto t2 = new TagResponseDto(2L, "이달의도서", true);

        tags.add(t1);
        tags.add(t2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<TagResponseDto> page = new PageImpl<>(tags, pageable, tags.size());

        given(tagService.allTag(pageable)).willReturn(page);

        mockMvc
                .perform(get("/api/shop/tag")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].tagName").value("베스트셀러"))
                .andExpect(jsonPath("$.content[1].tagName").value("이달의도서"));

        verify(tagService).allTag(pageable);
    }

    /**
     * 태그 수정 test
     */
    @Test
    void updateTagTest() throws Exception {
        this.mockMvc
                .perform(put("/api/shop/tag/{tagId}", 1)
                        .content("{\"tagName\": \"베스트셀러\", \"active\": true}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    /**
     * 태그 수정 잘못된 입력 실패 test
     */
    @Test
    void updateTagBadRequestErrorTest() throws Exception {
        this.mockMvc
                .perform(put("/api/shop/tag/{tagId}", 1)
                        .content("{\"tagName\": \"\", \"active\": true}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * 태그 수정 id 실패 test
     */
    @Test
    void updateTagIdErrorTest() throws Exception {
        this.mockMvc
                .perform(put("/api/shop/tag/{tagId}", -1)
                        .content("{\"tagName\": \"베스트셀러\", \"active\": true}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * 태그 삭제 test
     */
    @Test
    @DisplayName("태그 삭제 test")
    void deleteTagTest() throws Exception {
        this.mockMvc
                .perform(delete("/api/shop/tag/{tagId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    /**
     * 태그 삭제 id 실패 test
     */
    @Test
    @DisplayName("태그 삭제 test")
    void deleteTagIdErrorTest() throws Exception {
        this.mockMvc
                .perform(delete("/api/shop/tag/{tagId}", -1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }
}
