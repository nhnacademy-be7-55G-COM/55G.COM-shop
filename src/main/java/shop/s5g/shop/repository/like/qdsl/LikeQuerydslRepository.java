package shop.s5g.shop.repository.like.qdsl;

import shop.s5g.shop.dto.book.BookLikeResponseDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.like.Like;
import shop.s5g.shop.entity.member.Customer;

import java.util.List;

public interface LikeQuerydslRepository {
    List<BookLikeResponseDto> getLikeBooksByCustomerId(Long customerId);

    void deleteLike(Customer customer, Book book);

//    void addLike(Customer customer, Book book);
}
