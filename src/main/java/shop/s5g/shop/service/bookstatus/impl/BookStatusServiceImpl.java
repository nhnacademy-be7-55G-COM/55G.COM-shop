package shop.s5g.shop.service.bookstatus.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import shop.s5g.shop.dto.book.status.BookStatusResponseDto;
import shop.s5g.shop.repository.book.status.BookStatusRepository;
import shop.s5g.shop.service.bookstatus.BookStatusService;

import java.util.List;

@Service
public class BookStatusServiceImpl implements BookStatusService {

    private final BookStatusRepository bookStatusRepository;

    @Autowired
    public BookStatusServiceImpl(BookStatusRepository bookStatusRepository) {
        this.bookStatusRepository = bookStatusRepository;
    }

    //도서상태 전체 조회
    @Override
    public List<BookStatusResponseDto> getAllBookStatus() {
        return bookStatusRepository.findAllBookStatus();
    }
}