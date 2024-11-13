package shop.s5g.shop.service.book;

import java.util.List;
import shop.s5g.shop.dto.book.BookDocumentResponseDto;

public interface BookDocumentService {

    List<BookDocumentResponseDto> searchByTitleOrDescription(String keyword);
}
