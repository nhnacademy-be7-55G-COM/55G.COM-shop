package shop.S5G.shop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.S5G.shop.service.BookCategoryService;

@RestController
@RequestMapping("/api/shop")
public class BookCategoryController {

    private BookCategoryService bookCategoryService;
    @Autowired
    public BookCategoryController(BookCategoryService bookCategoryService) {
        this.bookCategoryService = bookCategoryService;
    }

    //도서 내 카테고리 추가
    @PostMapping("/{bookId}/{categoryId}")
//    public ResponseEntity addCategortInBook(@PathVariable("bookId") Long bookId, @PathVariable("categoryId")){
//
//        return ResponseEntity.ok().body("등록 성공");
//    }

    //도서 내 카테고리 조회
    @GetMapping("/{bookId}/{categoryId}")
    public ResponseEntity getCategoryInBook(@PathVariable("bookId") Long bookId, @PathVariable("categoryId") Long categoryId) {

        return ResponseEntity.ok().build(); //카테고리 리턴
    }

    //도서 내 카테고리 수정
//    @PutMapping("/{bookId}/{categoryId}")
//    public ResponseEntity updateCategoryInBook(@PathVariable("bookId") Long bookId, @PathVariable("categoryId") Long categoryId){
//
//    }
//
//
//    //도서 내 카테고리 삭제
//    @DeleteMapping("/{bookId}/{categoryId}")

}
