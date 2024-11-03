package shop.S5G.shop.controller.tag;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shop.S5G.shop.controller.book.TagController;
import shop.S5G.shop.dto.tag.TagRequestDto;
import shop.S5G.shop.service.tag.impl.TagServiceImpl;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TagController.class)
class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagServiceImpl tagServiceImpl;

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
                .andDo(print());
        verify(tagServiceImpl, times(1)).createtag(any());
    }

    /**
     * 태그 등록 실패 test
     * @throws Exception
     */
    @Test
    void testGetTagsError() throws Exception {
        TagRequestDto tagRequestDto = new TagRequestDto("베스트셀러", true);

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
        this.mockMvc
                .perform(get("/api/shop/tag"))
                .andExpect(status().isOk())
                .andDo(print());
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
     * 태그 수정 실패 test
     */
    @Test
    void updateTagErrorTest() throws Exception {
        this.mockMvc
                .perform(put("/api/shop/tag/{tagId}", 1)
                        .content("{\"publisherName\": \"베스트셀러\", \"active\": true}")
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
}
