package shop.s5g.shop.service.book;

import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.book.BookDocumentResponseDto;

public interface BookDocumentService {

    PageResponseDto<BookDocumentResponseDto> findAllBooks(Pageable pageable);

    PageResponseDto<BookDocumentResponseDto> searchByTitleOrDescription(String keyword,
        Pageable pageable);
}
