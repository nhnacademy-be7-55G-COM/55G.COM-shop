package shop.S5G.shop.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import shop.S5G.shop.dto.BookDto;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.exception.BadRequestException;
import shop.S5G.shop.exception.BookException.BookBadRequestException;
import shop.S5G.shop.service.BookService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BookController {
    private final BookService bookService;
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    //도서 등록
    @PostMapping("/book")
    public ResponseEntity addBook(@Validated @RequestBody BookDto bookdto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new BookBadRequestException("잘못된 입력입니다.");
        }
        Book book = new Book(
                bookdto.getPublisherId(),
                bookdto.getBookStatusId(),
                bookdto.getTitle(),
                bookdto.getChapter(),
                bookdto.getDescreption(),
                bookdto.getPublishedDate(),
                bookdto.getIsbn(),
                bookdto.getPrice(),
                bookdto.getDiscountRate(),
                bookdto.isPacked(),
                bookdto.getStock(),
                bookdto.getViews(),
                bookdto.getCreatedAt());
        bookService.createBook(book);
        return ResponseEntity.ok().body("success");
    }

    //도서 목록 조회
    @GetMapping("/books")
    public ResponseEntity<List<Book>> getAllBooks() {
        bookService.allBook();
        return ResponseEntity.ok().body(bookService.allBook());
    }

    //도서 상세 조회
    @GetMapping("/book/{bookId}")
    public ResponseEntity<Book> getBookById(@Validated @PathVariable("bookId") Long bookId) {
        if (bookId < 1) {
            throw new BookBadRequestException("bookId must be greater than 0");
        }
        Book book = bookService.getBookByid(bookId);
        return ResponseEntity.ok().body(book);
    }

    //도서 수정
    @PutMapping("/book/{bookId}")
    public ResponseEntity updateBook(@Validated @PathVariable("bookId") Long bookId, @Validated @RequestBody BookDto bookdto, BindingResult bindingResult) {
        if(bookId < 1) {
            throw new BookBadRequestException("bookId must be greater than 0");
        }
        if (bindingResult.hasErrors()) {
            throw new BookBadRequestException("잘못된 입력입니다.");
        }
        Book book = new Book(
                bookdto.getPublisherId(),
                bookdto.getBookStatusId(),
                bookdto.getTitle(),
                bookdto.getChapter(),
                bookdto.getDescreption(),
                bookdto.getPublishedDate(),
                bookdto.getIsbn(),
                bookdto.getPrice(),
                bookdto.getDiscountRate(),
                bookdto.isPacked(),
                bookdto.getStock(),
                bookdto.getViews(),
                bookdto.getCreatedAt());
        bookService.updateBooks(bookId, book);
        return ResponseEntity.ok().body("success");
    }

    //도서 삭제
    @DeleteMapping("/book/{bookId}")
    public ResponseEntity deleteBook(@Validated @PathVariable("bookId") Long bookId) {
        if (bookId < 1) {
            throw new BookBadRequestException("bookId must be greater than 0");
        }
        bookService.deleteBooks(bookId);
        return ResponseEntity.ok().body("success");
    }
}