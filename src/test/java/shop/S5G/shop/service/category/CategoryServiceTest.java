package shop.S5G.shop.service.category;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.S5G.shop.dto.category.CategoryRequestDto;
import shop.S5G.shop.entity.Category;
import shop.S5G.shop.exception.CategoryException.CategoryAlreadyExistsException;
import shop.S5G.shop.exception.CategoryException.CategoryResourceNotFoundException;
import shop.S5G.shop.repository.CategoryRepository;
import shop.S5G.shop.service.category.impl.CategoryServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;

    /**
     * 카테고리 등록 test
     */
    @Test
    void addCategory() {
        Category c = new Category();
        CategoryRequestDto mockCategory = new CategoryRequestDto(c, "컴퓨터", true );

        Category category = mock(Category.class);
        when(categoryRepository.findById(eq(1L))).thenReturn(Optional.of(category));
        assertThatThrownBy(() -> categoryServiceImpl.createCategory(mockCategory)).isInstanceOf(CategoryAlreadyExistsException.class);

        verify(categoryRepository, times(1)).findById(eq(1L));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    /**
     * 카테고리 수정 test
     */
//    @Test
//    void updateCategory() {
//        Category mockCategory = mock(Category.class);
//        assertThatThrownBy(()-> categoryService.updateCategory(-1L, mockCategory)).isInstanceOf(CategoryAlreadyExistsException.class);
//    }

    /**
     * 카테고리 삭제 test
     */
    @Test
    void deleteCategory() {
        Category mockCategory = mock(Category.class);
        //categoryId가 1L인 Category인스턴스가 존재한다고 가정할 때
        when(categoryRepository.existsById(1L)).thenReturn(false);
        assertThatThrownBy(() -> categoryServiceImpl.deleteCategory(1L)).isInstanceOf(CategoryResourceNotFoundException.class);
        verify(categoryRepository, times(1)).existsById(1L);
        verify(categoryRepository, never()).findById(eq(1L));
    }
}
