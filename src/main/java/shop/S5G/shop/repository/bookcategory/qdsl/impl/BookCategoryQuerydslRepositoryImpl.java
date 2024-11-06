package shop.S5G.shop.repository.bookcategory.qdsl.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.S5G.shop.dto.bookcategory.BookCategoryResponseDto;
import shop.S5G.shop.entity.bookcategory.BookCategory;
import shop.S5G.shop.repository.bookcategory.qdsl.BookCategoryQuerydslRepository;

import java.util.List;

import static shop.S5G.shop.entity.bookcategory.QBookCategory.bookCategory;

public class BookCategoryQuerydslRepositoryImpl extends QuerydslRepositorySupport implements BookCategoryQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public BookCategoryQuerydslRepositoryImpl(EntityManager em) {
        super(BookCategory.class);
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @PersistenceContext
    private EntityManager em;

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