package shop.s5g.shop.service.category;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import shop.s5g.shop.dto.category.CategoryRequestDto;
import shop.s5g.shop.dto.category.CategoryResponseDto;
import shop.s5g.shop.entity.Category;
import shop.s5g.shop.exception.category.CategoryResourceNotFoundException;
import shop.s5g.shop.repository.category.CategoryRepository;
import shop.s5g.shop.service.category.impl.CategoryServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl categoryService;

    /**
     * 카테고리 등록 test
     */
    @Test
    @DisplayName("카테고리 등록 test")
    void createCategoryTest() {
        //given
        Long parentCategoryId = 1L;
        Category parentCategory = new Category(null, "parent Category", true);
        CategoryRequestDto categoryDto = new CategoryRequestDto("Child Category", parentCategoryId);

        when(categoryRepository.findById(parentCategoryId)).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(new Category(parentCategory, "Child Category", true));

        //when
        categoryService.createCategory(categoryDto);

        //then
        verify(categoryRepository).findById(parentCategoryId);
    }

    /**
     * 카테고리 등록 실패
     * 부모 카테고리 없음
     */
    @Test
    @DisplayName("부모 카테고리 없음")
    void createCategoryErrorTest() {
        //given
        Long parentCategoryId = 1L;
        CategoryRequestDto categoryDto = new CategoryRequestDto("Child Category", parentCategoryId);

        when(categoryRepository.findById(parentCategoryId)).thenReturn(Optional.empty());

        //when
        Exception exception = assertThrows(CategoryResourceNotFoundException.class,
                () -> categoryService.createCategory(categoryDto));

        assertEquals("카테고리가 존재하지 않습니다.", exception.getMessage());

        //then
        verify(categoryRepository).findById(parentCategoryId);
    }

    /**
     * 모든 카테고리 조회
     */
    @Test
    @DisplayName("모든 카테고리 조회")
    void allCategoryTest() {
        //given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("categoryName").ascending());
        CategoryResponseDto c1 = new CategoryResponseDto(1L, null, "Parent Category", true);
        CategoryResponseDto c2 = new CategoryResponseDto(2L, 1L, "Child Category", true);

        List<CategoryResponseDto> categories = new ArrayList<>();
        categories.add(c1);
        categories.add(c2);

        Page<CategoryResponseDto> mockPage = new PageImpl<>(categories, pageable, categories.size());

        when(categoryRepository.getAllCategory(pageable)).thenReturn(mockPage);

        //when
        Page<CategoryResponseDto> result = categoryService.allCategory(pageable);

        //then
        assertEquals("Parent Category", result.getContent().get(0).categoryName());
        assertEquals("Child Category", result.getContent().get(1).categoryName());
        verify(categoryRepository, times(1)).getAllCategory(pageable);
    }

    /**
     * 자식 카테고리 조회
     */
    @Test
    @DisplayName("자식 카테고리 조회 test")
    void getChildCategoryTest() {
        //given
        long parentCategoryId = 1L;
        CategoryResponseDto c1 = new CategoryResponseDto(2L, 1L, "Child Category1", true);
        CategoryResponseDto c2 = new CategoryResponseDto(3L, 1L, "Child Category2", true);
        List<CategoryResponseDto> mockResponse = new ArrayList<>();

        mockResponse.add(c1);
        mockResponse.add(c2);

        when(categoryRepository.getChild_Category(parentCategoryId)).thenReturn(mockResponse);

        //when
        List<CategoryResponseDto> result = categoryService.getChildCategory(parentCategoryId);

        //then
        assertEquals("Child Category1", result.get(0).categoryName());
        assertEquals("Child Category2", result.get(1).categoryName());
    }

    /**
     * 국내도서 하위 카테고리 조회 test
     */
    @Test
    @DisplayName("국내도서 하위 카테고리 조회 test")
    void getKoreaBooks() {
        //given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("categoryName").ascending());

        CategoryResponseDto c1 = new CategoryResponseDto(2L, null, "category1", true);
        CategoryResponseDto c2 = new CategoryResponseDto(3L, null, "category2", true);

        List<CategoryResponseDto> categories = new ArrayList<>();
        categories.add(c1);
        categories.add(c2);
        Page<CategoryResponseDto> mockPage = new PageImpl<>(categories, pageable, categories.size());

        when(categoryRepository.getKoreaBook(pageable)).thenReturn(mockPage);

        //when
        Page<CategoryResponseDto> result = categoryService.getKoreaBooks(pageable);

        //then
        assertEquals("category1", result.getContent().get(0).categoryName());
        assertEquals("category2", result.getContent().get(1).categoryName());
        verify(categoryRepository, times(1)).getKoreaBook(pageable);
    }
    /**
     * 국내도서 하위 카테고리 실패 테스트
     */
    @Test
    @DisplayName("국내도서 하위 카테고리 조회 실패 테스트 - 빈 결과")
    void getKoreaBooksEmptyResultTest() {
        // Given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("categoryName").ascending());

        Page<CategoryResponseDto> mockPage = new PageImpl<>(List.of(), pageable, 0);

        when(categoryRepository.getKoreaBook(pageable)).thenReturn(mockPage);

        // When
        Page<CategoryResponseDto> result = categoryService.getKoreaBooks(pageable);

        // Then
        assertEquals(0, result.getTotalElements());
        verify(categoryRepository, times(1)).getKoreaBook(pageable);
    }

    /**
     * 카테고리 삭제 test
     */
    @Test
    void deleteCategory() {
        //categoryId가 1L인 Category인스턴스가 존재한다고 가정할 때
        when(categoryRepository.existsById(1L)).thenReturn(false);
        assertThatThrownBy(() -> categoryService.deleteCategory(1L)).isInstanceOf(CategoryResourceNotFoundException.class);
        verify(categoryRepository, times(1)).existsById(1L);
        verify(categoryRepository, never()).findById((1L));
    }
}
