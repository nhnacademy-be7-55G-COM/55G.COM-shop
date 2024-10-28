package shop.S5G.shop.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.entity.BookCategory.BookCategory;
import shop.S5G.shop.entity.BookCategory.BookCategoryId;
import shop.S5G.shop.entity.Category;
import shop.S5G.shop.exception.BookException.BookResourceNotFoundException;
import shop.S5G.shop.exception.bookcategory.BookCategoryResourceNotFoundException;
import shop.S5G.shop.repository.bookcategory.BookCategoryRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@DataJpaTest
class BookCategoryRepositoryTest {

    private final BookCategoryRepository bookCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    @Autowired
    public BookCategoryRepositoryTest(BookCategoryRepository bookCategoryRepository, CategoryRepository categoryRepository, BookRepository bookRepository) {
        this.bookCategoryRepository = bookCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * 도서카테고리 등록 test
     */
    @Test
    void addBookCategoryTest() {
        Book book1 = new Book(
                1L,
                22L,
                222L,
                "아낌없이 주는 나무",
                "전래동화",
                "이 책은 전래동화 입니다.",
                LocalDateTime.of(2000, 10, 10, 10, 50),
                "978-3-15-148410-2",
                15000L,
                new BigDecimal("5.5"),
                true,
                200,
                2000L,
                LocalDateTime.of(2010, 5, 5, 15, 30)
        );

        Category category1 = new Category(1L, null, "컴퓨터", true);

        Book save = bookRepository.save(book1);
        Category save1 = categoryRepository.save(category1);

        Book book2 = bookRepository.findById(save.getBookId()).orElseThrow(() -> new BookResourceNotFoundException("Book not found"));
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@" +save1.getCategoryId());
        Category category2 = categoryRepository.findById(1L).orElseThrow(() -> new BookCategoryResourceNotFoundException("Book category not found"));

        BookCategory bookCategory = new BookCategory(new BookCategoryId(1L, 1L), category2, book2);

        bookCategoryRepository.save(bookCategory);
        Assertions.assertEquals(1, bookCategoryRepository.count());
    }

    /**
     * 도서카테고리 삭제 test
     */
    @Test
    void deleteBookCategoryTest() {
        Book book1 = new Book(
                3L,
                22L,
                222L,
                "아낌없이 주는 나무",
                "전래동화",
                "이 책은 전래동화 입니다.",
                LocalDateTime.of(2000, 10, 10, 10, 50),
                "978-3-15-148410-2",
                15000L,
                new BigDecimal("5.5"),
                true,
                200,
                2000L,
                LocalDateTime.of(2010, 5, 5, 15, 30)
        );
        Category category1 = new Category(3L, null, "컴퓨터", true);

        Book book = bookRepository.save(book1);
        Category category = categoryRepository.save(category1);

        BookCategory bookCategory1 = new BookCategory(new BookCategoryId(3L, 3L), category, book);

        BookCategory save1 = bookCategoryRepository.save(bookCategory1);
        Assertions.assertEquals(1, bookCategoryRepository.count());

        bookCategoryRepository.delete(save1);
        Assertions.assertEquals(0, bookCategoryRepository.count());
    }
}
