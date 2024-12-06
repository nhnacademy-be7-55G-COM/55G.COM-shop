package shop.s5g.shop.service.like;

import shop.s5g.shop.dto.book.BookLikeResponseDto;

import java.util.List;

public interface LikeService {
    void addLikeInBook(Long customerId, Long bookId);

    void deleteLikeInBook(Long customerId, Long bookId);

    List<BookLikeResponseDto> getLikeBookByCustomerId(Long customerId);
}
