package shop.S5G.shop.repository.bookstatus.qdsl.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.S5G.shop.dto.bookstatus.BookStatusResponseDto;
import shop.S5G.shop.entity.BookStatus;
import shop.S5G.shop.repository.bookstatus.qdsl.BookStatusQuerydslRepository;

import java.util.List;

import static shop.S5G.shop.entity.QBookStatus.bookStatus;

public class BookStatusQuerydslRepositoryImpl extends QuerydslRepositorySupport implements BookStatusQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public BookStatusQuerydslRepositoryImpl(EntityManager em) {
        super(BookStatus.class);
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @PersistenceContext
    private EntityManager em;

    //모든 도서상태 List<BookStatusResponseDto>타입으로 리턴
    @Override
    public List<BookStatusResponseDto> findAllBookStatus() {
        return jpaQueryFactory
                .select(Projections.constructor(BookStatusResponseDto.class,
                        bookStatus.name))
                .from(bookStatus)
                .fetch();
    }
}
