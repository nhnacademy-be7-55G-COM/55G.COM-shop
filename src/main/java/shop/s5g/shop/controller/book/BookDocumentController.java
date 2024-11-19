package shop.s5g.shop.controller.book;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import shop.s5g.shop.config.ElasticSearchConfig;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.book.BookDocumentResponseDto;
import shop.s5g.shop.service.book.BookDocumentService;

@RestController
@ConditionalOnBean(ElasticSearchConfig.class)
@RequiredArgsConstructor
@RequestMapping("/api/shop")
public class BookDocumentController {

    private final BookDocumentService bookDocumentService;

    @GetMapping("/book/search")
    public PageResponseDto<BookDocumentResponseDto> searchByTitleOrDescription(
        @RequestParam("keyword") String keyword,
        Pageable pageable
    ) {
        if (keyword.isEmpty()) {
            return bookDocumentService.findAllBooks(pageable);
        }
        return bookDocumentService.searchByTitleOrDescription(keyword, pageable);
    }
}
