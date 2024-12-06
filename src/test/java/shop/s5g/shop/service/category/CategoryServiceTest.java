package shop.s5g.shop.service.category;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.s5g.shop.exception.category.CategoryResourceNotFoundException;
import shop.s5g.shop.repository.category.CategoryRepository;
import shop.s5g.shop.service.category.impl.CategoryServiceImpl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
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


    /**
     * 카테고리 수정 test
     */


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
