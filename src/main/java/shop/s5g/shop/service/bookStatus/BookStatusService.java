package shop.s5g.shop.service.bookStatus;

import shop.s5g.shop.dto.bookStatus.BookStatusResponseDto;

import java.util.List;

public interface BookStatusService {
    List<BookStatusResponseDto> getAllBookStatus();
}
