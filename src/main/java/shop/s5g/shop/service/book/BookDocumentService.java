package shop.s5g.shop.service.book;

import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.book.BookDocumentResponseDto;

public interface BookDocumentService {

    PageResponseDto<BookDocumentResponseDto> findAllBooks(Pageable pageable);

    PageResponseDto<BookDocumentResponseDto> searchByKeyword(String keyword,
        Pageable pageable);

    PageResponseDto<BookDocumentResponseDto> findAllBooksByCategory(String categoryName,
        Pageable pageable);

    PageResponseDto<BookDocumentResponseDto> searchByCategoryAndKeyword(String categoryName,
        String keyword,
        Pageable pageable);
}
