package shop.s5g.shop.repository.tag.qdsl.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import shop.s5g.shop.dto.publisher.PublisherResponseDto;
import shop.s5g.shop.dto.tag.TagRequestDto;
import shop.s5g.shop.dto.tag.TagResponseDto;
import shop.s5g.shop.entity.Tag;
import shop.s5g.shop.repository.tag.qdsl.TagQuerydslRepository;

import java.util.List;

import static shop.s5g.shop.entity.QPublisher.publisher;
import static shop.s5g.shop.entity.QTag.tag;

@Repository
public class TagQuerydslRepositoryImpl extends QuerydslRepositorySupport implements TagQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public TagQuerydslRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Tag.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    //모든 테그 조회
    @Override
    public Page<TagResponseDto> findAllTag(Pageable pageable) {
        List<TagResponseDto> tags = jpaQueryFactory
                .select(Projections.constructor(TagResponseDto.class,
                        tag.tagId,
                        tag.tagName,
                        tag.active
                ))
                .from(tag)
                .where(tag.active.eq(true))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 개수 쿼리
        Long total = jpaQueryFactory
                .select(tag.count())
                .from(tag)
                .where(tag.active.eq(true))
                .fetchOne();
        total = total != null ? total : 0L; // Null 안전 처리


        // Page 객체 반환
        return new PageImpl<>(tags, pageable, total);
    }

    //테그 수정
    @Override
    public void updateTag(Long tagId, TagRequestDto tagDto) {
        jpaQueryFactory.update(tag)
                .set(tag.tagName, tagDto.tagName())
                .where(tag.tagId.eq(tagId))
                .execute();
    }

    //태그 비활성화
    @Override
    public void inactiveTag(Long tagId) {
        jpaQueryFactory.update(tag)
                .set(tag.active, false)
                .where(tag.tagId.eq(tagId))
                .execute();
    }

    // 태그 검색
    @Override
    public List<TagResponseDto> findByTagNameList(String keyword){
        return from(tag)
            .where(tag.tagName.contains(keyword))
            .where(tag.active.eq(true))
            .select(Projections.constructor(TagResponseDto.class,
                tag.tagId,
                tag.tagName,
                tag.active
            ))
            .fetch();
    }
}