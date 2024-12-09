package shop.s5g.shop.service.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.Category;
import shop.s5g.shop.entity.book.category.BookCategory;
import shop.s5g.shop.exception.book.BookResourceNotFoundException;
import shop.s5g.shop.repository.book.BookRepository;
import shop.s5g.shop.repository.book.category.BookCategoryRepository;
import shop.s5g.shop.repository.category.CategoryRepository;
import shop.s5g.shop.service.book.category.impl.BookCategoryServiceImpl;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookCategoryServiceTest {
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
    @DisplayName("도서 내 카테고리 등록 test")
    void addCategoryInBookTest() {
        //given
        Long bookId = 1L;
        Long categoryId = 2L;

        Book book = new Book();
        Category category = new Category();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        //when
        bookCategoryService.addCategoryInBook(bookId, categoryId);

        //then
        verify(bookRepository, times(1)).findById(bookId);
        verify(categoryRepository, times(1)).findById(categoryId);
        verify(bookCategoryRepository, times(1)).save(any(BookCategory.class));
    }

    /**
     * 도서 내 카테고리 등록 실패 test
     * 잘못된 bookID
     */
    @Test
    @DisplayName("잘못된 bookID")
    void addCategoryInBookErrorTest() {
        //given
        Long bookId = 1L;
        Long categoryId = 2L;

        //when
        Exception exception = assertThrows(BookResourceNotFoundException.class,
                () -> bookCategoryService.addCategoryInBook(bookId, categoryId));

        assertEquals("없는 도서 입니다.", exception.getMessage());
    }
}
