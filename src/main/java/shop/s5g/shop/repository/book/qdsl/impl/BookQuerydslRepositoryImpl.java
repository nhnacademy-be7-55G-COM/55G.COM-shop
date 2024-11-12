package shop.s5g.shop.repository.book.qdsl.impl;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import shop.s5g.shop.dto.book.BookPageableResponseDto;
import shop.s5g.shop.dto.book.BookRequestDto;
import shop.s5g.shop.dto.book.BookResponseDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.repository.book.qdsl.BookQuerydslRepository;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static shop.s5g.shop.entity.QBook.book;
import static shop.s5g.shop.entity.QBookImage.bookImage;

@Repository
public class BookQuerydslRepositoryImpl extends QuerydslRepositorySupport implements BookQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public BookQuerydslRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Book.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    // 도서 수정
    @Override
    public void updateBook(long bookId, BookRequestDto bookDto) {
        jpaQueryFactory.update(book)
                .set(book.publisherId.id, bookDto.publisherId()) //TODO bookDto의 publisherId로 publisher를 찾아 객체를 바꿔줘야 되는지?
                .set(book.bookStatusId.id, bookDto.bookStatusId()) //TODO bookDto의 statusId로 status를 찾아 객체를 바꿔줘야 되는지?
                .set(book.title, bookDto.title())
                .set(book.chapter, bookDto.chapter())
                .set(book.description, bookDto.description())
                .set(book.publishedDate, bookDto.publishedDate())
                .set(book.isbn, bookDto.isbn())
                .set(book.price, bookDto.price())
                .set(book.discountRate, bookDto.discountRate())
                .set(book.isPacked, bookDto.isPacked())
                .set(book.stock, bookDto.stock())
                .set(book.views, bookDto.views())
                .set(book.createdAt, bookDto.createdAt())
                .where(book.bookId.eq(bookId))
                .execute();
    }

    // 모든 도서 List<BookResponseDto>타입으로 리턴
    @Override
    public List<BookResponseDto> findAllBookList() {
        return jpaQueryFactory
                .select(Projections.fields(BookResponseDto.class,
                        book.publisherId.id.as("publisherId"),
                        book.bookStatusId.id.as("bookStatusId"),
                        book.title,
                        book.chapter,
                        book.description,
                        book.publishedDate,
                        book.isbn,
                        book.price,
                        book.discountRate,
                        book.isPacked,
                        book.stock,
                        book.views,
                        book.createdAt
                ))
                .from(book)
                .fetch();
    }

    //모든 도서 Page<BookPageableResponseDto>타입으로 리턴
    @Override
    public Page<BookPageableResponseDto> findAllBookPage(Pageable pageable) {
        JPAQuery<BookPageableResponseDto> query = jpaQueryFactory
                .select(Projections.constructor(BookPageableResponseDto.class,
                        book.bookId,
                        book.publisherId.id,
                        book.bookStatusId.id,
                        book.title,
                        book.chapter,
                        book.description,
                        book.publishedDate,
                        book.isbn,
                        book.price,
                        book.discountRate,
                        book.isPacked,
                        book.stock,
                        book.views,
                        book.createdAt,
                        bookImage.imageName
                        ))
                .from(book)
                .leftJoin(bookImage)
                .on( bookImage.book.bookId.eq(book.bookId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                ;

        query = switch(pageable.getSort().toString().split(":")[0]) {
            case "title" -> query.orderBy(book.title.desc());
            case "price" -> query.orderBy(book.price.desc());
            case "publishedDate" -> query.orderBy(book.publishedDate.desc());
            default -> throw new IllegalArgumentException("Unknown sort type: " + pageable.getSort());
        };

        List<BookPageableResponseDto> content = query.fetch();

        Long totalCount = jpaQueryFactory
                .select(book.count())
                .from(book)
                .fetchOne();

        long total = (totalCount != null) ? totalCount : 0L;
        return new PageImpl<>(content, pageable, total);
    }

    // 도서 상세 BookResponseDto타입으로 리턴
    @Override
    public BookResponseDto getBookDetail(long bookId) {
        return select(Projections.fields(BookResponseDto.class,
                book.publisherId,
                book.bookStatusId,
                book.title,
                book.chapter,
                book.description,
                book.publishedDate,
                book.isbn,
                book.price,
                book.discountRate,
                book.isPacked,
                book.stock,
                book.views,
                book.createdAt
        ))
                .from(book)
                .where(book.bookId.eq(bookId))
                .fetchOne();
    }
}
