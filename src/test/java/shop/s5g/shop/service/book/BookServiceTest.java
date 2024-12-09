package shop.s5g.shop.service.book;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import shop.s5g.shop.dto.book.BookDetailResponseDto;
import shop.s5g.shop.dto.book.BookPageableResponseDto;
import shop.s5g.shop.dto.book.BookRequestDto;
import shop.s5g.shop.dto.book.author.BookAuthorRequestDto;
import shop.s5g.shop.dto.book.author.BookAuthorResponseDto;
import shop.s5g.shop.dto.book.category.BookDetailCategoryResponseDto;
import shop.s5g.shop.dto.tag.TagResponseDto;
import shop.s5g.shop.entity.*;
import shop.s5g.shop.entity.book.category.BookCategory;
import shop.s5g.shop.entity.booktag.BookTag;
import shop.s5g.shop.exception.book.BookResourceNotFoundException;
import shop.s5g.shop.exception.publisher.PublisherResourceNotFoundException;
import shop.s5g.shop.repository.author.AuthorRepository;
import shop.s5g.shop.repository.author.AuthorTypeRepository;
import shop.s5g.shop.repository.book.BookRepository;
import shop.s5g.shop.repository.book.author.BookAuthorRepository;
import shop.s5g.shop.repository.book.category.BookCategoryRepository;
import shop.s5g.shop.repository.book.image.BookImageRepository;
import shop.s5g.shop.repository.book.status.BookStatusRepository;
import shop.s5g.shop.repository.booktag.BookTagRepository;
import shop.s5g.shop.repository.category.CategoryRepository;
import shop.s5g.shop.repository.publisher.PublisherRepository;
import shop.s5g.shop.repository.tag.TagRepository;
import shop.s5g.shop.service.book.impl.BookServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {
    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private BookStatusRepository statusRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookCategoryRepository bookCategoryRepository;

    @Mock
    private BookImageRepository bookImageRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private BookTagRepository bookTagRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private AuthorTypeRepository authorTypeRepository;

    @Mock
    private BookAuthorRepository bookAuthorRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    /**
     * 도서 등록 test
     */
    @Test
    @DisplayName("도서 등록 성공 테스트")
    void createBookSuccessTest() {
        // Given
        BookRequestDto requestDto = new BookRequestDto(
                1L,  // publisherId
                1L,  // bookStatusId
                1L,  // categoryId
                "Sample Book", // title
                "Chapter 1", // chapter
                "This is a sample book description.", // description
                "2023-10-01", // publishedDate
                "978-3-16-148410-0", // isbn
                15000L, // price
                new BigDecimal("0.15"), // discountRate
                true, // isPacked
                50, // stock
                "path/to/image.jpg", // thumbnailPath
                List.of(1L, 2L), // tagIdList
                List.of(new BookAuthorRequestDto(1L, 1L)) // authorList
        );

        Publisher publisher = new Publisher("Sample Publisher", true);
        BookStatus bookStatus = new BookStatus("Available");
        Category category = new Category(null, "Sample Category", true);
        Book book = new Book();

        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
        when(statusRepository.findById(1L)).thenReturn(Optional.of(bookStatus));
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(tagRepository.findById(anyLong())).thenReturn(Optional.of(new Tag("Sample Tag")));
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(new Author("Author Name", true)));
        when(authorTypeRepository.findById(anyLong())).thenReturn(Optional.of(new AuthorType("Main Author", true)));

        // When
        bookService.createBook(requestDto);

        // Then
        verify(publisherRepository).findById(1L);
        verify(statusRepository).findById(1L);
        verify(bookRepository).save(any(Book.class));
        verify(categoryRepository).findById(1L);
        verify(bookCategoryRepository).save(any(BookCategory.class));
        verify(tagRepository, times(2)).findById(anyLong());
        verify(bookTagRepository, times(2)).save(any(BookTag.class));
        verify(authorRepository).findById(1L);
        verify(authorTypeRepository).findById(1L);
        verify(bookAuthorRepository).save(any(BookAuthor.class));
    }

    /**
     * 도서 등록 실패
     * 출판사 없음
     */
    @Test
    @DisplayName("출판사 없음")
    void createBookPublisherNotFoundTest() {
        // Given
        BookRequestDto requestDto = new BookRequestDto(
                1L, 1L, 1L, "Sample Book", "Chapter 1", "Description",
                "2023-10-01", "978-3-16-148410-0", 15000L, new BigDecimal("0.15"),
                true, 50, "path/to/image.jpg", List.of(1L), List.of()
        );

        when(publisherRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(PublisherResourceNotFoundException.class, () -> bookService.createBook(requestDto));
        verify(publisherRepository).findById(1L);
        verifyNoInteractions(statusRepository, bookRepository, categoryRepository);
    }

    /**
     * 도서 상세 조회 test
     */
    @Test
    @DisplayName("도서 전체 조회 페이징 테스트")
    void allBookPageableTest() {
        // Given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("title").ascending());

        BookPageableResponseDto b1 = new BookPageableResponseDto(
                1L,                         // bookId
                1L,                         // publisherId
                1L,                         // bookStatusId
                "도서1",                // title
                "챕터1",                // chapter
                "도서1의 설명 입니다.", // description
                LocalDate.of(2023, 10, 1),      // publishedDate
                "978-3-16-148410-0",            // isbn
                15000L,                         // price
                new BigDecimal("0.15"),         // discountRate
                true,                           // isPacked
                50,                             // stock
                1000L,                          // views
                LocalDateTime.of(2023, 10, 15, 12, 34, 56), // createdAt
                "sample-image1.jpg"              // imageName
        );

        BookPageableResponseDto b2 = new BookPageableResponseDto(
                2L,                         // bookId
                2L,                         // publisherId
                2L,                         // bookStatusId
                "도서2",                // title
                "챕터2",                // chapter
                "도서2의 설명 입니다.", // description
                LocalDate.of(2023, 10, 2),      // publishedDate
                "978-3-16-148410-1",            // isbn
                20000L,                         // price
                new BigDecimal("0.10"),         // discountRate
                true,                           // isPacked
                40,                             // stock
                500L,                           // views
                LocalDateTime.of(2023, 10, 20, 10, 20, 30), // createdAt
                "sample-image2.jpg"              // imageName
        );

        List<BookPageableResponseDto> books = List.of(b1, b2);

        Page<BookPageableResponseDto> mockPage = new PageImpl<>(books, pageable, books.size());

        when(bookRepository.findAllBookPage(pageable)).thenReturn(mockPage);

        // When
        Page<BookPageableResponseDto> result = bookService.allBookPageable(pageable);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(1L, result.getContent().get(0).bookId());
        assertEquals("도서1", result.getContent().get(0).title());
        assertEquals("도서2", result.getContent().get(1).title());
        verify(bookRepository, times(1)).findAllBookPage(pageable);
    }

    /**
     * 도서 상세조회 test
     */
    @Test
    @DisplayName("도서 상세 조회 성공 테스트")
    void getBookByIdSuccessTest() {
        // Given
        Long bookId = 1L;

        BookDetailResponseDto mockResponse = new BookDetailResponseDto(
                bookId,
                "Sample Publisher",
                "Available",
                "Sample Title",
                "Chapter 1",
                "This is a sample book description.",
                LocalDate.of(2023, 10, 1),
                "978-3-16-148410-0",
                15000L,
                new BigDecimal("0.15"),
                true,
                50,
                1000L,
                LocalDateTime.of(2023, 10, 15, 12, 34, 56),
                LocalDateTime.of(2023, 10, 20, 14, 0, 0),
                "path/to/image.jpg",
                List.of(new BookAuthorResponseDto(1L, "Author Name", 1L, "AUTHOR")),
                List.of(new BookDetailCategoryResponseDto(1L, "Category Name")),
                List.of(new TagResponseDto(1L, "Tag Name", true)),
                5L
        );

        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(bookRepository.getBookDetail(bookId)).thenReturn(mockResponse);

        // When
        BookDetailResponseDto result = bookService.getBookById(bookId);

        // Then
        assertNotNull(result);
        assertEquals("Sample Publisher", result.publisherName());
        assertEquals("Sample Title", result.title());
        assertEquals(1L, result.bookId());
        verify(bookRepository).existsById(bookId);
        verify(bookRepository).getBookDetail(bookId);
    }

    /**
     * 도서 상세 조회 실패 test
     * 존재하지 않는 도서
     */
    @Test
    @DisplayName("존재하지 않는 도서")
    void getBookByIdNotFoundTest() {
        // Given
        Long bookId = 999L;

        when(bookRepository.existsById(bookId)).thenReturn(false);

        // When & Then
        Exception exception = assertThrows(BookResourceNotFoundException.class, () -> bookService.getBookById(bookId));
        assertEquals("Book with id 999 not found", exception.getMessage());
        verify(bookRepository).existsById(bookId);
        verify(bookRepository, never()).getBookDetail(bookId);
    }

    /**
     * 도서 수정 test
     */
    @Test
    @DisplayName("도서 수정 성공 테스트")
    void updateBooksSuccessTest() {
        // Given
        Long bookId = 1L;

        BookRequestDto bookDto = new BookRequestDto(
                1L, // publisherId
                1L, // bookStatusId
                1L, // categoryId
                "Updated Title", // title
                "Updated Chapter", // chapter
                "Updated Description", // description
                "2023-10-01", // publishedDate
                "978-3-16-148410-0", // isbn
                20000L, // price
                new BigDecimal("0.20"), // discountRate
                true, // isPacked
                100, // stock
                "updated-thumbnail.jpg", // thumbnailPath
                List.of(1L, 2L), // tagIdList
                List.of(new BookAuthorRequestDto(1L, 1L)) // authorList
        );

        Publisher publisher = new Publisher("Sample Publisher", true);
        BookStatus bookStatus = new BookStatus("Available");
        Category category = new Category(null, "Sample Category", true);
        Book oldBook = new Book();

        when(bookRepository.existsById(bookId)).thenReturn(true);
        when(publisherRepository.findById(1L)).thenReturn(Optional.of(publisher));
        when(statusRepository.findById(1L)).thenReturn(Optional.of(bookStatus));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(oldBook));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(tagRepository.findById(anyLong())).thenReturn(Optional.of(new Tag("Sample Tag")));
        when(authorRepository.findById(anyLong())).thenReturn(Optional.of(new Author("Sample Author", true)));
        when(authorTypeRepository.findById(anyLong())).thenReturn(Optional.of(new AuthorType("Main Author", true)));

        // When
        bookService.updateBooks(bookId, bookDto);

        // Then
        verify(bookRepository).existsById(bookId);
        verify(publisherRepository).findById(1L);
        verify(statusRepository).findById(1L);
        verify(bookRepository).findById(bookId);
        verify(bookRepository).save(any(Book.class));
        verify(bookCategoryRepository).deleteByBookBookId(bookId);
        verify(bookCategoryRepository).save(any(BookCategory.class));
        verify(bookImageRepository).deleteByBook(any(Book.class));
        verify(bookTagRepository).deleteByBookBookId(bookId);
        verify(bookTagRepository, times(2)).save(any(BookTag.class));
        verify(bookAuthorRepository).deleteAllByBook(any(Book.class));
        verify(bookAuthorRepository).save(any(BookAuthor.class));
    }

    /**
     * 도서 수정 실패 test
     * 도서 없음
     */
    @Test
    @DisplayName("도서 없음")
    void updateBooksBookNotFoundTest() {
        // Given
        Long bookId = 999L;

        BookRequestDto bookDto = new BookRequestDto(
                1L, 1L, 1L, "Title", "Chapter", "Description",
                "2023-10-01", "978-3-16-148410-0", 20000L,
                new BigDecimal("0.20"), true, 100, "thumbnail.jpg",
                List.of(1L), List.of(new BookAuthorRequestDto(1L, 1L))
        );

        when(bookRepository.existsById(bookId)).thenReturn(false);

        // When & Then
        Exception exception = assertThrows(BookResourceNotFoundException.class,
                () -> bookService.updateBooks(bookId, bookDto));

        assertEquals("책이 존재하지 않습니다.", exception.getMessage());
        verify(bookRepository).existsById(bookId);
        verifyNoMoreInteractions(bookRepository, publisherRepository, statusRepository);
    }

    /**
     * 도서 삭제 test
     */
    @Test
    @DisplayName("도서 삭제 성공 테스트")
    void deleteBooksSuccessTest() {
        // Given
        Long bookId = 1L;

        when(bookRepository.existsById(bookId)).thenReturn(true);

        // When
        bookService.deleteBooks(bookId);

        // Then
        verify(bookRepository).existsById(bookId);
        verify(bookRepository).deleteById(bookId);
    }

    /**
     * 도서 삭제 실패 test
     */
    @Test
    @DisplayName("도서 삭제 실패 test")
    void deleteErrorBooks() {
        //given
        Long bookId = 1L;

        when(bookRepository.existsById(bookId)).thenReturn(false);

        //when
        Exception exception = assertThrows(BookResourceNotFoundException.class,
                () -> bookService.deleteBooks(bookId));

        assertEquals("Book with id 1 not found", exception.getMessage());
        verify(bookRepository).existsById(bookId);
        verify(bookRepository, never()).deleteById(bookId);
    }

}
