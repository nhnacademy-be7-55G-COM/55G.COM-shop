package shop.s5g.shop.controller.book;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.dto.book.BookDocumentRequestDto;
import shop.s5g.shop.dto.book.BookDocumentResponseDto;
import shop.s5g.shop.service.book.BookDocumentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/shop")
public class BookDocumentController {

    private final BookDocumentService bookSearchService;

    @GetMapping("/book/search")
    public ResponseEntity<List<BookDocumentResponseDto>> searchByTitleOrDescription(
        @RequestBody BookDocumentRequestDto bookSearchRequestDto
    ) {
        List<BookDocumentResponseDto> bookList = bookSearchService.searchByTitleOrDescription(
            bookSearchRequestDto.keyword());
        return ResponseEntity.ok().body(bookList);
    }
}
