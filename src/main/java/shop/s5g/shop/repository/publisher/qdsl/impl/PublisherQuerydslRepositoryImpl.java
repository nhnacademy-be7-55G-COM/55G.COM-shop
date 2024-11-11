package shop.s5g.shop.repository.publisher.qdsl.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import shop.s5g.shop.dto.publisher.PublisherRequestDto;
import shop.s5g.shop.dto.publisher.PublisherResponseDto;
import shop.s5g.shop.entity.Publisher;
import shop.s5g.shop.entity.QPublisher;
import shop.s5g.shop.repository.publisher.qdsl.PublisherQuerydslRepository;

import static shop.s5g.shop.entity.QPublisher.publisher;

@Repository
public class PublisherQuerydslRepositoryImpl extends QuerydslRepositorySupport implements PublisherQuerydslRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public PublisherQuerydslRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        super(Publisher.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @PersistenceContext
    private EntityManager em;

    //출판사 조회
    @Override
    public PublisherResponseDto getPublisher(Long publisherId) {

        QPublisher publisher1 = QPublisher.publisher;
        return new JPAQuery<>(em)
                .select(Projections.constructor(PublisherResponseDto.class,
                        publisher1.name,
                        publisher1.active))
                .from(publisher1)
                .where(publisher1.id.eq(publisherId))
                .fetchFirst();
    }

    //출판사 수정
    @Override
    public void updatePublisher(Long publisherId, PublisherRequestDto publisherRequestDto) {
        jpaQueryFactory.update(publisher)
                .set(publisher.name, publisherRequestDto.name())
                .set(publisher.active, publisherRequestDto.active())
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