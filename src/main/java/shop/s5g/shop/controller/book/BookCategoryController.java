package shop.s5g.shop.controller.book;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import shop.s5g.shop.dto.tag.MessageDto;
import shop.s5g.shop.service.book.category.BookCategoryService;

@RestController
@RequestMapping("/api/shop")
@RequiredArgsConstructor
public class BookCategoryController {

    private final BookCategoryService bookCategoryService;

    //도서 내 카테고리 등록
    @PostMapping("/bookcategory/{bookId}/{categoryId}")
    public ResponseEntity<MessageDto> addCategoryInBook(@PathVariable("bookId") Long bookId, @PathVariable("categoryId") Long categoryId) {
        bookCategoryService.addCategoryInBook(bookId, categoryId);
        return ResponseEntity.ok().body(new MessageDto("도서 내 카테고리 등록 성공"));
    }

}