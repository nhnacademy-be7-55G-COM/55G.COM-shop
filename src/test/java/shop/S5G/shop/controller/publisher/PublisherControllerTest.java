package shop.S5G.shop.controller.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shop.S5G.shop.service.publisher.impl.PublisherServiceImpl;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PublisherController.class)
class PublisherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PublisherServiceImpl publisherServiceImpl;

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
     * 출판사 수정 실패 test
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
}