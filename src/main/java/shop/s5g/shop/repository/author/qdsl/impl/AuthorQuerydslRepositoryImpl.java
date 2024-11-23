package shop.s5g.shop.repository.author.qdsl.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import shop.s5g.shop.dto.author.AllAuthorResponseDto;
import shop.s5g.shop.dto.author.AuthorRequestDto;
import shop.s5g.shop.dto.author.AuthorResponseDto;
import shop.s5g.shop.entity.Author;
import shop.s5g.shop.repository.author.qdsl.AuthorQuerydslRepository;

import java.util.List;

import static shop.s5g.shop.entity.QAuthor.author;

@Repository
public class AuthorQuerydslRepositoryImpl extends QuerydslRepositorySupport implements AuthorQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public AuthorQuerydslRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Author.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    //작가 전체 조회
    @Override
    public Page<AllAuthorResponseDto> findAllAuthor(Pageable pageable) {
        List<AllAuthorResponseDto> authors = jpaQueryFactory
                .select(Projections.constructor(AllAuthorResponseDto.class,
                        author.authorId,
                        author.name,
                        author.active
                ))
                .from(author)
                .where(author.active.eq(true))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 개수 쿼리
        Long total = jpaQueryFactory
                .select(author.count())
                .from(author)
                .where(author.active.eq(true))
                .fetchOne();
        total = total != null ? total : 0L; // Null 안전 처리

        // Page 객체 반환
        return new PageImpl<>(authors, pageable, total);
    }

    //작가id로 작가 조회
    @Override
    public AuthorResponseDto getAuthor(long authorId) {
        return jpaQueryFactory.select(Projections.constructor(AuthorResponseDto.class,
                author.authorId,
                author.name,
                author.active
                ))
                .from(author)
                .where(author.authorId.eq(authorId))
                .fetchOne();
    }

    //작가 수정
    @Override
    public void updateAuthor(long authorId, AuthorRequestDto authorRequestDto) {
        update(author)
                .set(author.name, authorRequestDto.name())
                .where(author.authorId.eq(authorId))
                .execute();
    }

    //작가 삭제(비활성화)
    @Override
    public void inactiveAuthor(long authorId) {
        jpaQueryFactory.update(author)
                .set(author.active, false)
                .where(author.authorId.eq(authorId))
                .execute();
    }
}
