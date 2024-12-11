package shop.s5g.shop.controller.author;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.dto.author.AllAuthorResponseDto;
import shop.s5g.shop.dto.author.AuthorRequestDto;
import shop.s5g.shop.dto.author.AuthorResponseDto;
import shop.s5g.shop.exception.author.AuthorBadRequestException;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.author.AuthorService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        value = AuthorController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
        )
)
@Import(TestSecurityConfig.class)
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 작가 등록 test
     */
    @Test
    void addAuthorTest() throws Exception {
        // Given
        AuthorRequestDto authorRequestDto = new AuthorRequestDto("한강");
        String content = """
                {
                  "name": "한강"
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/shop/author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("작가 등록 성공"))
                .andDo(print());

        verify(authorService, times(1)).createAuthor(authorRequestDto);
    }

    /**
     * 작가 등록 실패 test
     * @throws Exception
     */
    @Test
    void addAuthorValidationFailTest() throws Exception {
        // Given
        String content = """
                {
                  "name": "",
                }
                """;

        // When & Then
        mockMvc.perform(post("/api/shop/author")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andDo(print());

        verify(authorService, never()).createAuthor(any());
    }

    /**
     * 모든 작가 조회 test
     */
    @Test
    void getAllAuthorsSuccessTest() throws Exception {
        // Given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());

        AllAuthorResponseDto author1 = new AllAuthorResponseDto(
                1L, // authorId
                "J.K. Rowling", // name
                true // active
        );

        AllAuthorResponseDto author2 = new AllAuthorResponseDto(
                2L, // authorId
                "George R.R. Martin", // name
                true // active
        );

        List<AllAuthorResponseDto> authors = List.of(author1, author2);
        Page<AllAuthorResponseDto> mockPage = new PageImpl<>(authors, pageable, authors.size());

        when(authorService.allAuthor(any(Pageable.class))).thenReturn(mockPage);

        // When & Then
        mockMvc.perform(get("/api/shop/author")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "name,asc")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("J.K. Rowling"))
                .andExpect(jsonPath("$.content[1].name").value("George R.R. Martin"))
                .andDo(print());

        verify(authorService, times(1)).allAuthor(any(Pageable.class));
    }

    /**
     * 작가 id로 작가 조회 test
     */
    @Test
    void findAuthorSuccessTest() throws Exception {
        // Given
        long authorId = 1L;
        AuthorResponseDto authorResponseDto = new AuthorResponseDto(
                authorId,
                "한강",
                true
        );

        when(authorService.getAuthor(authorId)).thenReturn(authorResponseDto);

        // When & Then
        mockMvc.perform(get("/api/shop/author/{authorId}", authorId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authorId").value(authorId))
                .andExpect(jsonPath("$.name").value("한강"))
                .andExpect(jsonPath("$.active").value(true))
                .andDo(print());

        verify(authorService, times(1)).getAuthor(authorId);
    }

    /**
     * 작가 수정 test
     */
    @Test
    void updateAuthorSuccessTest() throws Exception {
        // Given
        long authorId = 1L;
        AuthorRequestDto authorRequestDto = new AuthorRequestDto("한강");
        String content = """
                {
                  "name": "한강"
                }
                """;

        // When & Then
        mockMvc.perform(put("/api/shop/author/{authorId}", authorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("작가 수정 성공"))
                .andDo(print());

        verify(authorService, times(1)).updateAuthor(authorId, authorRequestDto);
    }

    /**
     * 작가 수정 실패 test
     * 잘못된 작가 id
     */
    @Test
    void updateAuthorInvalidIdTest() throws Exception {
        // Given
        long authorId = 0L;
        String content = """
                {
                  "name": "Updated Author"
                }
                """;

        // When & Then
        mockMvc.perform(put("/api/shop/author/{authorId}", authorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof AuthorBadRequestException))
                .andExpect(result ->
                        assertEquals("작가id는 1보다 커야 합니다.", result.getResolvedException().getMessage()))
                .andDo(print());

        verify(authorService, never()).updateAuthor(anyLong(), any());
    }

    /**
     * 작가 수정 실패 test
     * 잘못된 입력
     */
    @Test
    void updateAuthorInvalidInputTest() throws Exception {
        // Given
        long authorId = 1L;
        String content = """
                {
                  "name": ""
                }
                """;

        // When & Then
        mockMvc.perform(put("/api/shop/author/{authorId}", authorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof AuthorBadRequestException))
                .andExpect(result ->
                        assertEquals("잘못된 입력입니다.", result.getResolvedException().getMessage()))
                .andDo(print());

        verify(authorService, never()).updateAuthor(anyLong(), any());
    }

    /**
     * 작가 삭제(비활성화) test
     */
    @Test
    void deleteAuthorSuccessTest() throws Exception {
        // Given
        long authorId = 1L;

        // When & Then
        mockMvc.perform(delete("/api/shop/author/{authorId}", authorId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("작가 삭제 성공"))
                .andDo(print());

        verify(authorService, times(1)).deleteAuthor(authorId);
    }

    /**
     * 작가 삭제(비활성화) 실패 test
     * 잘못된 작가 id
     */
    @Test
    void deleteAuthorInvalidIdTest() throws Exception {
        // Given
        long authorId = 0L;

        // When & Then
        mockMvc.perform(delete("/api/shop/author/{authorId}", authorId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result ->
                        assertTrue(result.getResolvedException() instanceof AuthorBadRequestException))
                .andExpect(result ->
                        assertEquals("작가 id는 1보다 커야 합니다.", result.getResolvedException().getMessage()))
                .andDo(print());

        verify(authorService, never()).deleteAuthor(anyLong());
    }

    /**
     * 작가 검색 test
     */
    @Test
    void searchAuthorsSuccessTest() throws Exception {
        // Given
        String keyword = "Rowling";
        List<AuthorResponseDto> authors = List.of(
                new AuthorResponseDto(1L, "J.K. Rowling", true),
                new AuthorResponseDto(2L, "George R.R. Martin", true)
        );

        when(authorService.searchAuthors(keyword)).thenReturn(ResponseEntity.ok(authors));

        // When & Then
        mockMvc.perform(get("/api/shop/authors")
                        .param("keyword", keyword)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].authorId").value(1L))
                .andExpect(jsonPath("$[0].name").value("J.K. Rowling"))
                .andExpect(jsonPath("$[0].active").value(true))
                .andExpect(jsonPath("$[1].authorId").value(2L))
                .andExpect(jsonPath("$[1].name").value("George R.R. Martin"))
                .andExpect(jsonPath("$[1].active").value(true))
                .andDo(print());

        verify(authorService, times(1)).searchAuthors(keyword);
    }
}
