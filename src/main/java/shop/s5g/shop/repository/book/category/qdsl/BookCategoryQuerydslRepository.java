package shop.s5g.shop.repository.book.category.qdsl;

import shop.s5g.shop.dto.book.category.BookCategoryResponseDto;

import java.util.List;

public interface BookCategoryQuerydslRepository {
    List<BookCategoryResponseDto> findCategoryByBookId(Long bookId);
}
