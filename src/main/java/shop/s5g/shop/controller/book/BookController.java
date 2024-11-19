package shop.s5g.shop.controller.book;

import jakarta.validation.Valid;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.book.*;
import shop.s5g.shop.dto.bookCategory.BookCategoryBookResponseDto;
import shop.s5g.shop.dto.bookCategory.BookCategoryResponseDto;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.exception.book.BookBadRequestException;
import shop.s5g.shop.dto.book.BookDetailResponseDto;
import shop.s5g.shop.dto.book.BookPageableResponseDto;
import shop.s5g.shop.dto.book.BookRequestDto;
import shop.s5g.shop.dto.book.BookResponseDto;
import shop.s5g.shop.dto.book.BookSimpleResponseDto;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.exception.book.BookBadRequestException;
import shop.s5g.shop.service.book.BookService;

@Slf4j
@RestController
@RequestMapping("/api/shop")
public class BookController {

    private final String MSG_BAD_BOOK_ID = "도서 ID는 1보다 커야 합니다.";

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    //도서 등록
    @PostMapping("/book")
    public ResponseEntity<MessageDto> addBook(@Valid @RequestBody BookRequestDto bookDto,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BookBadRequestException("잘못된 입력입니다.");
        }

        bookService.createBook(bookDto);
        return ResponseEntity.ok().body(new MessageDto("도서 등록 성공"));
    }

    //도서 목록 조회
    @GetMapping("/books")
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        return ResponseEntity.ok().body(bookService.allBook());
    }

    //도서 목록 조회 pageable
    @GetMapping("/books/pageable")
    public ResponseEntity<PageResponseDto<BookPageableResponseDto>> getAllBooksPageable(
        Pageable pageable) {
        log.trace("/books/pageable says: Pageable={}", pageable);
        return ResponseEntity.ok().body(PageResponseDto.of(bookService.allBookPageable(pageable)));
    }

    //도서 상세 조회
    @GetMapping("/book/{bookId}")
    public ResponseEntity<BookDetailResponseDto> getBookById(
        @Valid @PathVariable("bookId") Long bookId) {
        if (bookId < 1) {
            throw new BookBadRequestException(MSG_BAD_BOOK_ID);
        }
        BookDetailResponseDto bookDetailResponseDto = bookService.getBookById(bookId);
        return ResponseEntity.ok().body(bookDetailResponseDto);
    }

    //도서 수정
    @PutMapping("/books/{bookId}")
    public ResponseEntity<MessageDto> updateBook(@Valid @PathVariable("bookId") Long bookId,
        @Valid @RequestBody BookRequestDto bookdto, BindingResult bindingResult) {
        if (bookId < 1) {
            throw new BookBadRequestException(MSG_BAD_BOOK_ID);
        }

        if (bindingResult.hasErrors()) {
            throw new BookBadRequestException("잘못된 입력입니다.");
        }

        bookService.updateBooks(bookId, bookdto);
        return ResponseEntity.ok().body(new MessageDto("도서 수정 성공"));
    }

    //도서 삭제
    @DeleteMapping("/books/{bookId}")
    public ResponseEntity<MessageDto> deleteBook(@Validated @PathVariable("bookId") Long bookId) {
        if (bookId < 1) {
            throw new BookBadRequestException(MSG_BAD_BOOK_ID);
        }
        bookService.deleteBooks(bookId);
        return ResponseEntity.ok().body(new MessageDto("도서 삭제 성공"));
    }

    @GetMapping("/books/query")
    public ResponseEntity<List<BookSimpleResponseDto>> queryBooks(@RequestParam List<Long> books) {
//        List<Long> bookIds = Arrays.stream(stringify.split(",")).map(Long::valueOf).toList();
        return ResponseEntity.ok(bookService.getSimpleBooks(books));
    }

//    //도서id 리스트로 도서 리스트 조회
//    @GetMapping("/book/{bookIdList}")
//    public ResponseEntity<List<BookDetailResponseDto>> getBookListByBookId(@PathVariable("bookIdList") List<BookCategoryBookResponseDto> bookIdList) {
//        List<BookDetailResponseDto> bookListByBookIdList = bookService.getBookListByBookIdList(bookIdList);
//        return ResponseEntity.ok().body(bookListByBookIdList);
//    }

    // bookId 리스트로 book 리스트 조회
    @PostMapping("/book/bookList")
    ResponseEntity<List<BookDetailResponseDto>> getBooks(@RequestBody List<BookCategoryBookResponseDto> bookList) {
        List<BookDetailResponseDto> bookListByBookIdList = bookService.getBookListByBookIdList(bookList);
        return ResponseEntity.ok().body(bookListByBookIdList);
    }

    //categoryId로 bookList조회
    @GetMapping("/books/{categoryId}")
    ResponseEntity<List<BookPageableResponseDto>> getBookByCategory(@PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.ok().body(bookService.getBookList(categoryId));
    }
}