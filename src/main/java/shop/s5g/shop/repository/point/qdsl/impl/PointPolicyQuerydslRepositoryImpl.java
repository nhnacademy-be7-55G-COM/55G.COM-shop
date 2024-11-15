package shop.s5g.shop.repository.point.qdsl.impl;

import static shop.s5g.shop.entity.point.QPointPolicy.pointPolicy;
import static shop.s5g.shop.entity.point.QPointSource.pointSource;

import com.querydsl.core.types.Projections;
import java.util.List;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.dto.point.PointPolicyResponseDto;
import shop.s5g.shop.entity.point.PointPolicy;
import shop.s5g.shop.repository.point.qdsl.PointPolicyQuerydslRepository;

public class PointPolicyQuerydslRepositoryImpl extends QuerydslRepositorySupport implements PointPolicyQuerydslRepository {
    public PointPolicyQuerydslRepositoryImpl() {
        super(PointPolicy.class);
    }

    @Override
    public List<PointPolicyResponseDto> findAllPolicies() {
        return from(pointPolicy)
            .join(pointPolicy.pointSource, pointSource)
            .select(Projections.constructor(PointPolicyResponseDto.class,
                pointPolicy.id,
                pointPolicy.name,
                pointSource.sourceName,
                pointPolicy.value
                )
            )
            .fetch();
    }
}
