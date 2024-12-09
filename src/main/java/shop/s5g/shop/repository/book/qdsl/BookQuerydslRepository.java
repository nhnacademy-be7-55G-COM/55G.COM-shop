package shop.s5g.shop.repository.book.qdsl;

import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import shop.s5g.shop.dto.book.BookDetailResponseDto;
import shop.s5g.shop.dto.book.BookPageableResponseDto;
import shop.s5g.shop.dto.book.BookRequestDto;

import java.util.List;
import shop.s5g.shop.dto.book.BookSimpleResponseDto;
import shop.s5g.shop.dto.cart.response.CartBooksInfoInCartResponseDto;

public interface BookQuerydslRepository {
    void updateBook(long bookId, BookRequestDto bookDto);
    BookDetailResponseDto getBookDetail(long bookId);
    Page<BookPageableResponseDto> findAllBookPage(Pageable pageable);

    List<BookSimpleResponseDto> findSimpleBooksByIdList(List<Long> idList);
    String findBookStatus(Long bookId);
    List<CartBooksInfoInCartResponseDto> findAllBooksInfoInCart(Set<Long> bookIdList);

    void likeCount(Long bookId);
}
