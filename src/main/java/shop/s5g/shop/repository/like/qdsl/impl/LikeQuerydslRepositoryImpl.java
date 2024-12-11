package shop.s5g.shop.repository.like.qdsl.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import shop.s5g.shop.dto.book.BookLikeResponseDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.member.Customer;
import shop.s5g.shop.repository.like.qdsl.LikeQuerydslRepository;

import java.util.List;

import static shop.s5g.shop.entity.like.QLike.like;

@Repository
public class LikeQuerydslRepositoryImpl extends QuerydslRepositorySupport implements LikeQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public LikeQuerydslRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(LikeQuerydslRepository.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    //마이페이지에서 좋아요 누른 도서 조회
    @Override
    public List<BookLikeResponseDto> getLikeBooksByCustomerId(Long customerId) {
        //도서 제목, 도서 가격, 출판사, 도서 상태, [도서 이미지]
        return jpaQueryFactory
                .from(like)
                .select(Projections.constructor(BookLikeResponseDto.class,
                        like.book.bookId,
                        like.book.title,
                        like.book.price,
                        like.book.publisher.name,
                        like.book.bookStatus.name
                ))
                .where(like.customer.customerId.eq(customerId))
                .fetch();
    }

    //like 삭제
    @Override
    public void deleteLike(Customer customer, Book book) {
        jpaQueryFactory
                .delete(like) // Like 테이블을 대상으로 삭제
                .where(
                        like.customer.eq(customer) // customer 조건
                                .and(like.book.eq(book)) // book 조건
                )
                .execute(); // 실행
    }

}