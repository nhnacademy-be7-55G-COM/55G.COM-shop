package shop.s5g.shop.repository.booktag.qdsl.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import shop.s5g.shop.entity.booktag.BookTag;
import shop.s5g.shop.repository.booktag.qdsl.BookTagQuerydslRepository;

import java.util.List;

import static shop.s5g.shop.entity.booktag.QBookTag.bookTag;

@Repository
public class BookTagQuerydslRepositoryImpl extends QuerydslRepositorySupport implements BookTagQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public BookTagQuerydslRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(BookTag.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    //tagId가 도서에 붙어있는 tag 확인
    @Override
    public List<BookTag> BookTagCount(Long tagId) {
        return jpaQueryFactory
                .select(Projections.fields(BookTag.class, bookTag.id))
                .from(bookTag)
                .where(bookTag.id.tagId.eq(tagId))
                .fetch();
    }
}
