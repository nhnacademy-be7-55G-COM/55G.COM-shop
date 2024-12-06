package shop.s5g.shop.repository.book;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import shop.s5g.shop.config.TestQueryFactoryConfig;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.BookStatus;
import shop.s5g.shop.entity.Category;
import shop.s5g.shop.entity.Publisher;
import shop.s5g.shop.entity.book.category.BookCategory;
import shop.s5g.shop.entity.book.category.BookCategoryId;
import shop.s5g.shop.repository.book.category.BookCategoryRepository;
import shop.s5g.shop.repository.book.status.BookStatusRepository;
import shop.s5g.shop.repository.category.CategoryRepository;
import shop.s5g.shop.repository.publisher.PublisherRepository;

@DataJpaTest
@Import(TestQueryFactoryConfig.class)
class BookCategoryRepositoryTest {

    private final BookCategoryRepository bookCategoryRepository;
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;
    private final PublisherRepository publisherRepository;
    private final BookStatusRepository bookStatusRepository;

    @Autowired
    public BookCategoryRepositoryTest(BookCategoryRepository bookCategoryRepository, CategoryRepository categoryRepository, BookRepository bookRepository,PublisherRepository publisherRepository,BookStatusRepository bookStatusRepository) {
        this.bookCategoryRepository = bookCategoryRepository;
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
        this.publisherRepository=publisherRepository;
        this.bookStatusRepository=bookStatusRepository;
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
                LocalDate.of(2000, 10, 10),
                "978-3-15-148410-2",
                15000L,
                new BigDecimal("5.5"),
                true,
                200,
                2000L,
                LocalDateTime.of(2010, 5, 5, 15, 30),
                LocalDateTime.of(2010, 5, 5, 15, 30)
        );

        Category category1 = new Category( null, "컴퓨터", true);

        BookCategory bookCategory = new BookCategory(new BookCategoryId(book1.getBookId(), category1.getCategoryId()), category1, book1,LocalDateTime.of(2010, 5, 5, 15, 30),LocalDateTime.of(2010, 5, 5, 15, 30));

        bookCategoryRepository.save(bookCategory);
        Assertions.assertEquals(1, bookCategoryRepository.count());
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
                LocalDate.of(2000, 10, 10),
                "978-3-15-148410-2",
                15000L,
                new BigDecimal("5.5"),
                true,
                200,
                2000L,
                LocalDateTime.of(2010, 5, 5, 15, 30),
                LocalDateTime.of(2010, 5, 5, 15, 30)
        );
        Category category1 = new Category(null, "컴퓨터", true);

        Book saveBook = bookRepository.save(book1);
        Category saveCategory = categoryRepository.save(category1);

        BookCategory bookCategory = new BookCategory(new BookCategoryId(saveBook.getBookId(), saveCategory.getCategoryId()), saveCategory, saveBook,LocalDateTime.of(2010, 5, 5, 15, 30),LocalDateTime.of(2010, 5, 5, 15, 30));
        BookCategory save = bookCategoryRepository.save(bookCategory);

        bookCategoryRepository.delete(bookCategory);
        Assertions.assertEquals(0, bookCategoryRepository.count());
    }
}
