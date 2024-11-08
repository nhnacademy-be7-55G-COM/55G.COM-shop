package shop.S5G.shop.repository.tag.qdsl.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import shop.S5G.shop.dto.tag.TagRequestDto;
import shop.S5G.shop.dto.tag.TagResponseDto;
import shop.S5G.shop.entity.Tag;
import shop.S5G.shop.repository.tag.qdsl.TagQuerydslRepository;

import java.util.List;

import static shop.S5G.shop.entity.QTag.tag;

@Repository
public class TagQuerydslRepositoryImpl extends QuerydslRepositorySupport implements TagQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public TagQuerydslRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Tag.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    //모든 테그 조회
    @Override
    public List<TagResponseDto> findAllTag() {
        return jpaQueryFactory
                .select(Projections.constructor(TagResponseDto.class,
                        tag.tagName,
                        tag.active))
                .from(tag)
                .fetch();
    }

    //테그 수정
    @Override
    public void updateTag(Long tagId, TagRequestDto tagDto) {
        jpaQueryFactory.update(tag)
                .set(tag.tagName, tagDto.tagName())
                .set(tag.active, tagDto.active())
                .where(tag.tagId.eq(tagId))
                .execute();
    }

    //태그 비활성화
    @Override
    public void inactiveTag(Long tagId) {
        update(tag)
                .set(tag.active, false)
                .where(tag.tagId.eq(tagId))
                .execute();
    }
}