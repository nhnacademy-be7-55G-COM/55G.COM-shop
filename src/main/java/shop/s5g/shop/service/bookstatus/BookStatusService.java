package shop.s5g.shop.service.bookstatus;

import shop.s5g.shop.dto.bookstatus.BookStatusResponseDto;

import java.util.List;

public interface BookStatusService {
    List<BookStatusResponseDto> getAllBookStatus();
}
