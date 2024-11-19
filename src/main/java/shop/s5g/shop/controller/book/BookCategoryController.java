package shop.s5g.shop.controller.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.s5g.shop.dto.book.category.BookCategoryBookResponseDto;
import shop.s5g.shop.dto.book.category.BookCategoryResponseDto;
import shop.s5g.shop.dto.book.category.BookCategoryResponseDto;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.service.book.category.BookCategoryService;

import java.util.List;

@RestController
@RequestMapping("/api/shop")
public class BookCategoryController {

    private final BookCategoryService bookCategoryService;
    @Autowired
    public BookCategoryController(BookCategoryService bookCategoryService) {
        this.bookCategoryService = bookCategoryService;
    }

    //도서 내 카테고리 등록
    @PostMapping("/bookcategory/{bookId}/{categoryId}")
    public ResponseEntity<MessageDto> addCategoryInBook(@PathVariable("bookId") Long bookId, @PathVariable("categoryId") Long categoryId) {
        bookCategoryService.addCategoryInBook(bookId, categoryId);
        return ResponseEntity.ok().body(new MessageDto("도서 내 카테고리 등록 성공"));
    }

    //도서카테고리 전체 조회
    @GetMapping("/bookcategory")
    public ResponseEntity<List<BookCategoryResponseDto>> getAllBookCategory() {
        List<BookCategoryResponseDto> allBookCategory = bookCategoryService.getAllBookCategory();
        return ResponseEntity.ok().body(allBookCategory);
    }

    //도서 내 카테고리 조회
    @GetMapping("/bookcategory/{bookId}")
    public ResponseEntity<List<BookCategoryResponseDto>> getCategoryInBook(@PathVariable("bookId") Long bookId) {
        return ResponseEntity.ok().body(bookCategoryService.getCategoryInBook(bookId));
    }

    //카테고리 id로 도서 조회
    @GetMapping("/bookcategory/{categoryId}")
    public ResponseEntity<List<BookCategoryBookResponseDto>> getBookByCategory(@PathVariable("categoryId") Long categoryId) {
        return ResponseEntity.ok().body(bookCategoryService.getBookByCategoryId(categoryId));
    }

    //도서 내 카테고리 삭제
    @DeleteMapping("/bookcategory/{bookId}/{categoryId}")
    public ResponseEntity<MessageDto> deleteCategoryInBook(@PathVariable("bookId") Long bookId, @PathVariable("categoryId") Long categoryId) {
        bookCategoryService.deleteCategory(bookId, categoryId);
        return ResponseEntity.ok().body(new MessageDto("도서 내 카테고리 삭제 성공"));
    }
}