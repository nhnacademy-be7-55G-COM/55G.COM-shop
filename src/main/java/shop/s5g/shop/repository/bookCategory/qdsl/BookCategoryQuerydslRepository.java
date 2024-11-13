package shop.s5g.shop.repository.bookCategory.qdsl;

import shop.s5g.shop.dto.bookCategory.BookCategoryResponseDto;

import java.util.List;

public interface BookCategoryQuerydslRepository {
    List<BookCategoryResponseDto> findCategoryByBookId(Long bookId);
}
