package shop.s5g.shop.repository.bookstatus.qdsl;

import shop.s5g.shop.dto.bookstatus.BookStatusResponseDto;

import java.util.List;

public interface BookStatusQuerydslRepository {
    List<BookStatusResponseDto> findAllBookStatus();
}
