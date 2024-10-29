package shop.S5G.shop.controller.book;

import java.util.List;

import jakarta.validation.Valid;
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
import org.springframework.web.bind.annotation.RestController;
import shop.S5G.shop.dto.Book.BookRequestDto;
import shop.S5G.shop.dto.Book.BookResponseDto;
import shop.S5G.shop.exception.BookException.BookBadRequestException;
import shop.S5G.shop.service.book.impl.BookServiceImpl;

@RestController
@RequestMapping("/api/shop")
public class BookController {

    private final BookServiceImpl bookServiceImpl;
    public BookController(BookServiceImpl bookServiceImpl) {
        this.bookServiceImpl = bookServiceImpl;
    }

    //도서 등록
    @PostMapping("/book")
    public ResponseEntity addBook(@Valid @RequestBody BookRequestDto bookDto, BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            throw new BookBadRequestException("잘못된 입력입니다.");
        }
        bookServiceImpl.createBook(bookDto);
        return ResponseEntity.ok().body("success");
    }

    //도서 목록 조회
    @GetMapping("/books")
    public ResponseEntity<List<BookResponseDto>> getAllBooks() {
        bookServiceImpl.allBook();
        return ResponseEntity.ok().body(bookServiceImpl.allBook());
    }

    //도서 상세 조회
    @GetMapping("/book/{bookId}")
    public ResponseEntity<BookResponseDto> getBookById(@Validated @PathVariable("bookId") Long bookId) {
        if (bookId < 1) {
            throw new BookBadRequestException("bookId must be greater than 0");
        }
        BookResponseDto bookResponseDto = bookServiceImpl.getBookById(bookId);
        return ResponseEntity.ok().body(bookResponseDto);
    }

    //도서 수정
    @PutMapping("/book/{bookId}")
    public ResponseEntity<String> updateBook(@Valid @PathVariable("bookId") Long bookId, @Valid @RequestBody BookRequestDto bookdto, BindingResult bindingResult) {
        if(bookId < 1) {
            throw new BookBadRequestException("bookId must be greater than 0");
        }

        if (bindingResult.hasErrors()) {
            throw new BookBadRequestException("잘못된 입력입니다.");
        }


        bookServiceImpl.updateBooks(bookId, bookdto);
        return ResponseEntity.ok().body("success");
    }

    //도서 삭제
    @DeleteMapping("/book/{bookId}")
    public ResponseEntity deleteBook(@Validated @PathVariable("bookId") Long bookId) {
        if (bookId < 1) {
            throw new BookBadRequestException("bookId must be greater than 0");
        }
        bookServiceImpl.deleteBooks(bookId);
        return ResponseEntity.ok().body("success");
    }
}