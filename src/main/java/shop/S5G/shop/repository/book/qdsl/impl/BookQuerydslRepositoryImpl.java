package shop.S5G.shop.repository.book.qdsl.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import shop.S5G.shop.dto.Book.BookRequestDto;
import shop.S5G.shop.dto.Book.BookResponseDto;
import shop.S5G.shop.entity.Book;
import shop.S5G.shop.repository.book.qdsl.BookQuerydslRepository;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static shop.S5G.shop.entity.QBook.book;

@Repository
public class BookQuerydslRepositoryImpl extends QuerydslRepositorySupport implements BookQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public BookQuerydslRepositoryImpl(EntityManager em) {
        super(Book.class);
        this.jpaQueryFactory = new JPAQueryFactory(em);
    }

    @PersistenceContext
    private EntityManager em;

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
