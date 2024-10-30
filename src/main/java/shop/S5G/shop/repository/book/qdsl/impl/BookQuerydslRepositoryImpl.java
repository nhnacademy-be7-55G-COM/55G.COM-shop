package shop.S5G.shop.repository.book.qdsl.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.S5G.shop.dto.Book.BookRequestDto;
import shop.S5G.shop.dto.Book.BookResponseDto;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.entity.QBook;
import shop.S5G.shop.repository.book.qdsl.BookQuerydslRepository;

import java.util.List;

import static shop.S5G.shop.entity.QBook.book;

public class BookQuerydslRepositoryImpl extends QuerydslRepositorySupport implements BookQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;
    public BookQuerydslRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Book.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    //도서 수정
    @Override
    public void updateBook(long bookId, BookRequestDto bookDto) {
        update(book)
                .set(book.publisherId, bookDto.publisherId())
                .set(book.bookStatusId, bookDto.bookStatusId())
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

    //모든 도서 List<BookResponseDto>타입으로 리턴
    @Override
    public List<BookResponseDto> findAllBookList() {
        QBook book = QBook.book;

        return jpaQueryFactory
                .select(Projections.fields(BookResponseDto.class,
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
                .fetch();
    }

    //도서 상세 BookResponseDto타입으로 리턴
    @Override
    public BookResponseDto getBookDetail(long bookId) {

        return jpaQueryFactory
                .select(Projections.fields(BookResponseDto.class,
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
                .fetchOne(); //bookId에 해당하는 단일 결과 반환
    }
}
