package shop.S5G.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.S5G.shop.dto.bookcategory.BookCategoryResponseDto;
import shop.S5G.shop.service.BookCategory.BookCategoryService;

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
    public ResponseEntity addCategoryInBook(@PathVariable("bookId") Long bookId, @PathVariable("categoryId") Long categoryId) {
        bookCategoryService.addCategoryInBook(bookId, categoryId);
        return ResponseEntity.ok().body("등록 성공");
    }

    //도서 내 카테고리 조회
    @GetMapping("/bookcategory/{bookId}")
    public ResponseEntity getCategoryInBook(@PathVariable("bookId") Long bookId) {
        List<BookCategoryResponseDto> categoryList = bookCategoryService.getCategoryInBook(bookId);
        return ResponseEntity.ok().body(categoryList);
    }



    //도서 내 카테고리 삭제
    @DeleteMapping("/bookcategory/{bookId}/{categoryId}")
    public ResponseEntity deleteCategoryInBook(@PathVariable("bookId") Long bookId, @PathVariable("categoryId") Long categoryId) {
        bookCategoryService.deleteCategory(bookId, categoryId);
        return ResponseEntity.ok().body("삭제 성공");
    }
}