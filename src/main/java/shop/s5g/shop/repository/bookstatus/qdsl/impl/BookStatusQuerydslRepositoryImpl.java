package shop.s5g.shop.repository.bookstatus.qdsl.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.dto.bookstatus.BookStatusResponseDto;
import shop.s5g.shop.entity.BookStatus;
import shop.s5g.shop.repository.bookstatus.qdsl.BookStatusQuerydslRepository;

import java.util.List;

import static shop.s5g.shop.entity.QBookStatus.bookStatus;

public class BookStatusQuerydslRepositoryImpl extends QuerydslRepositorySupport implements BookStatusQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public BookStatusQuerydslRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(BookStatus.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    //모든 도서상태 List<BookStatusResponseDto>타입으로 리턴
    @Override
    public List<BookStatusResponseDto> findAllBookStatus() {
        return jpaQueryFactory
                .select(Projections.constructor(BookStatusResponseDto.class,
                        bookStatus.name))
                .from(bookStatus)
                .fetch();
    }
    //todo 도서 1권의 상태 조회 코드 작성
}
