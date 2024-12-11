package shop.s5g.shop.controller.category;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import shop.s5g.shop.config.SecurityConfig;
import shop.s5g.shop.config.TestSecurityConfig;
import shop.s5g.shop.controller.book.CategoryController;
import shop.s5g.shop.dto.category.CategoryResponseDto;
import shop.s5g.shop.filter.JwtAuthenticationFilter;
import shop.s5g.shop.service.category.CategoryService;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        value = CategoryController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
        )
)
@Import(TestSecurityConfig.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    String content = """
            {
                "categoryName": "컴퓨터",
                "parentCategoryId": 222
            }
            """;

    /**
     * 카테고리 등록 test
     */
    @Test
    @DisplayName("카테고리 등록 test")
    void addCategoryTest() throws Exception {
        mockMvc.perform(post("/api/shop/category")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isOk())
                .andDo(print());
        verify(categoryService, times(1)).createCategory(any());
    }

    /**
     * 카테고리 등록 실패 test
     */
    @Test
    @DisplayName("카테고리 등록 실패 test")
    void addCategoryErrorTest() throws Exception {
        this.mockMvc
                //given
                .perform(post("/api/shop/category")
                        .content("""
                                {
                                  "categoryName": "컴퓨터2",
                                  "parentCategoryId": 
                                }
                                """)
                        //when & then
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * 카테고리 목록 조회 test
     */
    @Test
    @DisplayName("카테고리 목록 조회 test")
    void getAllCategoryTest() throws Exception {
        List<CategoryResponseDto> category = new ArrayList<>();

        CategoryResponseDto c1 = new CategoryResponseDto(1L, 5L, "현대문학", true);
        CategoryResponseDto c2 = new CategoryResponseDto(2L, 5L, "고전문학", true);

        category.add(c1);
        category.add(c2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryResponseDto> page = new PageImpl<>(category, pageable, category.size());

        given(categoryService.allCategory(pageable)).willReturn(page);

        mockMvc.perform(get("/api/shop/category")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].categoryName").value("현대문학"))
                .andExpect(jsonPath("$.content[1].categoryName").value("고전문학"));

        verify(categoryService, times(1)).allCategory(pageable);
    }

    /**
     * 자식 카테고리 조회 test
     */
    @Test
    @DisplayName("자식 카테고리 조회 test")
    void getChildCategoryTest() throws Exception {
        List<CategoryResponseDto> mockCategory = new ArrayList<>();

        CategoryResponseDto c1 = new CategoryResponseDto(1L, null, "피아노", true);
        CategoryResponseDto c2 = new CategoryResponseDto(2L, 1L, "기타", true);
        CategoryResponseDto c3 = new CategoryResponseDto(3L, 1L, "드럼", true);

        mockCategory.add(c1);
        mockCategory.add(c2);
        mockCategory.add(c3);

        when(categoryService.getChildCategory(anyLong()))
                .thenAnswer(invocation -> {
                    Long categoryId = invocation.getArgument(0);
                    return mockCategory.stream()
                            .filter(category -> category.parentCategoryId() != null && category.parentCategoryId().equals(categoryId))
                            .toList();
                });

        mockMvc.perform(get("/api/shop/category/childCategory/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].categoryName").value("기타"))
                .andExpect(jsonPath("$.[1].categoryName").value("드럼"));

        verify(categoryService).getChildCategory(anyLong());
    }

    /**
     * 자식 카테고리 조회 실패 test
     */
    @Test
    @DisplayName("자식 카테고리 조회 test")
    void getChildCategoryErrorTest() throws Exception {
        mockMvc.perform(get("/api/shop/category/childCategory/-1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    /**
     * 국내도서 하위 카테고리 조회
     */
    @Test
    @DisplayName("국내도서 하위 카테고리 조회")
    void getKoreaCategoriesTest() throws Exception {
        List<CategoryResponseDto> category = new ArrayList<>();

        CategoryResponseDto c1 = new CategoryResponseDto(1L, null, "현대문학", true);
        CategoryResponseDto c2 = new CategoryResponseDto(2L, null, "고전문학", true);

        category.add(c1);
        category.add(c2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<CategoryResponseDto> page = new PageImpl<>(category, pageable, category.size());

        given(categoryService.getKoreaBooks(pageable)).willReturn(page);

        mockMvc.perform(get("/api/shop/category/korea")
                        .param("page", "0")
                        .param("size", "10")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].categoryName").value("현대문학"))
                .andExpect(jsonPath("$.content[1].categoryName").value("고전문학"));

        verify(categoryService).getKoreaBooks(pageable);
    }

    /**
     * 카테고리 수정 test
     */
    @Test
    @DisplayName("카테고리 수정 test")
    void updateCategoryTest() throws Exception {
        this.mockMvc
                .perform(put("/api/shop/category/1")
                        .content("""
                                {
                                  "categoryName": "컴퓨터"
                                }
                                """)

                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    /**
     * 카테고리 수정 categoryId 실패 test
     */
    @Test
    @DisplayName("카테고리 수정 id 실패 test")
    void updateCategoryIdErrorTest() throws Exception {
        this.mockMvc
                .perform(put("/api/shop/category/-1")
                        .content("""
                                {
                                  "categoryName": "컴퓨터"
                                }
                                """)

                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * 카테고리 수정 badRequest 실패 test
     */
    @Test
    @DisplayName("카테고리 수정 id 실패 test")
    void updateCategoryRequestErrorTest() throws Exception {
        this.mockMvc
                .perform(put("/api/shop/category/1")
                        .content("{\n" +
//                                " \"categoryName\" : \"컴퓨터\"\n" +
                                "}\n")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    /**
     * 카테고리 삭제(비활성화) test
     */
    @Test
    @DisplayName("카테고리 삭제(비활성화) test")
    void deleteCategoryTest() throws Exception {
        this.mockMvc
                .perform(delete("/api/shop/category/{categoryId}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("카테고리 삭제 성공"));

        Mockito.verify(categoryService).deleteCategory(any());
    }
}
