package shop.S5G.shop.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.service.BookService;

@RestController
@RequestMapping("/api")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    //도서 등록
    @PostMapping("/book")
    public ResponseEntity addBook(@RequestBody Book book) {
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
    public ResponseEntity<Book> getBookById(@PathVariable("bookId") int bookId) {
        Book book = bookService.getBookByid(bookId);
        return ResponseEntity.ok().body(book);
    }

    //도서 수정
    @PutMapping("/book/{bookId}")
    public ResponseEntity updateBook(@PathVariable("bookId") int bookId, @RequestBody Book book) {
        bookService.updateBooks(bookId, book);
        return ResponseEntity.ok().body("success");
    }

    //도서 삭제
    @DeleteMapping("/book/{bookId}")
    public ResponseEntity deleteBook(@PathVariable("bookId") int bookId) {
        bookService.deleteBooks(bookId);
        return ResponseEntity.ok().body("success");
    }
}