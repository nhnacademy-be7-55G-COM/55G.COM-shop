package shop.s5g.shop.repository.delivery.qdsl.impl;


import static shop.s5g.shop.entity.delivery.QDeliveryFee.deliveryFee;

import com.querydsl.core.BooleanBuilder;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import shop.s5g.shop.dto.delivery.DeliveryFeeUpdateRequestDto;
import shop.s5g.shop.entity.delivery.DeliveryFee;
import shop.s5g.shop.repository.delivery.qdsl.DeliveryFeeQuerydslRepository;

public class DeliveryFeeQuerydslRepositoryImpl extends QuerydslRepositorySupport implements
    DeliveryFeeQuerydslRepository {
    public DeliveryFeeQuerydslRepositoryImpl() {
        super(DeliveryFee.class);
    }

    @Override
    public Optional<DeliveryFee> updateFee(DeliveryFeeUpdateRequestDto updateRequest) {
        update(deliveryFee)
            .set(deliveryFee.fee, updateRequest.fee())
            .set(deliveryFee.condition, updateRequest.condition())
            .set(deliveryFee.name, updateRequest.name())
            .set(deliveryFee.refundFee, updateRequest.refundFee())
            .where(deliveryFee.id.eq(updateRequest.id()))
            .execute();

        return Optional.ofNullable(
            from(deliveryFee)
                .where(deliveryFee.id.eq(updateRequest.id()))
                .fetchOne()
        );
    }

    @Override
    public List<DeliveryFee> findInfoForPurchase() {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.or(deliveryFee.fee.eq(0l))
            .or(deliveryFee.condition.eq(0l));

        return from(deliveryFee)
            .where(booleanBuilder)
            .select(deliveryFee)
            .fetch();
    }

}
