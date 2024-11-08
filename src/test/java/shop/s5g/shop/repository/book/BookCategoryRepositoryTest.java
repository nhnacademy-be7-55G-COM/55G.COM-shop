package shop.s5g.shop.repository.book;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import shop.s5g.shop.dto.bookcategory.BookCategoryResponseDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.BookStatus;
import shop.s5g.shop.entity.Category;
import shop.s5g.shop.entity.Publisher;
import shop.s5g.shop.entity.bookcategory.BookCategory;
import shop.s5g.shop.entity.bookcategory.BookCategoryId;
import shop.s5g.shop.repository.bookcategory.BookCategoryRepository;
import shop.s5g.shop.repository.bookstatus.BookStatusRepository;
import shop.s5g.shop.repository.category.CategoryRepository;
import shop.s5g.shop.repository.publisher.PublisherRepository;

@DataJpaTest
class BookCategoryRepositoryTest {

    private final BookCategoryRepository bookCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    @Autowired
    private PublisherRepository publisherRepository;
    @Autowired
    private BookStatusRepository bookStatusRepository;

    @Autowired
    public BookCategoryRepositoryTest(BookCategoryRepository bookCategoryRepository, CategoryRepository categoryRepository, BookRepository bookRepository) {
        this.bookCategoryRepository = bookCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * 도서 내 카테고리 등록 test
     */
    @Test
    @DisplayName("도서 내 카테고리 등록 test")
    void addBookCategoryTest() {
        Publisher publisher = new Publisher();
        BookStatus bookStatus = new BookStatus();

        Book book1 = new Book(
                publisher,
                bookStatus,
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

        Category category1 = new Category( null, "컴퓨터", true);

        BookCategory bookCategory = new BookCategory(new BookCategoryId(book1.getBookId(), category1.getCategoryId()), category1, book1);

        bookCategoryRepository.save(bookCategory);
        Assertions.assertEquals(1, bookCategoryRepository.count());
    }

    /**
     * 도서 내 카테고리 조회 test
     */
    @Test
    @DisplayName("도서 내 카테고리 조회 test")
    void getBookCategoryTest() {
        Publisher publisher = new Publisher();
        BookStatus bookStatus = new BookStatus();

        publisherRepository.save(publisher);
        bookStatusRepository.save(bookStatus);

        Book book1 = new Book(
                publisher,
                bookStatus,
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

        Category category1 = new Category( null, "컴퓨터", true);

        Book saveBook = bookRepository.save(book1);
        Category saveCategory = categoryRepository.save(category1);

        BookCategory bookCategory = new BookCategory(new BookCategoryId(saveBook.getBookId(), saveCategory.getCategoryId()), saveCategory, saveBook);
        BookCategory save = bookCategoryRepository.save(bookCategory);

        List<BookCategoryResponseDto> categoryByBookId = bookCategoryRepository.findCategoryByBookId(saveBook.getBookId());
        Assertions.assertEquals(category1.getCategoryId(), categoryByBookId.getFirst().categoryId());
    }

    /**
     * 도서카테고리 삭제 test
     */
    @Test
    void deleteBookCategoryTest() {
        Publisher publisher = new Publisher();
        BookStatus bookStatus = new BookStatus();

        publisherRepository.save(publisher);
        bookStatusRepository.save(bookStatus);

        Book book1 = new Book(
                publisher,
                bookStatus,
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
        Category category1 = new Category(null, "컴퓨터", true);

        Book saveBook = bookRepository.save(book1);
        Category saveCategory = categoryRepository.save(category1);

        BookCategory bookCategory = new BookCategory(new BookCategoryId(saveBook.getBookId(), saveCategory.getCategoryId()), saveCategory, saveBook);
        BookCategory save = bookCategoryRepository.save(bookCategory);

        bookCategoryRepository.delete(bookCategory);
        Assertions.assertEquals(0, bookCategoryRepository.count());
    }
}
