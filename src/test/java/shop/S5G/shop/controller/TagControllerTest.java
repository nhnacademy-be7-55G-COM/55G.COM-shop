package shop.S5G.shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shop.S5G.shop.dto.tag.TagRequestDto;
import shop.S5G.shop.exception.TagException.TagBadRequestException;
import shop.S5G.shop.service.TagService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TagController.class)
public class TagControllerTest {

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
        TagRequestDto tagRequestDto = new TagRequestDto("서울 출판사", true);

        this.mockMvc
                .perform(post("/api/shop/tag")
                        .content(objectMapper.writeValueAsString(tagRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("success"))
                .andDo(print());
        verify(tagService, times(1)).createtag(any());
    }

    /**
     * 태그 등록 실패 test
     * @throws Exception
     */
    @Test
    void testGetTagsError() throws Exception {
        TagRequestDto tagRequestDto = new TagRequestDto();
        tagRequestDto.setActive(true);

        this.mockMvc
                .perform(post("/api/shop/tag")
                        .content(objectMapper.writeValueAsString(tagRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertThat(result.getResolvedException())
                                .isInstanceOf(TagBadRequestException.class)
                                .hasMessage("잘못된 입력입니다.")
                );
    }

    /**
     * 태그 목록 조회 test
     */
    @Test
    void getTagListTest() throws Exception {
        this.mockMvc
                .perform(get("/api/shop/tag"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    /**
     * 태그 수정 test
     */
//    @Test
//    void updateTagTest() throws Exception {
//
//        this.mockMvc
//                .perform(put("/api/shop/tag/0")
//                        .content("{\"publisherName\": \"asdda\", \"active\": true}")
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().string(equalTo("success")))
//                .andDo(print());
//        verify(tagService, times(1)).updateTag(eq(2L), Mockito.any(Tag.class));
//    }

    /**
     * 태그 수정 실패 test
     */
//    @Test
//    void updateTagErrorTest() {
//        Long invalidTagId = 0L;
//        TagRequestDto tagRequestDto = new TagRequestDto();
//        tagRequestDto.setTagName("수정된 태그 이름");
//        tagRequestDto.setActive(true);
//
//        // When & Then
//        mockMvc.perform(put("/api/tag/{tagId}", invalidTagId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(tagRequestDto)))
//                .andExpect(status().isBadRequest())
//                .andExpect(result -> {
//                    Exception resolvedException = result.getResolvedException();
//                    assertThat(resolvedException).isInstanceOf(TagBadRequestException.class);
//                    assertThat(resolvedException.getMessage()).isEqualTo("tagId must be grater than 0.");
//                });
//    }

    /**
     * 태그 삭제 test
     */
    @Test
    @DisplayName("태그 삭제 test")
    void deleteTagTest() throws Exception {
        this.mockMvc
                .perform(delete("/api/shop/tag/{tagId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }
    /**
     * 태그 삭제 실패 test
     */

}
