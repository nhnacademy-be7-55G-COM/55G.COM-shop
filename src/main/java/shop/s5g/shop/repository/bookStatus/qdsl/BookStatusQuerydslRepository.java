package shop.s5g.shop.repository.bookStatus.qdsl;

import shop.s5g.shop.dto.book.status.BookStatusResponseDto;

import java.util.List;

public interface BookStatusQuerydslRepository {
    List<BookStatusResponseDto> findAllBookStatus();
}
