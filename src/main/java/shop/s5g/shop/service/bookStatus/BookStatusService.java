package shop.s5g.shop.service.bookStatus;

import shop.s5g.shop.dto.book.status.BookStatusResponseDto;

import java.util.List;

public interface BookStatusService {
    List<BookStatusResponseDto> getAllBookStatus();
}
