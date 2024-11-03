package shop.S5G.shop.repository.bookcategory.qdsl;

import shop.S5G.shop.dto.bookcategory.BookCategoryResponseDto;

import java.util.List;

public interface BookCategoryQuerydslRepository {
    List<BookCategoryResponseDto> findCategoryByBookId(Long bookId);
}
