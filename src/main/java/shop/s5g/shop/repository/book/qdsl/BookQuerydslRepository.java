package shop.s5g.shop.repository.book.qdsl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.book.BookDetailResponseDto;
import shop.s5g.shop.dto.book.BookPageableResponseDto;
import shop.s5g.shop.dto.book.BookRequestDto;
import shop.s5g.shop.dto.book.BookResponseDto;

import java.util.List;
import shop.s5g.shop.dto.book.BookSimpleResponseDto;

public interface BookQuerydslRepository {
    void updateBook(long bookId, BookRequestDto bookDto);
    List<BookResponseDto> findAllBookList();
    BookDetailResponseDto getBookDetail(long bookId);
    Page<BookPageableResponseDto> findAllBookPage(Pageable pageable);

    List<BookSimpleResponseDto> findSimpleBooksByIdList(List<Long> idList);
    String findBookStatus(Long bookId);
}
