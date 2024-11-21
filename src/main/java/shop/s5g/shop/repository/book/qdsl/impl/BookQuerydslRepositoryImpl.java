package shop.s5g.shop.repository.book.qdsl.impl;

import com.querydsl.core.types.ConstantImpl;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Stack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import shop.s5g.shop.dto.book.BookDetailResponseDto;
import shop.s5g.shop.dto.book.BookPageableResponseDto;
import shop.s5g.shop.dto.book.BookRequestDto;
import shop.s5g.shop.dto.book.BookResponseDto;
import shop.s5g.shop.dto.book.BookSimpleResponseDto;
import shop.s5g.shop.dto.book.author.BookAuthorResponseDto;
import shop.s5g.shop.dto.book.category.BookDetailCategoryResponseDto;
import shop.s5g.shop.dto.cart.response.CartBooksInfoInCartResponseDto;
import shop.s5g.shop.entity.Book;
import shop.s5g.shop.entity.BookImage;
import shop.s5g.shop.entity.Category;
import shop.s5g.shop.entity.book.category.BookCategory;
import shop.s5g.shop.repository.book.qdsl.BookQuerydslRepository;

import static shop.s5g.shop.entity.QAuthor.author;
import static shop.s5g.shop.entity.QAuthorType.authorType;
import static shop.s5g.shop.entity.QBook.book;
import static shop.s5g.shop.entity.QBookAuthor.bookAuthor;
import static shop.s5g.shop.entity.QBookStatus.bookStatus;
import static shop.s5g.shop.entity.QCategory.category;
import static shop.s5g.shop.entity.QPublisher.publisher;
import static shop.s5g.shop.entity.book.category.QBookCategory.bookCategory;
import static shop.s5g.shop.entity.QBookImage.bookImage;

@Repository
public class BookQuerydslRepositoryImpl extends QuerydslRepositorySupport implements
    BookQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public BookQuerydslRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Book.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    // 도서 수정
    @Override
    public void updateBook(long bookId, BookRequestDto bookDto) {
        jpaQueryFactory.update(book)
            .set(book.publisher.id,
                bookDto.publisherId()) //TODO bookDto의 publisherId로 publisher를 찾아 객체를 바꿔줘야 되는지?
            .set(book.bookStatus.id,
                bookDto.bookStatusId()) //TODO bookDto의 statusId로 status를 찾아 객체를 바꿔줘야 되는지?
            .set(book.title, bookDto.title())
            .set(book.chapter, bookDto.chapter())
            .set(book.description, bookDto.description())
            .set(book.publishedDate, LocalDate.parse(bookDto.publishedDate()))
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
                book.publisher.id.as("publisherId"),
                book.bookStatus.id.as("bookStatusId"),
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
//    @Override
//    public Page<BookPageableResponseDto> findAllBookPage(Pageable pageable) {
//        JPAQuery<BookPageableResponseDto> query = jpaQueryFactory
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
//                        ))
//                .from(book)
//                .leftJoin(bookImage)
//                .on(bookImage.book.bookId.eq(book.bookId))
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                ;
//
//        query = switch(pageable.getSort().toString().split(":")[0]) {
//            case "title" -> query.orderBy(book.title.desc());
//            case "price" -> query.orderBy(book.price.desc());
//            case "publishedDate" -> query.orderBy(book.publishedDate.desc());
//            default -> query.orderBy(book.title.asc());
//        };
//
//        List<BookPageableResponseDto> content = query.fetch();
//
//        Long totalCount = jpaQueryFactory
//                .select(book.count())
//                .from(book)
//                .fetchOne();
//
//        long total = (totalCount != null) ? totalCount : 0L;
//        return new PageImpl<>(content, pageable, total);
//    }

    /**
     * 특정 책의 상태 조회
     * @param bookId
     * @return String
     */
    @Override
    public String findBookStatus(Long bookId) {

        return jpaQueryFactory
            .select(bookStatus.name)
            .from(book)
            .innerJoin(bookStatus)
            .on(book.bookStatus.id.eq(bookStatus.id))
            .where(book.bookId.eq(bookId))
            .fetchOne();

    }

    // 도서 상세 BookResponseDto타입으로 리턴
    @Override
    public BookDetailResponseDto getBookDetail(long bookId) {
        List<BookAuthorResponseDto> authorList = from(bookAuthor)
            .join(bookAuthor.author, author)
            .join(bookAuthor.authorType, authorType)
            .where(bookAuthor.book.bookId.eq(bookId))
            .select(Projections.constructor(BookAuthorResponseDto.class,
                author.authorId,
                author.name,
                authorType.authorTypeId,
                authorType.typeName))
            .fetch();

        BookCategory bookCategoryQuery = from(bookCategory)
            .join(bookCategory.category, category)
            .join(bookCategory.book, book)
            .where(bookCategory.book.bookId.eq(bookId))
            .select(Projections.constructor(BookCategory.class,
                category,
                book
            ))
            .fetchOne();

        List<BookDetailCategoryResponseDto> categoryList = new ArrayList<>();
        Stack<BookDetailCategoryResponseDto> categoryStack = new Stack<>();
        Category subCategory = bookCategoryQuery.getCategory();
        while (subCategory != null) {
            categoryStack.push(new BookDetailCategoryResponseDto(subCategory.getCategoryId(),
                subCategory.getCategoryName()));
            subCategory = subCategory.getParentCategory();
        }
        while (!categoryStack.isEmpty()) {
            categoryList.add(categoryStack.pop());
        }

        BookImage queryBookImage = from(bookImage)
            .join(bookImage.book, book)
            .where(book.bookId.eq(bookId))
            .select(Projections.constructor(BookImage.class,
                bookImage.bookImageId,
                book,
                bookImage.imageName
            ))
            .fetchOne();

        String imagePath = "no-image.png";
        if (queryBookImage != null) {
            imagePath = queryBookImage.getImageName();
        }

        return from(book)
            .join(book.publisher, publisher)
            .join(book.bookStatus, bookStatus)
            .where(book.bookId.eq(bookId))
            .select(Projections.constructor(BookDetailResponseDto.class,
                book.bookId,
                publisher.name,
                bookStatus.name,
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
                book.updatedAt,
                ConstantImpl.create(imagePath),
                ConstantImpl.create(authorList),
                ConstantImpl.create(categoryList)
            ))
            .fetchOne();
    }

    @Override
    public List<BookSimpleResponseDto> findSimpleBooksByIdList(List<Long> idList) {
        return from(book)
            .join(book.bookStatus, bookStatus)
            .where(book.bookId.in(idList))
            .select(Projections.constructor(BookSimpleResponseDto.class,
                book.bookId,
                book.title,
                book.price,
                book.discountRate,
                book.stock,
                book.isPacked,
                bookStatus.name
                )
            ).fetch();
    }
    @Override
    public List<CartBooksInfoInCartResponseDto> findAllBooksInfoInCart(Set<Long> bookIdSet){
        if (Objects.isNull(bookIdSet) || bookIdSet.isEmpty()) {
            return new ArrayList<>();
        }

        return from(bookImage)
            .rightJoin(bookImage.book,book)
            .where(book.bookId.in(bookIdSet))
            .select(Projections.constructor(CartBooksInfoInCartResponseDto.class,
                book.bookId,
                book.price,
                book.discountRate,
                book.stock,
                book.title,
                bookImage.imageName
                )
            ).fetch();
    }
}
