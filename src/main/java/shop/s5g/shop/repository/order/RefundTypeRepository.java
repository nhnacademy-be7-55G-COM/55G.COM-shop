package shop.s5g.shop.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.refund.RefundType;
import shop.s5g.shop.exception.EssentialDataNotFoundException;

public interface RefundTypeRepository extends JpaRepository<RefundType, Long> {
    default RefundType findTypeById(long id) {
        return findById(id).orElseThrow(
            () -> new EssentialDataNotFoundException("RefundType not found id="+id)
        );
    }
    default RefundType findByType(RefundType.Type type) {
        return findTypeById(type.getTypeId());
    }
}
