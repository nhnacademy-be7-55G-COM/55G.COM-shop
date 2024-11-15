package shop.s5g.shop.repository.coupon.book.qdsl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Objects;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.dto.coupon.book.CouponBookDetailsForBookDto;
import shop.s5g.shop.dto.coupon.book.CouponBookRequestDto;
import shop.s5g.shop.dto.coupon.book.CouponBookResponseDto;
import shop.s5g.shop.dto.coupon.template.CouponTemplateResponseDto;
import shop.s5g.shop.entity.QBook;
import shop.s5g.shop.entity.coupon.CouponBook;
import shop.s5g.shop.entity.coupon.QCouponBook;
import shop.s5g.shop.entity.coupon.QCouponPolicy;
import shop.s5g.shop.entity.coupon.QCouponTemplate;

public class CouponBookQuerydslRepositoryImpl extends QuerydslRepositorySupport implements CouponBookQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;
    private static final boolean ACTIVE = true;

    public CouponBookQuerydslRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(CouponBook.class);
        this. jpaQueryFactory = jpaQueryFactory;
    }

    QCouponBook couponBook = QCouponBook.couponBook;

    QCouponTemplate couponTemplate = QCouponTemplate.couponTemplate;

    QBook book = QBook.book;

    QCouponPolicy couponPolicy = QCouponPolicy.couponPolicy;

    /**
     * 책 쿠폰이 존재하는 지 체크
     * @param bookId
     * @param couponTemplateId
     * @return boolean
     */
    @Override
    public boolean existsByBookAndCouponTemplate(Long bookId, Long couponTemplateId) {

        return !jpaQueryFactory
            .selectFrom(couponBook)
            .where(couponBook.book.bookId.eq(bookId)
                .and(couponBook.couponTemplate.couponTemplateId.eq(couponTemplateId)))
            .fetch()
            .isEmpty();

    }

    /**
     * 특정 책 쿠폰 조회
     * @param couponBookRequestDto
     * @return CouponBookResponseDto
     */
    @Override
    public CouponBookResponseDto findCouponBook(CouponBookRequestDto couponBookRequestDto) {

        return from(couponBook)
            .innerJoin(couponBook.book, book)
            .innerJoin(couponBook.couponTemplate, couponTemplate)
            .innerJoin(couponTemplate.couponPolicy, couponPolicy)
            .where(book.bookId.eq(couponBookRequestDto.bookId()))
            .where(couponTemplate.couponTemplateId.eq(couponBookRequestDto.couponTemplateId()))
            .select(Projections.constructor(CouponBookResponseDto.class,
                book.title,
                couponPolicy.discountPrice,
                couponPolicy.condition,
                couponPolicy.maxPrice,
                couponPolicy.duration,
                couponTemplate.couponName,
                couponTemplate.couponDescription))
            .fetchOne();

    }

    /**
     * CouponBook 조회 - Pageable
     * @param pageable
     * @return Page<CouponBookResponseDto>
     */
    @Override
    public Page<CouponBookResponseDto> findCouponBooks(Pageable pageable) {

        List<CouponBookResponseDto> bookList = jpaQueryFactory
            .select(Projections.constructor(CouponBookResponseDto.class,
                book.title,
                couponPolicy.discountPrice,
                couponPolicy.condition,
                couponPolicy.maxPrice,
                couponPolicy.duration,
                couponTemplate.couponName,
                couponTemplate.couponDescription))
            .from(couponBook)
            .innerJoin(book)
            .on(couponBook.book.bookId.eq(book.bookId))
            .innerJoin(couponTemplate)
            .on(couponBook.couponTemplate.couponTemplateId.eq(couponTemplate.couponTemplateId))
            .innerJoin(couponPolicy)
            .on(couponTemplate.couponPolicy.couponPolicyId.eq(couponPolicy.couponPolicyId))
            .where(couponTemplate.active.eq(ACTIVE))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        Long totalCnt = jpaQueryFactory
            .select(couponBook.count())
            .from(couponBook)
            .fetchOne();

        long total = (Objects.isNull(totalCnt)) ? 0L : totalCnt;

        return new PageImpl<>(bookList, pageable, total);
    }

    /**
     * 특정 책에 적용된 쿠폰 템플릿 리스트 조회
     * @param bookId
     * @param pageable
     * @return Page<CouponTemplateResponseDto>
     */
    @Override
    public Page<CouponTemplateResponseDto> findCouponBooksByBookId(Long bookId, Pageable pageable) {

        List<CouponTemplateResponseDto> templateList = jpaQueryFactory
            .select(Projections.constructor(CouponTemplateResponseDto.class,
                couponTemplate.couponTemplateId,
                couponPolicy.discountPrice,
                couponPolicy.condition,
                couponPolicy.maxPrice,
                couponPolicy.duration,
                couponTemplate.couponName,
                couponTemplate.couponDescription))
            .from(couponBook)
            .innerJoin(couponTemplate)
            .on(couponBook.couponTemplate.couponTemplateId.eq(couponTemplate.couponTemplateId))
            .innerJoin(couponPolicy)
            .on(couponTemplate.couponPolicy.couponPolicyId.eq(couponPolicy.couponPolicyId))
            .where(couponTemplate.active.eq(ACTIVE))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        Long totalCnt = jpaQueryFactory
            .select(couponBook.count())
            .from(couponBook)
            .fetchOne();

        long total = (Objects.isNull(totalCnt)) ? 0L : totalCnt;

        return new PageImpl<>(templateList, pageable, total);
    }

    /**
     * 특정 쿠폰 템플릿이 적용된 책 리스트 조회
     * @param couponTemplateId
     * @param pageable
     * @return Page<CouponBookDetailsForBookDto>
     */
    @Override
    public Page<CouponBookDetailsForBookDto> findCouponBooksByCouponTemplateId(Long couponTemplateId,
        Pageable pageable) {

        List<CouponBookDetailsForBookDto> detailsForBookList = jpaQueryFactory
            .select(Projections.constructor(CouponBookDetailsForBookDto.class,
                book.title))
            .from(couponBook)
            .innerJoin(book)
            .on(couponBook.book.bookId.eq(book.bookId))
            .limit(pageable.getPageSize())
            .offset(pageable.getOffset())
            .fetch();

        Long totalCnt = jpaQueryFactory
            .select(couponBook.count())
            .from(couponBook)
            .fetchOne();

        long total = (Objects.isNull(totalCnt)) ? 0L : totalCnt;

        return new PageImpl<>(detailsForBookList, pageable, total);
    }
}
