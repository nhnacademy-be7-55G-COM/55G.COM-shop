package shop.S5G.shop.service.book;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.entity.bookcategory.BookCategory;
import shop.S5G.shop.entity.bookcategory.BookCategoryId;
import shop.S5G.shop.entity.Category;
import shop.S5G.shop.exception.bookcategory.BookCategoryAlreadyExistsException;
import shop.S5G.shop.exception.bookcategory.BookCategoryBadRequestException;
import shop.S5G.shop.repository.book.BookRepository;
import shop.S5G.shop.repository.CategoryRepository;
import shop.S5G.shop.repository.bookcategory.BookCategoryRepository;
import shop.S5G.shop.service.bookcategory.impl.BookCategoryServiceImpl;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatNoException;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookCategoryServiceTest {

    @Mock
    private BookCategoryRepository bookCategoryRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookCategoryServiceImpl bookCategoryService;


    /**
     * 도서 내 카테고리 등록 test
     */
    @Test
    void addBookCategoryTest() {
        //given

        Book b = mock(Book.class);
        when(bookRepository.findById(eq(1L))).thenReturn(Optional.of(b));

        Category c = mock(Category.class);
        when(categoryRepository.findById(eq(1L))).thenReturn(Optional.of(c));

        BookCategory mockBc1 = new BookCategory(new BookCategoryId(1L, 1L), c, b);

        BookCategory bookCategory = mock(BookCategory.class);
        //bookId == 1L, categoryId == 1L 인 bookCategory가 존재한다고 가정할 때
        when(bookCategoryRepository.findById(mockBc1.getId())).thenReturn(Optional.of(bookCategory));

        //when
        //중복 테스트
        assertThatThrownBy(() -> bookCategoryService.addCategoryInBook(1L, 1L)).isInstanceOf(BookCategoryAlreadyExistsException.class);

        //then
        verify(bookCategoryRepository, times(1)).findById(mockBc1.getId());
        verify(bookCategoryRepository, never()).save(bookCategory);

        //bookId 범위 테스트
        assertThatThrownBy(()-> bookCategoryService.addCategoryInBook(-1L, 1L)).isInstanceOf(BookCategoryBadRequestException.class);

        //categoryId 범위 테스트
        assertThatThrownBy(()-> bookCategoryService.addCategoryInBook(1L, -1L)).isInstanceOf(BookCategoryBadRequestException.class);
    }

    /**
     * 도서 내 카테고리 조회 test
     */
    @Test
    void getCategoryInBookTest() {
        //bookId 범위 테스트
        assertThatThrownBy(() -> bookCategoryService.getCategoryInBook(-1L)).isInstanceOf(BookCategoryBadRequestException.class);
    }


    /**
     * 도서 내 카테고리 삭제 test
     */
    @Test
    void deleteCategoryTest() {
        //bookId 범위 테스트
        assertThatThrownBy(()-> bookCategoryService.addCategoryInBook(-1L, 1L)).isInstanceOf(BookCategoryBadRequestException.class);

        //categoryId 범위 테스트
        assertThatThrownBy(() -> bookCategoryService.addCategoryInBook(1L, -1L)).isInstanceOf(BookCategoryBadRequestException.class);

        //정상동작 확인
        Book b = new Book();
        b.setBookId(1L);
        Category c = new Category();
        c.setCategoryId(1L);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(b));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(c));
        assertThatNoException().isThrownBy(()-> bookCategoryService.deleteCategory(1L, 1L));

    }
}