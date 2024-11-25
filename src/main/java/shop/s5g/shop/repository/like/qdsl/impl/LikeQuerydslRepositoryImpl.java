package shop.s5g.shop.repository.like.qdsl.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.s5g.shop.dto.book.BookLikeResponseDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.like.Like;
import shop.s5g.shop.entity.like.QLike;
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

//    @Override
//    public List<BookLikeResponseDto> getLikeBooksByCustomerId(Long customerId) {
//        return jpaQueryFactory
//                .select(Projections.constructor(BookLikeResponseDto.class,
//                        //도서 제목, 도서 가격, 출판사, 도서 상태, [도서 이미지]
//                        book.title.as("bookTitle"),
//                        book.price.as("bookPrice"),
//                        author.name.as("authorName"),
//                        publisher.name.as("publisherName"),
//                        bookStatus.name.as("statusName"),
//                        bookImage.imageName.as("imageName")
//                ))
//                .from(like) // 좋아요 테이블
//                .join(book).on(like.book.bookId.eq(book.bookId))
//                .leftJoin(bookImage).on(book.bookId.eq(bookImage.book.bookId))
//                .leftJoin(bookAuthor).on(book.bookId.eq(bookAuthor.book.bookId))
//                .leftJoin(author).on(bookAuthor.author.authorId.eq(author.authorId))
//                .leftJoin(publisher).on(book.publisher.id.eq(publisher.id))
//                .leftJoin(bookStatus).on(book.bookStatus.id.eq(bookStatus.id))
//                .where(like.customer.customerId.eq(customerId))
//                .fetch();
//    }


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

    //like 등록
//    @Override
//    @Transactional
//    public void addLike(Customer customer, Book book) {
//        // Like 엔티티 생성
//        Like like = new Like(customer, book);
//
//        // JPAQueryFactory를 사용하여 삽입 실행
//        jpaQueryFactory.insert(QLike.like)
//                .columns(QLike.like.id.customerId, QLike.like.id.bookId)
//                .values(customer.getCustomerId(), book.getBookId())
//                .execute();
//    }

}