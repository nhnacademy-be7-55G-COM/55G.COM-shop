package shop.s5g.shop.repository.bookCategory.qdsl.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.dto.bookCategory.BookCategoryResponseDto;
import shop.s5g.shop.entity.bookCategory.BookCategory;
import shop.s5g.shop.repository.bookCategory.qdsl.BookCategoryQuerydslRepository;

import java.util.List;

import static shop.s5g.shop.entity.bookCategory.QBookCategory.bookCategory;

public class BookCategoryQuerydslRepositoryImpl extends QuerydslRepositorySupport implements BookCategoryQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public BookCategoryQuerydslRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(BookCategory.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }


    //bookId에 해당하는 category 리스트 리턴
    @Override
    public List<BookCategoryResponseDto> findCategoryByBookId(Long bookId) {
        return jpaQueryFactory
                .from(bookCategory)
                .select(Projections.constructor(BookCategoryResponseDto.class,
                        bookCategory.category.categoryId,
                        bookCategory.book.bookId))
                .where(bookId != null ? bookCategory.book.bookId.eq(bookId) : bookCategory.book.bookId.isNull())
                .fetch();
    }
}