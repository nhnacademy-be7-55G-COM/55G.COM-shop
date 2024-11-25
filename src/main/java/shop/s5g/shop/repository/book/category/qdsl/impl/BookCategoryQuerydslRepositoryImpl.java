package shop.s5g.shop.repository.book.category.qdsl.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.dto.book.category.BookCategoryResponseDto;
import shop.s5g.shop.entity.book.category.BookCategory;
import shop.s5g.shop.repository.book.category.qdsl.BookCategoryQuerydslRepository;
import shop.s5g.shop.dto.book.category.BookCategoryBookResponseDto;
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

    //categoryId에 해당하는 bookId 리스트 반환
    @Override
    public List<BookCategoryBookResponseDto> findBookByCategoryId(Long categoryId) {
        return jpaQueryFactory
                .from(bookCategory)
                .select(Projections.constructor(BookCategoryBookResponseDto.class,
                        bookCategory.category.categoryId,
                        bookCategory.book.bookId
                        ))
                .where(categoryId != null ? bookCategory.category.categoryId.eq(categoryId) : bookCategory.category.categoryId.isNull())
                .fetch();
    }

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

    //categoryId로 bookList 조회
//    @Override
//    public List<BookPageableResponseDto> getBookList(Long categoryId) {
//        return jpaQueryFactory
//                .select(Projections.constructor(BookPageableResponseDto.class,
//                        book.bookId,
//                        book.publisher.id,
//                        book.bookStatus.id,
//                        book.title,
//                        book.chapter,
//                        book.description,
//                        book.publishedDate,
//                        book.isbn,
//                        book.price,
//                        book.discountRate,
//                        book.isPacked,
//                        book.stock,
//                        book.views,
//                        book.createdAt,
//                        bookImage.imageName
//                ))
//                .from(book)
//                .innerJoin(bookCategory).on(book.bookId.eq(bookCategory.book.bookId))
//                .innerJoin(category).on(bookCategory.category.categoryId.eq(category.categoryId))
//                .leftJoin(bookImage).on(bookImage.book.bookId.eq(book.bookId)) // book과 bookImage 조인
//                .where(category.categoryId.in(
//                        JPAExpressions
//                                .select(subCategory.categoryId) // 서브쿼리: 하위 카테고리 ID 조회
//                                .from(category)
//                                .innerJoin(subCategory).on(category.categoryId.eq(subCategory.parentCategory.categoryId))
//                                .where(category.categoryId.eq(categoryId)) // 입력받은 categoryId
//                ))
//                .fetch();
//    }
}