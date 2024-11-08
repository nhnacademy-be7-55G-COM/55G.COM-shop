package shop.s5g.shop.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import shop.s5g.shop.entity.refund.RefundType;

public interface RefundTypeRepository extends JpaRepository<RefundType, Long> {

}
