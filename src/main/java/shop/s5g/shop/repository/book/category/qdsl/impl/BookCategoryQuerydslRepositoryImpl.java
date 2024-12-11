package shop.s5g.shop.repository.book.category.qdsl.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.entity.book.category.BookCategory;
import shop.s5g.shop.repository.book.category.qdsl.BookCategoryQuerydslRepository;

public class BookCategoryQuerydslRepositoryImpl extends QuerydslRepositorySupport implements BookCategoryQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public BookCategoryQuerydslRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(BookCategory.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

}