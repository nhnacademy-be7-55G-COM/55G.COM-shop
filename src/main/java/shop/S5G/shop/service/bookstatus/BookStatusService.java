package shop.S5G.shop.service.bookstatus;

import shop.S5G.shop.dto.bookstatus.BookStatusResponseDto;

import java.util.List;

public interface BookStatusService {
    List<BookStatusResponseDto> getAllBookStatus();
}
