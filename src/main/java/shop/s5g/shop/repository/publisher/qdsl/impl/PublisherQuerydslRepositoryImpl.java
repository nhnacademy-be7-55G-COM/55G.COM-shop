package shop.s5g.shop.repository.publisher.qdsl.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import shop.s5g.shop.dto.PageResponseDto;
import shop.s5g.shop.dto.publisher.PublisherRequestDto;
import shop.s5g.shop.dto.publisher.PublisherResponseDto;
import shop.s5g.shop.entity.Publisher;
import shop.s5g.shop.entity.QPublisher;
import shop.s5g.shop.repository.publisher.qdsl.PublisherQuerydslRepository;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;
import static shop.s5g.shop.entity.QPublisher.publisher;

@Repository
public class PublisherQuerydslRepositoryImpl extends QuerydslRepositorySupport implements PublisherQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public PublisherQuerydslRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Publisher.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    //출판사 조회
    @Override
    public PublisherResponseDto getPublisher(Long id) {

//        QPublisher publisher1 = QPublisher.publisher;
//        return new JPAQuery<>(em)
//                .select(Projections.constructor(PublisherResponseDto.class,
//                        publisher1.id,
//                        publisher1.name,
//                        publisher1.active))
//                .from(publisher1)
//                .where(publisher1.id.eq(publisherId))
//                .fetchFirst();
        return jpaQueryFactory.select(Projections.constructor(PublisherResponseDto.class,
                        publisher.id,
                        publisher.name,
                        publisher.active
                ))
                .from(publisher)
                .where(publisher.id.eq(id))
                .fetchOne();
    }

    //모든 출판사 조회
    @Override
    public Page<PublisherResponseDto> getAllPublisher(Pageable pageable) {
        List<PublisherResponseDto> publishers = jpaQueryFactory
                .select(Projections.constructor(PublisherResponseDto.class,
                        publisher.id,
                        publisher.name,
                        publisher.active
                ))
                .from(publisher)
                .where(publisher.active.eq(true))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 개수 쿼리
        Long total = jpaQueryFactory
                .select(publisher.count())
                .from(publisher)
                .where(publisher.active.eq(true))
                .fetchOne();
        total = total != null ? total : 0L; // Null 안전 처리


        // Page 객체 반환
        return new PageImpl<>(publishers, pageable, total);
    }


    //출판사 수정
    @Override
    public void updatePublisher(Long publisherId, PublisherRequestDto publisherRequestDto) {
        update(publisher)
                .set(publisher.name, publisherRequestDto.name())
                .where(publisher.id.eq(publisherId))
                .execute();
    }

    //출판사 삭제(비활성화)
    @Override
    public void deletePublisher(Long publisherId) {
        jpaQueryFactory.update(publisher)
                .set(publisher.active, false)
                .where(publisher.id.eq(publisherId))
                .execute();
    }
}