package shop.S5G.shop.repository.bookstatus.qdsl;

import shop.S5G.shop.dto.bookstatus.BookStatusResponseDto;

import java.util.List;

public interface BookStatusQuerydslRepository {
    List<BookStatusResponseDto> findAllBookStatus();
}
