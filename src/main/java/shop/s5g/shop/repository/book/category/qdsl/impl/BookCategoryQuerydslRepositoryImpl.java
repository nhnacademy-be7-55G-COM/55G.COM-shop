package shop.s5g.shop.repository.book.category.qdsl.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.dto.book.category.BookCategoryResponseDto;
import shop.s5g.shop.entity.book.category.BookCategory;
import shop.s5g.shop.repository.book.category.qdsl.BookCategoryQuerydslRepository;
import shop.s5g.shop.entity.QBook;
import shop.s5g.shop.entity.QCategory;
import shop.s5g.shop.entity.book.category.QBookCategory;

import java.util.List;


public class BookCategoryQuerydslRepositoryImpl extends QuerydslRepositorySupport implements BookCategoryQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public BookCategoryQuerydslRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(BookCategory.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    QBook book = QBook.book;
    QBookCategory bookCategory = QBookCategory.bookCategory;
    QCategory category = QCategory.category;
    QCategory subCategory = new QCategory("subCategory");


    //bookCateogry 전체 목록 조회
    @Override
    public List<BookCategoryResponseDto> findAllBookCategory() {
        return jpaQueryFactory
                .from(bookCategory)
                .select(Projections.constructor(BookCategoryResponseDto.class,
                        bookCategory.category.categoryId,
                        bookCategory.book.bookId
                        ))
                .fetch();
    }

}